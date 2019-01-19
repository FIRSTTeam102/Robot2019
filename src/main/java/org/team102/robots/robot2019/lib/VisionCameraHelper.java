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

import java.util.function.Function;
import java.util.function.Supplier;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;

import edu.wpi.first.vision.VisionPipeline;
import edu.wpi.first.vision.VisionRunner;
import edu.wpi.first.vision.VisionThread;

public class VisionCameraHelper {
	
	/**
	 * Starts an OpenCV vision pipeline
	 * @param camera The camera to be used as input
	 * @param width The width of the video stream
	 * @param height The width of the video stream
	 * @param outputName The name of the video stream that will be sent to the camera server, from the output of the pipeline, or null to disable output
	 * @param pipeline The vision pipeline
	 */
	public static void startPipeline(VideoSource camera, int width, int height, String outputName, Pipeline pipeline) {
		startPipeline(camera, width, height, outputName, (Function<Mat, Mat>)pipeline::process);
	}
	
	/**
	 * Starts an OpenCV vision pipeline
	 * @param camera The camera to be used as input
	 * @param width The width of the video stream
	 * @param height The width of the video stream
	 * @param outputName The name of the video stream that will be sent to the camera server, from the output of the pipeline, or null to disable output
	 * @param pipeline The vision pipeline
	 */
	public static void startPipeline(VideoSource camera, int width, int height, String outputName, Function<Mat, Mat> pipeline) {
		Mat[] latestOutput = new Mat[1];
		
		VisionPipeline frcPipeline = (input) -> { latestOutput[0] = pipeline.apply(input); };
		Supplier<Mat> frcPipelineOutput = () -> latestOutput[0];
		
		startPipeline(camera, width, height, outputName, frcPipeline, frcPipelineOutput);
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
	public static void startPipeline(VideoSource camera, int width, int height, String outputName, VisionPipeline pipeline, Supplier<Mat> pipelineOutput) {
		camera.setResolution(width, height);
		
		VisionRunner.Listener<VisionPipeline> listener;
		if(pipelineOutput != null && outputName != null) {
			CvSource processedOutput = CameraServer.getInstance().putVideo(outputName, width, height);
			listener = (pipe) -> processedOutput.putFrame(pipelineOutput.get());
		} else {
			listener = (pipe) -> {};
		}
		
		VisionThread thread = new VisionThread(camera, pipeline, listener);
		thread.setName(thread.getName() + " (" + outputName + ")");
		thread.start();
	}
	
	/**
	 * An interface to be implemented by any vision pipeline
	 */
	public static interface Pipeline {
		/**
		 * The main pipeline processing method
		 * @param input The frame of video to be processed
		 * @return The frame of video, after being processed
		 */
		Mat process(Mat input);
	}
}