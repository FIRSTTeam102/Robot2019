package org.team102.robots.robot2019.subsystems;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Map;

import org.team102.robots.robot2019.Robot;
import org.team102.robots.robot2019.commands.CommandSetDSVideoOutput;
import org.team102.robots.robot2019.lib.VisionCameraHelper;

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
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
	
	//private CvSink srcSink;
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
		
		videoOutput = CameraServer.getInstance().addServer("Selected Video Stream");
		VisionCameraHelper.advertiseServerToShuffleboard(videoOutput, driverInfoTab).withPosition(2, 0).withSize(3, 3);
		
		for(VideoSource src : Robot.cameras.visibleVideoOutputs) {
			if(src != null) {
				selectCameraLayout.add(new CommandSetDSVideoOutput(src.getName())).withWidget(BuiltInWidgets.kCommand);
			}
		}
		
		setVideoStream(Robot.cameras.visibleVideoOutputs.get(0).getName());
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
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void periodic() {
		double time = Math.max(0, DriverStation.getInstance().getMatchTime());
		timeLeftPane.setString("Time Remaining: " + ROUNDING_FORMATTER.format(time));
		
		if(time <= 30 && RobotState.isOperatorControl() && RobotState.isEnabled()) {
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