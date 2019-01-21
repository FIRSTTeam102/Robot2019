package org.team102.robots.robot2019.subsystems;

import org.opencv.core.Mat;
import org.team102.robots.robot2019.lib.VisionCameraHelper;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SubsystemCameras extends Subsystem {
	private UsbCamera visionCamera;
	
	public SubsystemCameras() {
		super("Cameras");
		
		visionCamera = CameraServer.getInstance().startAutomaticCapture();
		VisionCameraHelper.startPipeline(visionCamera, 320, 240, "Vision Pipeline", new Pipe());
	}
	
	protected void initDefaultCommand() {}
	
	private class Pipe extends VisionCameraHelper.Pipeline {
		
		public Mat process(Mat input) {
			return input;
		}
	}
}