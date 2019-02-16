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

import org.team102.robots.robot2019.commands.CommandMoveArmAutomatic;
import org.team102.robots.robot2019.commands.CommandMoveArmManual;
import org.team102.robots.robot2019.lib.CustomOperatorConsole;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class OI {
	
	public Joystick driverJoystick;
	public CustomOperatorConsole opConsole;
	
	public OI() {
		driverJoystick = new Joystick(RobotMap.JOYSTICK_ID_DRIVER);
		opConsole = new CustomOperatorConsole(RobotMap.JOYSTICK_ID_OPERATOR);
		
		/* TODO add this all in when we have the actual setpoints
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_TOP).whenPressed(new CommandMoveArmAutomatic(RobotMap.ARM_SETPOINT_ROCKET_HATCH_TOP));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_MIDDLE).whenPressed(new CommandMoveArmAutomatic(RobotMap.ARM_SETPOINT_ROCKET_HATCH_MIDDLE));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_BOTTOM).whenPressed(new CommandMoveArmAutomatic(RobotMap.ARM_SETPOINT_ROCKET_HATCH_BOTTOM));
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_TOP).whenPressed(new CommandMoveArmAutomatic(RobotMap.ARM_SETPOINT_ROCKET_CARGO_TOP));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_MIDDLE).whenPressed(new CommandMoveArmAutomatic(RobotMap.ARM_SETPOINT_ROCKET_CARGO_MIDDLE));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_BOTTOM).whenPressed(new CommandMoveArmAutomatic(RobotMap.ARM_SETPOINT_ROCKET_CARGO_BOTTOM));
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_CARGO_SHIP_CARGO).whenPressed(new CommandMoveArmAutomatic(RobotMap.ARM_SETPOINT_CARGO_SHIP_CARGO));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_CARGO_SHIP_HATCH).whenPressed(new CommandMoveArmAutomatic(RobotMap.ARM_SETPOINT_CARGO_SHIP_HATCH));
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_FLOOR_HATCH_DOWN).whenPressed(new CommandMoveArmAutomatic(RobotMap.ARM_SETPOINT_FLOOR_HATCH_DOWN));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_FLOOR_HATCH_UP).whenPressed(new CommandMoveArmAutomatic(RobotMap.ARM_SETPOINT_FLOOR_HATCH_UP));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_FLOOR_CARGO).whenPressed(new CommandMoveArmAutomatic(RobotMap.ARM_SETPOINT_FLOOR_CARGO));
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_LOADING_STATION_HATCH).whenPressed(new CommandMoveArmAutomatic(RobotMap.ARM_SETPOINT_LOADING_STATION_HATCH));
		*/
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_DOWN).whileHeld(new CommandMoveArmManual(false, true));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_UP).whileHeld(new CommandMoveArmManual(false, false));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_DOWN).whileHeld(new CommandMoveArmManual(true, true));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_UP).whileHeld(new CommandMoveArmManual(true, false));
		
		Robot.driverNotif.initOIPortions();
	}
	
	public double getTimeRemaining() {
		return Math.max(0, DriverStation.getInstance().getMatchTime());
	}
}