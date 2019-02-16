package org.team102.robots.robot2019.commands;

import org.team102.robots.robot2019.Robot;

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
		Robot.climber.setClimberExtended(extended);
	}
}