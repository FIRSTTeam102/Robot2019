/*
 * The code for FRC Team #102's robot for the 2019 game, Destination: Deep Space
 * Copyright (C) 2019  Robotics Fund Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * 
 * Contact us at: firstteam102@gmail.com
 */

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