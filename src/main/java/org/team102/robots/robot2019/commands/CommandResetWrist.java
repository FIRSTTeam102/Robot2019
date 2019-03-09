package org.team102.robots.robot2019.commands;

import org.team102.robots.robot2019.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CommandResetWrist extends Command {
	
	public CommandResetWrist() {
		super("Reset Arm's Wrist");
		requires(Robot.arm);
	}
	
	protected void initialize() {
		Robot.arm.setArmManual(true, true);
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.arm.isWristLimitedDown() || !Robot.arm.hasWristLowerLimitSwitch();
	}
	
	protected void end() {
		done();
		Robot.arm.resetWristAccelerometer();
	}
	
	protected void interrupted() {
		done();
	}
	
	private void done() {
		Robot.arm.endManualMode();
	}
}