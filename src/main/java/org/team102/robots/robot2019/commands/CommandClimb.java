package org.team102.robots.robot2019.commands;

import org.team102.robots.robot2019.Robot;
import org.team102.robots.robot2019.RobotMap;
import org.team102.robots.robot2019.lib.commandsAndTrigger.CommandPlayRumble;
import org.team102.robots.robot2019.lib.commandsAndTrigger.CommandSetOpConsoleLightPattern;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.PrintCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class CommandClimb extends ConditionalCommand {
	
	public CommandClimb() {
		super("Climb",
			new CommandClimbUnconditionally(),
			new CommandOnTriedToClimbWithoutEnoughTime()
		);
	}

	protected boolean condition() {
		return Robot.climber.hasEnoughTimeToClimb();
	}
	
	public static class CommandOnTriedToClimbWithoutEnoughTime extends CommandGroup {
		
		public CommandOnTriedToClimbWithoutEnoughTime() {
			super("Tried to climb without enough time remaining");
			
			addSequential(new CommandPlayRumble(Robot.oi.driverJoystick, RobotMap.RUMBLE_NOT_ENOUGH_TIME, false));
			addSequential(new CommandSetOpConsoleLightPattern(Robot.oi.opConsole, RobotMap.OP_CONTROLLER_PATTERN_SOLID_RED));
			addSequential(new PrintCommand("Warning: You tried to climb with not enough time remaining!"));
		}
	}
	
	public static class CommandClimbUnconditionally extends CommandGroup {
		
		public CommandClimbUnconditionally() {
			super("Climb Unconditionally");
			
			addSequential(new CommandSetClimber(true));
			addSequential(new WaitCommand(RobotMap.CLIMBER_WAIT_BEFORE_DRIVE_TIME));
			addSequential(new CommandDriveForwardTimed(RobotMap.CLIMBER_DRIVE_FORWARD_TIME));
		}
	}
}