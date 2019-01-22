package org.team102.robots.robot2019.subsystems;

import org.opencv.core.Mat;
import org.team102.robots.robot2019.Robot;
import org.team102.robots.robot2019.RobotMap;
import org.team102.robots.robot2019.lib.VisionCameraHelper;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SubsystemCameras extends Subsystem {
	
	public SubsystemCameras() {
		super("Cameras");
		
		if(UsbCamera.enumerateUsbCameras().length > 0) {
			initialize();
		} else {
			if(Robot.isReal()) {
				System.out.println("Warning: This is a real robot, and no cameras were detected! Check the USB ports?");
			} else {
				System.out.println("No camera is connected to the computer running the sim. Vision will be skipped.");
			}
		}
	}
	
	private void initialize() {
		UsbCamera visionCamera = CameraServer.getInstance().startAutomaticCapture(RobotMap.CAMERA_ID_VISION);
		VisionCameraHelper.startPipeline(visionCamera, 320, 240, "Vision Pipeline", new Pipe());
	}
	
	protected void initDefaultCommand() {}
	
	private class Pipe extends VisionCameraHelper.Pipeline {
		
		public Mat process(Mat input) {
			return input;
		}
	}
}