package org.team102.robots.robot2019.commands;

import org.team102.robots.robot2019.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class CommandToggleOverrideLimitSwitch extends InstantCommand {
	
	public CommandToggleOverrideLimitSwitch() {
		super("Override Arm Limit");
		requires(Robot.arm);
	}
	
	@Override
	public void initialize() {
		Robot.limitSwitchesDisabled = !Robot.limitSwitchesDisabled;
	}
}