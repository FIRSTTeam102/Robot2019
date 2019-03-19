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

import edu.wpi.first.wpilibj.command.InstantCommand;

public class CommandExtendArm extends InstantCommand {
	
	private ArmExtendStatus status;
	
	public CommandExtendArm(boolean extended) {
		this(extended ? ArmExtendStatus.EXTEND : ArmExtendStatus.CONTRACT);
	}
	
	public CommandExtendArm(ArmExtendStatus status) {
		super("Set Arm Extension: " + status);
		requires(Robot.arm);
		
		this.status = status;
	}
	
	@Override
	protected void initialize() {
		boolean active;
		
		switch(status) {
			case CONTRACT: {
				active = false;
				break;
			}
			
			case EXTEND: {
				active = true;
				break;
			}
			
			default: {
				active = !Robot.arm.getExtender();
				break;
			}
		}
		
		Robot.arm.setExtender(active);
	}
	
	public enum ArmExtendStatus {
		CONTRACT, EXTEND, TOGGLE
	}
}