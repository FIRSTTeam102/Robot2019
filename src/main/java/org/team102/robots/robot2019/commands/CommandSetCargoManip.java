package org.team102.robots.robot2019.commands;

import org.team102.robots.robot2019.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CommandSetCargoManip extends Command {
	
	private boolean isReverse;
	private boolean hasTimeout = false;
	
	public CommandSetCargoManip(boolean isReverse) {
		this(isReverse, -1);
	}
	
	public CommandSetCargoManip(boolean isReverse, double timeout) {
		super("Set Cargo Manipulator: { reverse: " + isReverse + ", timeout: " + timeout + " }");
		requires(Robot.cargoManip);
		
		this.isReverse = isReverse;
		
		if(timeout > 0) {
			setTimeout(timeout);
			hasTimeout = true;
		}
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
		if(hasTimeout) {
			return isTimedOut();
		} else {
			return false;
		}
	}
}