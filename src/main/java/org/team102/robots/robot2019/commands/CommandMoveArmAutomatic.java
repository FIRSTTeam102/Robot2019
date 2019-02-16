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
import org.team102.robots.robot2019.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CommandMoveArmAutomatic extends Command {
	
	private ArmStatus setpoint;
	
	public CommandMoveArmAutomatic(ArmStatus setpoint) {
		super("Move arm to: " + setpoint);
		requires(Robot.arm);
		
		this.setpoint = setpoint;
	}
	
	@Override
	public void initialize() {
		Robot.arm.setSetpoint(setpoint);
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.arm.isWithinMarginOfErrorOfSetpoints();
	}
}