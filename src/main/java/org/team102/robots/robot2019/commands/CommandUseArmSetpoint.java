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
import org.team102.robots.robot2019.subsystems.SubsystemArm.ArmSetpoint;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.PrintCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class CommandUseArmSetpoint extends CommandGroup {
	
	public CommandUseArmSetpoint(ArmSetpoint setpoint) {
		this(setpoint, null);
	}
	
	public CommandUseArmSetpoint(ArmSetpoint setpoint, int[] controllerLightPatterns) {
		super("Use Arm Setpoint: " + (setpoint == null ? "(no setpoint given)" : setpoint.name));
		
		if(controllerLightPatterns != null) { // If we have light patterns to use, use the first one for "in progress".
			addSequential(new CommandSetOpConsoleLightPattern(Robot.oi.opConsole, controllerLightPatterns[0]));
		}
		
		if(setpoint == null) {
			addSequential(new PrintCommand("Warning: The setpoint given has not yet been assigned!"));
		} else {
			if(!setpoint.isExtended) { // If it isn't supposed to be extended, contract it first, so it doesn't hit anything.
				addSequential(new CommandExtendArm(false));
			}
			
			// Then move the elbow and wrist to the correct distances
			addSequential(new CommandMoveArmAutomatic(setpoint));
			
			if(setpoint.isExtended) { // Extend it if we're supposed to.
				addSequential(new CommandExtendArm(true));
			}
		}
		
		if(controllerLightPatterns != null) { // If we have light patterns to use, use the second one for "done", and also assume that we're doing this from the a button (not automatically), so rumble that we're done, too
			addSequential(new CommandSetOpConsoleLightPattern(Robot.oi.opConsole, controllerLightPatterns[1]));
			addSequential(new CommandPlayRumble(Robot.oi.driverJoystick, RobotMap.RUMBLE_ARM_AT_SETPOINT, false));
			
			addSequential(new WaitCommand(2));
			addSequential(new CommandSetOpConsoleLightPattern(Robot.oi.opConsole, RobotMap.OP_CONTROLLER_PATTERN_OFF));
		}
	}
	
	@Override
	protected void interrupted() {
		Robot.oi.setOpConsoleIdlePattern();
	}
}