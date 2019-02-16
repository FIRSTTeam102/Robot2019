package org.team102.robots.robot2019.commands;

import org.team102.robots.robot2019.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CommandSetHatchManip extends Command {

	public CommandSetHatchManip() {
		super("Set Hatch Manipulator");
		requires(Robot.hatchManip);
	}
	
	@Override
	public void initialize() {
		Robot.hatchManip.setEjector(true);
	}
	
	private void done() {
		Robot.hatchManip.setEjector(false);
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