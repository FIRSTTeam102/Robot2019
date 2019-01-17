package org.team102.robots.robot2019.subsystems.cameras;

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
	
	public static void startPipeline(VideoSource camera, int width, int height, String outputName, Pipeline pipeline) {
		startPipeline(camera, width, height, outputName, (Function<Mat, Mat>)pipeline::process);
	}
	
	public static void startPipeline(VideoSource camera, int width, int height, String outputName, Function<Mat, Mat> pipeline) {
		Mat[] latestOutput = new Mat[1];
		
		VisionPipeline frcPipeline = (input) -> { latestOutput[0] = pipeline.apply(input); };
		Supplier<Mat> frcPipelineOutput = () -> latestOutput[0];
		
		startPipeline(camera, width, height, outputName, frcPipeline, frcPipelineOutput);
	}
	
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
	
	public static interface Pipeline {
		Mat process(Mat input);
	}
}