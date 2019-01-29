package org.team102.robots.robot2019.commands;

import org.team102.robots.robot2019.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class CommandSetDSVideoOutput extends InstantCommand {
	
	private String streamName;
	
	public CommandSetDSVideoOutput(String streamName) {
		super(streamName);
		setRunWhenDisabled(true);
		
		requires(Robot.driverNotif);
		this.streamName = streamName;
	}
	
	public void initialize() {
		Robot.driverNotif.setVideoStream(streamName);
	}
}