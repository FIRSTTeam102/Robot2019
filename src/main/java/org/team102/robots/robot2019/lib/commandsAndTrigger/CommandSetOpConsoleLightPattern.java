package org.team102.robots.robot2019.lib.commandsAndTrigger;

import org.team102.robots.robot2019.lib.CustomOperatorConsole;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class CommandSetOpConsoleLightPattern extends InstantCommand {
	
	private CustomOperatorConsole opCon;
	private int pattern;
	
	public CommandSetOpConsoleLightPattern(CustomOperatorConsole opCon, int pattern) {
		super("Set Operator Console Light Pattern: " + pattern);
		
		this.opCon = opCon;
		this.pattern = pattern;
	}
	
	@Override
	public void initialize() {
		opCon.setLightPattern(pattern);
	}
}