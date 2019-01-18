package org.team102.robots.robot2019.subsystems;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.*;
import org.opencv.core.*;
import org.opencv.imgproc.*;


public class SubsystemVision {
	
	//UsbCamera usbCam0 = CameraServer.getInstance().startAutomaticCapture();
	CvSink cam0Sink = CameraServer.getInstance().getVideo();
	
	public void periodic(){
		
	Mat frame = new Mat();
	Mat blurFrame = new Mat();
	cam0Sink.grabFrame(frame);
	
	
	Imgproc.GaussianBlur(frame, blurFrame, new Size(frame.width(), frame.height()), 0);
	
	
	
		
	}
	
	
	
	
	
	
	

}
