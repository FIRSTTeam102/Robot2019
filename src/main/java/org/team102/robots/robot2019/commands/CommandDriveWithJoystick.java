package org.team102.robots.robot2019.commands;

import org.team102.robots.robot2019.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CommandDriveWithJoystick extends Command {
	
	public CommandDriveWithJoystick() {
		super("Drive with Joystick");
		requires(Robot.driveTrain);
	}
	
	@Override
	public void execute() {
		Robot.driveTrain.driveWithJoystick(Robot.oi.driverJoystick);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
}