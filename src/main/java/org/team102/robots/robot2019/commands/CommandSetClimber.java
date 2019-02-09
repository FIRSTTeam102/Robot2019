package org.team102.robots.robot2019.commands;

import org.team102.robots.robot2019.Robot;
import org.team102.robots.robot2019.RobotMap;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class CommandSetClimber extends InstantCommand {
	
	private boolean extended;
	
	public CommandSetClimber(boolean extended) {
		super("Set Climber: " + (extended ? "Extended" : "Contracted"));
		requires(Robot.climber);
		
		this.extended = extended;
	}
	
	@Override
	public void initialize() {
		if(extended) {
			if(Robot.oi.getTimeRemaining() <= RobotMap.LOW_TIME) {
				Robot.climber.setClimberExtended(true);
			} else {
				System.out.println("Warning: Tried to climb outside of low time, so we won't!");
			}
		} else {
			Robot.climber.setClimberExtended(false);
		}
	}
}