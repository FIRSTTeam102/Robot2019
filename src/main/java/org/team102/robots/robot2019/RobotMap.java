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

import org.team102.robots.robot2019.lib.RumbleSeries;
import org.team102.robots.robot2019.subsystems.SubsystemArm.ArmSetpoint;

public class RobotMap {
	
	public static final int ARM_WRIST_LOWER_MAXIMUM_RECORDABLE_DISTANCE = 20; // TODO configure this
	public static final int ARM_WRIST_UPPER_MAXIMUM_RECORDABLE_DISTANCE = 20; // TODO configure this
	
	public static final double DRIVE_TRAIN_AUTONOMOUS_FORWARD_SPEED = .1; // TODO configure this
	
	public static final double CLIMBER_EXTENSION_TIME = 10; // TODO configure this
	public static final double CLIMBER_WAIT_BEFORE_DRIVE_TIME = 5; // TODO configure this
	public static final double CLIMBER_DRIVE_FORWARD_TIME = 5; // TODO configure this
	
	public static final ArmSetpoint ARM_SETPOINT_ROCKET_HATCH_TOP = null;  // TODO configure this
	public static final ArmSetpoint ARM_SETPOINT_ROCKET_HATCH_MIDDLE = null; // TODO configure this
	public static final ArmSetpoint ARM_SETPOINT_ROCKET_HATCH_BOTTOM = null; // TODO configure this
	public static final ArmSetpoint ARM_SETPOINT_ROCKET_CARGO_TOP = null; // TODO configure this
	public static final ArmSetpoint ARM_SETPOINT_ROCKET_CARGO_MIDDLE = null; // TODO configure this
	public static final ArmSetpoint ARM_SETPOINT_ROCKET_CARGO_BOTTOM = null; // TODO configure this
	
	public static final ArmSetpoint ARM_SETPOINT_CARGO_SHIP_CARGO = null; // TODO configure this
	public static final ArmSetpoint ARM_SETPOINT_CARGO_SHIP_OR_LOADING_STATION_HATCH = null; // TODO configure this
	
	public static final ArmSetpoint ARM_SETPOINT_FLOOR_HATCH_ABOVE_POSITION = null; // TODO configure this
	public static final ArmSetpoint ARM_SETPOINT_FLOOR_HATCH_COMMIT = null; // TODO configure this
	public static final ArmSetpoint ARM_SETPOINT_FLOOR_CARGO = null; // TODO configure this
	
	public static final RumbleSeries RUMBLE_ARM_AT_SETPOINT = new RumbleSeries().addSoft(.5);
	public static final RumbleSeries RUMBLE_LOW_TIME = new RumbleSeries().addRough(.2).addBreak(.1).addRough(.2);
	public static final RumbleSeries RUMBLE_NOT_ENOUGH_TIME = new RumbleSeries().addRough(.5);
	public static final RumbleSeries RUMBLE_PROGRESS = new RumbleSeries().addSoft(.2);
	
	public static final int OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_TOP = 1;
	public static final int OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_MIDDLE = 2;
	public static final int OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_BOTTOM = 3;
	public static final int OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_TOP = 4;
	public static final int OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_MIDDLE = 5;
	public static final int OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_BOTTOM = 6;
	
	public static final int OP_CONTROLLER_BUTTON_ID_CARGO_SHIP_CARGO = 9;
	public static final int OP_CONTROLLER_BUTTON_ID_CARGO_SHIP_OR_LOADING_STATION_HATCH = 12;
	
	public static final int OP_CONTROLLER_BUTTON_ID_FLOOR_HATCH_ABOVE_POSITION = 13;
	public static final int OP_CONTROLLER_BUTTON_ID_FLOOR_HATCH_COMMIT = 14;
	public static final int OP_CONTROLLER_BUTTON_ID_FLOOR_CARGO = 15;
	
	public static final int OP_CONTROLLER_BUTTON_ID_UNUSED = 16;
	
	public static final int OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_DOWN = 17;
	public static final int OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_UP = 18;
	public static final int OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_DOWN = 19;
	public static final int OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_UP = 20;
	
	public static final int OP_CONTROLLER_PATTERN_OFF = 0;
	public static final int OP_CONTROLLER_PATTERN_SCROLLING_ORANGE = 1;
	
	public static final int OP_CONTROLLER_PATTERN_COUNTDOWN_2_ON = 2;
	public static final int OP_CONTROLLER_PATTERN_COUNTDOWN_4_ON = 3;
	public static final int OP_CONTROLLER_PATTERN_COUNTDOWN_6_ON = 4;
	public static final int OP_CONTROLLER_PATTERN_COUNTDOWN_8_ON = 5;
	
	public static final int OP_CONTROLLER_PATTERN_FLASH_RED = 6;
	public static final int OP_CONTROLLER_PATTERN_FLASH_GREEN = 8;
	public static final int OP_CONTROLLER_PATTERN_FLASH_BLUE = 10;
	public static final int OP_CONTROLLER_PATTERN_FLASH_YELLOW = 12;
	public static final int OP_CONTROLLER_PATTERN_FLASH_PURPLE = 14;
	
	public static final int OP_CONTROLLER_PATTERN_SOLID_RED = 7;
	public static final int OP_CONTROLLER_PATTERN_SOLID_GREEN = 9;
	public static final int OP_CONTROLLER_PATTERN_SOLID_BLUE = 11;
	public static final int OP_CONTROLLER_PATTERN_SOLID_YELLOW = 13;
	public static final int OP_CONTROLLER_PATTERN_SOLID_PURPLE = 15;
	
	public static final int[] OP_CONTROLLER_PATTERN_SET_COUNTDOWN = { OP_CONTROLLER_PATTERN_COUNTDOWN_2_ON, OP_CONTROLLER_PATTERN_COUNTDOWN_4_ON, OP_CONTROLLER_PATTERN_COUNTDOWN_6_ON, OP_CONTROLLER_PATTERN_COUNTDOWN_8_ON };
	
	public static final int[] OP_CONTROLLER_PATTERN_SET_RED_BUTTON = { OP_CONTROLLER_PATTERN_FLASH_RED, OP_CONTROLLER_PATTERN_SOLID_RED };
	public static final int[] OP_CONTROLLER_PATTERN_SET_GREEN_BUTTON = { OP_CONTROLLER_PATTERN_FLASH_GREEN, OP_CONTROLLER_PATTERN_SOLID_GREEN };
	public static final int[] OP_CONTROLLER_PATTERN_SET_BLUE_BUTTON = { OP_CONTROLLER_PATTERN_FLASH_BLUE, OP_CONTROLLER_PATTERN_SOLID_BLUE };
	public static final int[] OP_CONTROLLER_PATTERN_SET_YELLOW_BUTTON = { OP_CONTROLLER_PATTERN_FLASH_YELLOW, OP_CONTROLLER_PATTERN_SOLID_YELLOW };
	public static final int[] OP_CONTROLLER_PATTERN_SET_PURPLE_BUTTON = { OP_CONTROLLER_PATTERN_FLASH_PURPLE, OP_CONTROLLER_PATTERN_SOLID_PURPLE };
	
	public static final double JOYSTICK_MIN_AXIS_PRESS_TO_ACTIVATE_TRIGGER = .25; // TODO configure this
	public static final double JOYSTICK_TIMED_TRIGGER_CONFIRM_TIME = 2; // TODO configure this
	
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
	public static final int CAMERA_ID_ARM = CAMERA_ID_VISION + 1;
	public static final String CAMERA_URL_VISION_PI_OUTPUT = "http://10.1.2.4:1181";
	
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