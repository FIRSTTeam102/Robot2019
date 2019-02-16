package org.team102.robots.robot2019.commands;

import org.team102.robots.robot2019.Robot;
import org.team102.robots.robot2019.RobotMap;

import edu.wpi.first.wpilibj.command.TimedCommand;

public class CommandDriveForwardTimed extends TimedCommand {
	
	public CommandDriveForwardTimed(double time) {
		super("Drive forward for " + time + " seconds", time);
		requires(Robot.driveTrain);
	}
	
	@Override
	public void execute() {
		Robot.driveTrain.drive(RobotMap.DRIVE_TRAIN_AUTONOMOUS_FORWARD_SPEED, 0, 0);
	}
}