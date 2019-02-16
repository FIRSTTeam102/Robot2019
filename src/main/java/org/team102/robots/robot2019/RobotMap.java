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

public class RobotMap {
	
	public static final int OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_TOP = 1;
	public static final int OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_MIDDLE = 2;
	public static final int OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_BOTTOM = 3;
	public static final int OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_TOP = 4;
	public static final int OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_MIDDLE = 5;
	public static final int OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_BOTTOM = 6;
	
	public static final int OP_CONTROLLER_BUTTON_ID_CARGO_SHIP_CARGO = 9;
	public static final int OP_CONTROLLER_BUTTON_ID_CARGO_SHIP_HATCH = 12;
	
	public static final int OP_CONTROLLER_BUTTON_ID_FLOOR_HATCH_DOWN = 13;
	public static final int OP_CONTROLLER_BUTTON_ID_FLOOR_HATCH_UP = 14;
	public static final int OP_CONTROLLER_BUTTON_ID_FLOOR_CARGO = 16;
	
	public static final int OP_CONTROLLER_BUTTON_ID_LOADING_STATION_HATCH = 15;
	
	public static final int OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_DOWN = 17;
	public static final int OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_UP = 18;
	public static final int OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_DOWN = 19;
	public static final int OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_UP = 20;
	
	public static final double LOW_TIME = 30;
	
	public static final double CENTERING_ALLOWABLE_ERROR = .01; // TODO configure this
	public static final double CENTERING_SPEED_FRONT_OR_BACK = .1; // TODO configure this
	public static final double CENTERING_SPEED_LEFT_OR_RIGHT = .1; // TODO configure this
	public static final double CENTERING_SPEED_ROTATION = .1; // TODO configure this
	
	public static final double CAMERA_LIGHT_BRIGHTNESS = 1; // TODO configure this
	
	public static final String ARM_ARDUINO_WHOIS_RESPONSE = "arm-sensors";
	public static final String CENTERING_ARDUINO_WHOIS_RESPONSE = "ls-r2";
	public static final String LIGHTS_ARDUINO_WHOIS_RESPONSE = "lcl-r1";
	public static final String LONG_LIGHT_STRIP_ARDUINO_WHOIS_RESPONSE = "led-r2";
	
	public static final double CARGO_MANIP_ROLLER_SPEED = .5; // TODO configure this
	public static final double ARM_ELBOW_SPEED = .5; // TODO configure this
	public static final double ARM_WRIST_SPEED = .5; // TODO configure this
	
	public static final int ARM_ELBOW_ACCEPTABLE_RANGE_OF_ERROR = 1; // TODO configure this
	public static final int ARM_WRIST_ACCEPTABLE_RANGE_OF_ERROR = 1; // TODO configure this
	
	public static final int JOYSTICK_ID_DRIVER = 0;
	public static final int JOYSTICK_ID_OPERATOR = 1;
	
	public static final int CAMERA_ID_VISION_REAL = 0;
	public static final int CAMERA_ID_VISION_IN_SIMULATOR = 1;
	public static final int CAMERA_ID_VISION = Robot.isReal() ? CAMERA_ID_VISION_REAL : CAMERA_ID_VISION_IN_SIMULATOR;
	
	public static final int CAN_TALON_DRIVE_TRAIN_FRONT_LEFT = 2;
	public static final int CAN_TALON_DRIVE_TRAIN_FRONT_RIGHT = 1;
	public static final int CAN_TALON_DRIVE_TRAIN_REAR_LEFT = 4;
	public static final int CAN_TALON_DRIVE_TRAIN_REAR_RIGHT = 3;
	
	public static final int CAN_TALON_ARM_ELBOW = 5;
	public static final int CAN_TALON_ARM_WRIST = 6;
	
	public static final int CAN_TALON_CARGO_MANIP_ROLLER = 7;
	
	public static final int SOLENOID_ARM_EXTENDER = 0; // TODO configure this
	
	public static final int SOLENOID_HATCH_MANIP_EJECTOR_1 = 1; // TODO configure this
	public static final int SOLENOID_HATCH_MANIP_EJECTOR_2 = 2; // TODO configure this
	
	public static final int SOLENOID_CLIMBER = 3; // TODO configure this
}