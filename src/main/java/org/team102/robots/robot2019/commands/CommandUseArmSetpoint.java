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

import org.team102.robots.robot2019.ArmStatus;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CommandUseArmSetpoint extends CommandGroup {
	
	public CommandUseArmSetpoint(ArmStatus setpoint) {
		super("Use Arm Setpoint: " + setpoint.name);
		
		if(!setpoint.isExtended) { // If the arm isn't supposed to be extended, contract it first, so it doesn't hit anything.
			addSequential(new CommandExtendArm(false));
		}
		
		if(!setpoint.isHatchEjectorExtended) { // It the hatch ejector isn't supposed to be extended, contract it first, so it doesn't mess anything up or hit anything
			addSequential(new CommandSetHatchManip(false));
		}
		
		// Then move the elbow and wrist to the correct distances
		addSequential(new CommandMoveArmAutomatic(setpoint));
		
		if(setpoint.isExtended) { // Extend the arm if we're supposed to.
			addSequential(new CommandExtendArm(true));
		}
		
		if(setpoint.isHatchEjectorExtended) { // Extend the hatch ejector if we're supposed to
			addSequential(new CommandSetHatchManip(true));
		}
		
		if(setpoint.cargoRollerActiveTime > 0) { // Set the cargo roller at the end, either forward or reverse, as applicable
			addSequential(new CommandSetCargoManip(setpoint.isCargoRollerReverse, setpoint.cargoRollerActiveTime));
		}
	}
}