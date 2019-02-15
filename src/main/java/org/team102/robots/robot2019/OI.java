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

package org.team102.robots.robot2019;

import org.team102.robots.robot2019.lib.CustomOperatorConsole;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class OI {
	
	public Joystick driverJoystick;
	public CustomOperatorConsole opConsole;
	
	public OI() {
		driverJoystick = new Joystick(RobotMap.JOYSTICK_ID_DRIVER);
		opConsole = new CustomOperatorConsole(RobotMap.JOYSTICK_ID_OPERATOR);
		
		Robot.driverNotif.initOIPortions();
	}
	
	public double getTimeRemaining() {
		return Math.max(0, DriverStation.getInstance().getMatchTime());
	}
}