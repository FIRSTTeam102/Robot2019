/*
 * The code for FRC Team #102's robot for the 2019 game, Destination: Deep Space
 * Copyright (C) 2019  Robotics Fund Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * 
 * Contact us at: firstteam102@gmail.com
 */

package org.team102.robots.robot2019.lib;

import org.opencv.core.Mat;
import org.team102.robots.robot2019.Robot;

import edu.wpi.cscore.CameraServerJNI;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.Threads;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class VisionCameraHelper {
	
	/**
	 * Because setting resolution on desktop causes a crash, this will set the resolution, but only on a real RoboRIO
	 * @param src The camera or other video source
	 * @param width The desired width
	 * @param height The desired height
	 */
	public static void safeSetResolution(VideoSource src, int width, int height) {
		if(Robot.isReal()) {
			src.setResolution(width, height);
		}
	}
	
	/**
	 * Adds the given MJPEG server to the given Shuffleboard container as a video stream
	 * @param srv The server
	 * @param parent Where to put the stream
	 * @return The Shuffleboard widget that holds the stream, able to be moved and sized as normal
	 */
	public static ComplexWidget advertiseServerToShuffleboard(MjpegServer srv, ShuffleboardContainer parent) {
		String name = srv.getName();
		
		ComplexWidget widge = parent.add(name, new SendableBase() {
			public void initSendable(SendableBuilder builder) {
				builder.addStringProperty(".ShuffleboardURI", () -> "camera_server://" + name, null);
			}
		});
		
		NetworkTableInstance.getDefault().getTable("/CameraPublisher/" + srv.getName()).getEntry("streams").setStringArray(new String[] {
				"mjpg:http://" + CameraServerJNI.getHostname() + ".local:" + srv.getPort() + "/?action=stream"
		});
		
		return widge;
	}
	
	/**
	 * Start a transfer thread
	 * @param transferFunc The transfer function
	 * @param name The name for the thread
	 */
	public static void startTransferThread(Runnable transferFunc, String name) {
		Thread runner = new Thread(() -> {
			Threads.setCurrentThreadPriority(true, 10);
			boolean lastFrameErrored = false;
			
			while(true) {
				try {
					transferFunc.run();
				} catch(Exception e) {
					System.err.println("Failed to transfer frame for " + name + ": " + e.getClass() + ": " + e.getLocalizedMessage());
					
					if(lastFrameErrored) {
						System.err.println("This has happened twice in a row. The vision pipeline will now quit to prevent console spam.");
						e.printStackTrace();
						
						break;
					} else {
						lastFrameErrored = true;
					}
				}
			}
		}, "Transfer: "+ name);
		
		runner.setDaemon(true);
		runner.start();
	}
	
	/**
	 * Opens the given camera, checking whether or not it is existent and supported in the process<br>
	 * <strong>Make sure you check to see if the given width, height, and FPS are supported by the camera.<br>
	 * Just because OpenCV reports it as a valid video mode, doesn't mean it actually works with the camera</strong>
	 * @param name The name for the camera
	 * @param deviceID The camera's ID number
	 * @param width The width of the stream
	 * @param height The height of the stream
	 * @param fps The FPS of the stream
	 * @param brightness The brightness of the camera
	 * @param autoCapture Whether to add a video output for it, or just add it to the camera server
	 * @return The camera, whether or not it exists or is supported, unless an invalid ID is given
	 */
	public static VideoSource openAndVerifyCamera(String name, int deviceID, int width, int height, int fps, int brightness, boolean autoCapture) {
		if(deviceID < 0) {
			System.out.println("Warning: Given an invalid camera ID " + deviceID + "; ignoring...");
			return null;
		}
		
		try {
			UsbCamera cam = new UsbCamera(name, deviceID);
			
			safeSetResolution(cam, width, height);
			cam.setFPS(fps);
			cam.setExposureManual(brightness);
			
			if(autoCapture) {
				CameraServer.getInstance().startAutomaticCapture(cam);
			} else {
				CameraServer.getInstance().addCamera(cam);
			}
			
			return cam;
		} catch(Exception e) {
			System.err.println("Camera \"" + name + "\" was not found.");
			return null;
		}
	}
	

	
	/**
	 * Starts an OpenCV vision pipeline
	 * @param camera The camera to be used as input
	 * @param width The width of the video stream
	 * @param height The width of the video stream
	 * @param outputName The name of the video stream that will be sent to the camera server, from the output of the pipeline, or null to disable output
	 * @param autoCapture Whether to add a video output for it, or just add it to the camera server
	 * @param pipeline The vision pipeline
	 * @return The video stream that results from the pipeline processing
	 */
	public static CvSource startPipeline(VideoSource camera, int width, int height, String outputName, boolean autoCapture, Pipeline pipeline) {
		if(camera == null) {
			return null;
		}
		
		CvSink input = CameraServer.getInstance().getVideo(camera);
		CvSource[] output = new CvSource[1]; // It's an array to avoid the final-or-effectively-final requirement for lambda methods
		
		if(outputName != null) {
			output[0] = new CvSource(outputName, VideoMode.PixelFormat.kBGR, width, height, 30); // It will advertise this output, but it won't actually create a server, so it's fine
			
			if(autoCapture) {
				CameraServer.getInstance().startAutomaticCapture(output[0]);
			}
		
		}
		
		Thread pipelineRunnerThread = new Thread(pipeline, "Vision runner: Pipeline " + pipeline);
		pipelineRunnerThread.setDaemon(true);
		pipelineRunnerThread.start();
		
		Mat[] frameBuff = { new Mat() };
		startTransferThread(() -> {
			input.grabFrame(frameBuff[0]);
			pipeline.supplyInput(frameBuff[0]);
			
			if(output[0] != null) {
				Mat outputFrame = pipeline.getOutput();
				
				if(!(outputFrame == null || outputFrame.empty())) {
					output[0].putFrame(outputFrame);
				}
			}
		}, "Vision Pipeline: " + pipeline);
		
		return output[0];
	}
	
	/**
	 * An interface to be implemented by any vision pipeline
	 */
	public static abstract class Pipeline implements Runnable {
		private Mat lastInput = null;
		private Mat lastOutput = null;
		private int inputs = 0;
		
		private Object frameLock = new Object();
		
	
		
		/**
		 * The main pipeline processing method
		 * @param input The frame of video to be processed
		 * @return The frame of video, after being processed
		 */
		public abstract Mat process(Mat input);
		
	
		
		public void run() {
			while(true) {
				if(inputs > 0) {
					if(inputs > 5) {
						System.out.println("Warning: Skipped " + (inputs - 1) + " frame(s) for vision pipeline " + this);
					}
					
					synchronized(frameLock) {
						lastOutput = process(lastInput);
					}
					
					inputs = 0;
				} else {
					Timer.delay(.01);
				}
			}
		}
		
		public void supplyInput(Mat input) {
			if(input == null || input.empty()) {
				return;
			}
			
			synchronized(frameLock) {
				lastInput = input;
				inputs++;
			}
		}
		
		public Mat getOutput() {
			synchronized(frameLock) {
				return lastOutput;
			}
		}
		
		}
}