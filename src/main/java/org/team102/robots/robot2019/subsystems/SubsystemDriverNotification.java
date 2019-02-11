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
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.shuffleboard.*;

public class SubsystemDriverNotification extends Subsystem {
	public static final Map<String, Object> PROP_HIDE_LABELS = Collections.<String, Object>singletonMap("Label Position", "HIDDEN");
	public static final DecimalFormat ROUNDING_FORMATTER = new DecimalFormat("#.#");
	
	private ShuffleboardTab driverInfoTab;
	
	private NetworkTableEntry lowTimeNotifier;
	private NetworkTableEntry timeLeftPane;
	
	private boolean lowTimeTriggered = false;
	private int notificationBlinkTime = 0;
	
	private NetworkTableEntry selectedStreamName;
	private MjpegServer videoOutput;
	
	public SubsystemDriverNotification() {
		super("Driver Notification");
		
		driverInfoTab = Shuffleboard.getTab("Driver Information");
		
		ShuffleboardLayout timeLayout = driverInfoTab
				.getLayout("Time", BuiltInLayouts.kList)
				.withPosition(0, 0).withSize(2, 1)
				.withProperties(PROP_HIDE_LABELS);
		
		timeLeftPane = timeLayout.add("Time Remaining", "").getEntry();
		lowTimeNotifier = timeLayout.add("Low Time", false).getEntry();
	}
	
	public void initOIPortions() {
		ShuffleboardLayout selectCameraLayout = driverInfoTab
				.getLayout("Video Selector", BuiltInLayouts.kList)
				.withPosition(0, 1).withSize(2, 2)
				.withProperties(PROP_HIDE_LABELS);
		
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
			if(!lowTimeTriggered) {
				lowTimeTriggered = true;
				
				onBeginningOfLowTime();
			}
			
			notificationBlinkTime++;
			if(notificationBlinkTime >= 15) {
				notificationBlinkTime = 0;
				
				blinkNotification();
			}
		}
	}
	
	private void onBeginningOfLowTime() {
		// TODO rummble controller
	}
	
	private void blinkNotification() {
		lowTimeNotifier.setBoolean(!lowTimeNotifier.getBoolean(false));
	}
}