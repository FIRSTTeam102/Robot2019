package org.team102.robots.robot2019.commands;

import org.team102.robots.robot2019.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CommandSetCargoManip extends Command {
	
	private boolean isReverse;
	
	public CommandSetCargoManip(boolean isReverse) {
		super("Set Cargo Manipulator: " + (isReverse ? "Reverse" : "Forward"));
		requires(Robot.cargoManip);
		
		this.isReverse = isReverse;
	}
	
	@Override
	public void initialize() {
		Robot.cargoManip.setRoller(isReverse);
	}
	
	private void done() {
		Robot.cargoManip.stopRoller();
	}
	
	@Override
	public void end() {
		done();
	}
	
	@Override
	public void interrupted() {
		done();
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
}