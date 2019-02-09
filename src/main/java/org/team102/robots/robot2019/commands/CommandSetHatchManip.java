package org.team102.robots.robot2019.commands;

import org.team102.robots.robot2019.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class CommandSetHatchManip extends InstantCommand {

	private boolean extended;
	
	public CommandSetHatchManip(boolean extended) {
		super("Set Hatch Manipulator: " + (extended ? "Extended" : "Contracted"));
		requires(Robot.hatchManip);
		
		this.extended = extended;
	}
	
	@Override
	public void initialize() {
		Robot.hatchManip.setEjector(extended);
	}
}