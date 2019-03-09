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

package org.team102.robots.robot2019.subsystems;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Map;

import org.team102.robots.robot2019.Robot;
import org.team102.robots.robot2019.RobotMap;
import org.team102.robots.robot2019.commands.CommandSetDSVideoOutput;
import org.team102.robots.robot2019.lib.VisionCameraHelper;

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.shuffleboard.*;

public class SubsystemDriverNotification extends Subsystem {
	public static final Map<String, Object> PROP_HIDE_LABELS = Collections.<String, Object>singletonMap("Label Position", "HIDDEN");
	public static final DecimalFormat ROUNDING_FORMATTER = new DecimalFormat("#.#");
	
	private ShuffleboardTab driverInfoTab;
	private ShuffleboardTab visionInfoTab;
	private NetworkTableEntry lowTimeNotifier;
	private NetworkTableEntry timeLeftPane;
	
	private int notificationBlinkTime = 0;
	private int prevCountdownStage = -1;
	
	private NetworkTableEntry selectedStreamName;
	private MjpegServer videoOutput;
	
	private NetworkTableEntry armElbowStatus;
	private NetworkTableEntry armWristStatus;
	private NetworkTableEntry armOverallStatus;
	
	private NetworkTableEntry frontCenteringInfo;
	private NetworkTableEntry rearCenteringInfo;
	private NetworkTableEntry centeringStatus;
	
	private NetworkTableEntry climberStatus;
		
	public SubsystemDriverNotification() {
		super("Driver Notification");
		
		addChild("The PDP", Robot.pdp);
		
		driverInfoTab = Shuffleboard.getTab("Driver Information");
		visionInfoTab = Shuffleboard.getTab("Vision Information");
		
		ShuffleboardLayout timeLayout = driverInfoTab
				.getLayout("Time", BuiltInLayouts.kList)
				.withPosition(0, 0).withSize(2, 1)
				.withProperties(PROP_HIDE_LABELS);
		
		timeLeftPane = timeLayout.add("Time Remaining", "").getEntry();
		lowTimeNotifier = timeLayout.add("Low Time", false).getEntry();
		
		ShuffleboardLayout centeringStatusLayout = driverInfoTab
				.getLayout("Centering", BuiltInLayouts.kList)
				.withPosition(5, 0).withSize(1, 3);
		
		frontCenteringInfo = centeringStatusLayout.add("Front", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
		rearCenteringInfo = centeringStatusLayout.add("Rear", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
		centeringStatus = centeringStatusLayout.add("Status", "").getEntry();
		
		ShuffleboardLayout armStatusLayout = driverInfoTab
				.getLayout("Arm Status", BuiltInLayouts.kList)
				.withPosition(6, 0).withSize(2, 2);
		
		armElbowStatus = armStatusLayout.add("Status: Elbow", "").getEntry();
		armWristStatus = armStatusLayout.add("Status: Wrist", "").getEntry();
		armOverallStatus = armStatusLayout.add("Overall", "").getEntry();
		
		climberStatus = driverInfoTab
				.add("Climber Status", "")
				.withPosition(6, 2).withSize(2, 1)
				.getEntry();
		
		//vision layout starts here
		ShuffleboardLayout HSVLowLayout = visionInfoTab
				.getLayout("HSVLow", BuiltInLayouts.kList)
				.withPosition(2,0).withSize(2,3);
		
		ShuffleboardLayout HSVHighLayout = visionInfoTab
				.getLayout("HSVHigh", BuiltInLayouts.kList)
				.withPosition(4,0).withSize(2,3);
		
		ShuffleboardLayout cameraSettings = visionInfoTab
				.getLayout("cameraSettings", BuiltInLayouts.kList)
				.withPosition(6,1).withSize(2, 3);
		
		//starting values, 29, 75, 150
		HSVLowLayout.add("hueLow", 29).withWidget(BuiltInWidgets.kNumberSlider);
		HSVLowLayout.add("saturationLow", 75).withWidget(BuiltInWidgets.kNumberSlider);
		HSVLowLayout.add("valueLow", 150).withWidget(BuiltInWidgets.kNumberSlider);
		
		//starting values 64, 255, 255
		HSVHighLayout.add("hueHigh", 64).withWidget(BuiltInWidgets.kNumberSlider);
		HSVHighLayout.add("saturationHigh", 255).withWidget(BuiltInWidgets.kNumberSlider);
		HSVHighLayout.add("valueHigh", 255).withWidget(BuiltInWidgets.kNumberSlider);
		
		cameraSettings.add("exposure", 20).withWidget(BuiltInWidgets.kNumberSlider);
		cameraSettings.add("brightness", 55).withWidget(BuiltInWidgets.kNumberSlider);
		cameraSettings.add("shuttterSpeed",5000).withWidget(BuiltInWidgets.kNumberSlider);
		cameraSettings.add("sharpness", 60).withWidget(BuiltInWidgets.kNumberSlider);
		
		//image display
	}
	
	public void initOIPortions() {
		ShuffleboardLayout selectCameraLayout = driverInfoTab
				.getLayout("Video Selector", BuiltInLayouts.kList)
				.withPosition(0, 1).withSize(2, 2)
				.withProperties(PROP_HIDE_LABELS);
		
		NetworkTableEntry piCamToggle = visionInfoTab
				.add("piCamSelector", false)
				.withPosition(0, 2).withSize(2,2)
				.withWidget(BuiltInWidgets.kToggleSwitch)
				.getEntry();
		
		MjpegServer piVisionOutput = CameraServer.getInstance().addServer("Vision Pi Output (Debug)");
		VisionCameraHelper.advertiseServerToShuffleboard(piVisionOutput, visionInfoTab).withPosition(2, 0).withSize(3, 3);
		
		piCamToggle.addListener(notif -> {
			if(piCamToggle.getBoolean(false)) {
				piVisionOutput.setSource(Robot.cameras.piVisionOutput);
			} else {
				piVisionOutput.setSource(null);
			}
		}, EntryListenerFlags.kUpdate);
		
		selectedStreamName = selectCameraLayout.add("Selected Stream Name", "").getEntry();
		videoOutput = CameraServer.getInstance().addServer("Selected Video Stream");
		
		boolean streamSet = false;
		for(VideoSource src : Robot.cameras.visibleVideoOutputs) {
			if(src != null) {
				String name = src.getName();
				selectCameraLayout.add(new CommandSetDSVideoOutput(name)).withWidget(BuiltInWidgets.kCommand);
				
				if(!streamSet) {
					setVideoStream(name);
					streamSet = true;
				}
			}
		}
		
		if(streamSet) {
			VisionCameraHelper.advertiseServerToShuffleboard(videoOutput, driverInfoTab).withPosition(2, 0).withSize(3, 3);
		} else {
			System.out.println("Warning: No video streams present! Skipping video output to the driver station.");
		}
	}
	
	public void setVideoStream(String name) {
		VideoSource source = null;
		for(VideoSource possibleSource : Robot.cameras.visibleVideoOutputs) {
			if(possibleSource != null && possibleSource.getName().equals(name)) {
				source = possibleSource;
				break;
			}
		}
		
		if(source == null) {
			System.out.println("Warning: Tried to switch to non-existant camera " + name);
			return;
		}
		
		videoOutput.setSource(source);
		selectedStreamName.setString("Current Stream: " + name);
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void periodic() {
		double time = Robot.oi.getTimeRemaining();
		timeLeftPane.setString("Time Remaining: " + ROUNDING_FORMATTER.format(time));
		
		if(time <= RobotMap.LOW_TIME && RobotState.isOperatorControl() && RobotState.isEnabled()) {
			notificationBlinkTime++;
			if(notificationBlinkTime >= 15) {
				notificationBlinkTime = 0;
				
				blinkNotification();
			}
			
			updateOpConsoleLights(time);
		}
		
		armElbowStatus.setString(Robot.arm.getElbowStatus());
		armWristStatus.setString(Robot.arm.getWristStatus());
		armOverallStatus.setString(Robot.arm.getOverallStatus());
		
		frontCenteringInfo.setNumber(Robot.centering.getFrontValue());
		rearCenteringInfo.setNumber(Robot.centering.getRearValue());
		centeringStatus.setString(Robot.centering.getStatus());
		
		climberStatus.setString(Robot.climber.getStatus());
	}
	
	private void blinkNotification() {
		lowTimeNotifier.setBoolean(!lowTimeNotifier.getBoolean(false));
	}
	
	private void updateOpConsoleLights(double time) {
		int numStages = RobotMap.OP_CONTROLLER_PATTERN_SET_COUNTDOWN.length;
		double timePerStage = RobotMap.LOW_TIME / numStages;
		
		int countdownStage = numStages - (int)Math.ceil(Math.max(time, .01) / timePerStage);
		
		if(countdownStage != prevCountdownStage) {
			prevCountdownStage = countdownStage;
			
			RobotMap.RUMBLE_LOW_TIME.play(Robot.oi.driverJoystick);
			Robot.oi.opConsole.setLightPattern(RobotMap.OP_CONTROLLER_PATTERN_SET_COUNTDOWN[countdownStage]);
		}
	}
}