package org.team102.robots.robot2019.subsystems;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SubsystemCameras extends Subsystem {
	private CvSink cam0Sink = CameraServer.getInstance().getVideo();
	
	private Mat frame = new Mat();
	private Mat blurFrame = new Mat();
	
	public SubsystemCameras() {
		super("Cameras");
	}
	
	public void periodic(){
		cam0Sink.grabFrame(frame);
		Imgproc.GaussianBlur(frame, blurFrame, new Size(frame.width(), frame.height()), 0);
	}
	
	protected void initDefaultCommand() {}
}