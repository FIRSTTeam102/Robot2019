package org.team102.robots.robot2019.commands;

import org.team102.robots.robot2019.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class CommandAdvanceDSVideoOutput extends InstantCommand {
	
	public CommandAdvanceDSVideoOutput() {
		super("Advance DS Video Output");
		requires(Robot.driverNotif);
	}
	
	@Override
	public void initialize() {
		Robot.driverNotif.advanceVideoOutput();
	}
}