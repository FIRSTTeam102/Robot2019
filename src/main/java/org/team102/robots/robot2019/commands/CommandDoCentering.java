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
import org.team102.robots.robot2019.subsystems.SubsystemCentering.MoveDirection;

import edu.wpi.first.wpilibj.command.Command;

public class CommandDoCentering extends Command {
	
	private MoveDirection lastMoveDir;
	
	public CommandDoCentering() {
		super("Autonomous Centering");
		
		requires(Robot.centering);
		requires(Robot.driveTrain);
	}
	
	@Override
	public void execute() {
		lastMoveDir = Robot.centering.getMoveDirection();
		
		double frontBack = 0;
		double leftRight = 0;
		double rotation = 0;
		
		switch(lastMoveDir) {
			case FRONT: {
				frontBack = RobotMap.CENTERING_SPEED_FRONT_OR_BACK;
				break;
			}
			
			case BACK: {
				frontBack = -RobotMap.CENTERING_SPEED_FRONT_OR_BACK;
				break;
			}
			
			case RIGHT: {
				leftRight = RobotMap.CENTERING_SPEED_LEFT_OR_RIGHT;
				break;
			}
			
			case LEFT: {
				leftRight = -RobotMap.CENTERING_SPEED_LEFT_OR_RIGHT;
				break;
			}
			
			case TURN_CW: {
				rotation = RobotMap.CENTERING_SPEED_ROTATION;
				break;
			}
			
			case TURN_CCW: {
				rotation = -RobotMap.CENTERING_SPEED_ROTATION;
				break;
			}
			
			default:
		}
		
		Robot.driveTrain.drive(frontBack, leftRight, rotation);
	}
	
	@Override
	protected boolean isFinished() {
		return lastMoveDir == MoveDirection.NONE;
	}
}