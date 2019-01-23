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

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Timer;

public class VisionCameraHelper {
	
	/**
	 * Opens the given camera, checking whether or not it is existent and supported in the process
	 * @param deviceID The camera's ID number
	 * @return The camera, whether or not it exists or is supported, unless an invalid ID is given
	 */
	public static UsbCamera openAndVerifyCamera(int deviceID) {
		if(deviceID < 0) {
			System.out.println("Warning: Given an invalid camera ID " + deviceID + "; ignoring...");
			return null;
		}
		
		UsbCamera cam = new UsbCamera("USB Camera " + deviceID, deviceID);
		
		if(!(cam.isConnected() && cam.isValid())) {
			System.err.println("Error: Attempted to connect to a non-existant or unsupported camera with ID " + deviceID);
		} else {
			CameraServer.getInstance().startAutomaticCapture(cam);
		}
		
		return cam;
	}
	
	/**
	 * Starts an OpenCV vision pipeline
	 * @param camera The camera to be used as input
	 * @param width The width of the video stream
	 * @param height The width of the video stream
	 * @param outputName The name of the video stream that will be sent to the camera server, from the output of the pipeline, or null to disable output
	 * @param pipeline The vision pipeline
	 * @param pipelineOutput The supplier of the frames that were output by the pipeline
	 */
	public static void startPipeline(VideoSource camera, int width, int height, String outputName, Pipeline pipeline/*, Supplier<Mat> pipelineOutput*/) {
		if(camera == null || !(camera.isConnected() && camera.isValid())) {
			return;
		}
		
		camera.setResolution(width, height);
		
		CvSink input = CameraServer.getInstance().getVideo(camera);
		CvSource[] output = new CvSource[1]; // It's an array to avoid the final-or-effectively-final requirement for lambda methods
		
		if(outputName != null) {
			output[0] = CameraServer.getInstance().putVideo(outputName, width, height);
		}
		
		Thread pipelineRunnerThread = new Thread(pipeline, "Vision runner: Pipeline " + pipeline);
		
		Thread pipelineTransferRunnerThread = new Thread(() -> {
			Mat inputFrame = new Mat();
			boolean lastFrameErrored = false;
			
			while(true) {
				try {
					input.grabFrame(inputFrame);
					pipeline.supplyInput(inputFrame);
					
					if(output[0] != null) {
						Mat outputFrame = pipeline.getOutput();
						
						if(!(outputFrame == null || outputFrame.empty())) {
							output[0].putFrame(outputFrame);
						}
					}
				} catch(Exception e) {
					System.err.println("Failed to grab frame for the vision pipeline for " + camera.getName() + ": " + e.getClass() + ": " + e.getLocalizedMessage());
					
					if(lastFrameErrored) {
						System.err.println("This has happened twice in a row. The vision pipeline will now quit to prevent console spam.");
						e.printStackTrace();
						
						break;
					} else {
						lastFrameErrored = true;
					}
				}
			}
		}, "Vision runner: "+ camera.getName());
		
		pipelineRunnerThread.setDaemon(true);
		pipelineRunnerThread.start();
		
		pipelineTransferRunnerThread.setDaemon(true);
		pipelineTransferRunnerThread.start();
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
					if(inputs > 1) {
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