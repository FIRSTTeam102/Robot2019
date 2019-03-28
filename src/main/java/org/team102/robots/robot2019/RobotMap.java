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

public class RobotMap {
	
	// LEAVE THESE AT THE TOP!
	public static final boolean IS_TESTBED = false;
	
	public static final boolean ARM_REVERSE_ELBOW = true;
	public static final boolean ARM_REVERSE_WRIST = false;
	public static final boolean ARM_ENABLE_DAMPENING = true;
	
	public static final boolean ARM_USE_VICTOR_FOR_WRIST = true;
	
	public static final double CLIMB_MAX_TIME_REMAINING = 35;
	
	public static final double AESTHETICS_PATTERN_CHANGE_TIME = 3; // TODO configure me
	
	public static final int LONG_LIGHT_STRIP_PATTERN_TWINKLE_WHITE = 4;
	public static final int LONG_LIGHT_STRIP_PATTERN_TWINKLE_ORANGE = 12;
	public static final int LONG_LIGHT_STRIP_PATTERN_TWINKLE_RGBW = 11;
	
	public static final int LONG_LIGHT_STRIP_PATTERN_RED_ORANGE_YELLOW_GREEN_BLUE_PURPLE_SEGMENTED_SNAKE = 3;
	public static final int LONG_LIGHT_STRIP_PATTERN_GREEN_RED_BLUE_SNAKE = 1;
	public static final int LONG_LIGHT_STRIP_PATTERN_RANDOM_COLORS_SNAKE = 2;
	
	public static final int LONG_LIGHT_STRIP_PATTERN_TWO_RANDOM_COLORED_SNAKES = 10;
	public static final int LONG_LIGHT_STRIP_PATTERN_RED_YELLOW_GREEN_BLUE_SNAKES = 13;
	public static final int LONG_LIGHT_STRIP_PATTERN_ORANGE_PINK_PURPLE_WHITE_SNAKES = 14;
	public static final int LONG_LIGHT_STRIP_PATTERN_RED_GREEN_BLUE_SNAKES = 15;
	
	public static final int LONG_LIGHT_STRIP_PATTERN_RED_SNAKE_AND_BLUE_SNAKE = 7;
	public static final int LONG_LIGHT_STRIP_PATTERN_YELLOW_SNAKE_AND_GREEN_SNAKE = 8;
	public static final int LONG_LIGHT_STRIP_PATTERN_ORANGE_SNAKE_AND_PURPLE_SNAKE = 9;
	
	public static final int LONG_LIGHT_STRIP_PATTERN_RED_SNAKE = 5;
	public static final int LONG_LIGHT_STRIP_PATTERN_BLUE_SNAKE = 6;
	
	public static final int LONG_LIGHT_STRIP_PATTERN_ANY_COLOR_RANDOM_PIXELS = 16;
	public static final int LONG_LIGHT_STRIP_PATTERN_DEFAULT_COLOR_RANDOM_PIXELS = 17;
	
	public static final int LONG_LIGHT_STRIP_PATTERN_RED_FLAMES = 18;
	public static final int LONG_LIGHT_STRIP_PATTERN_GREEN_FLAMES = 19;
	public static final int LONG_LIGHT_STRIP_PATTERN_BLUE_FLAMES = 20;
	public static final int LONG_LIGHT_STRIP_PATTERN_PURPLE_FLAMES = 21;
	public static final int LONG_LIGHT_STRIP_PATTERN_ORANGE_FLAMES = 22;
	
	public static final int LONG_LIGHT_STRIP_PATTERN_WHITE_COMET = 23;
	public static final int LONG_LIGHT_STRIP_PATTERN_SLOW_COLOR_CHANGING  = 24;
	public static final int LONG_LIGHT_STRIP_PATTERN_RED_GREEN_PURPLE_BOUNCING_BALLS = 25;
	
	public static final int[] LONG_LIGHT_STRIP_PATTERN_SET_DISABLED = { LONG_LIGHT_STRIP_PATTERN_ORANGE_FLAMES, LONG_LIGHT_STRIP_PATTERN_TWINKLE_ORANGE, LONG_LIGHT_STRIP_PATTERN_RED_SNAKE_AND_BLUE_SNAKE, LONG_LIGHT_STRIP_PATTERN_WHITE_COMET };
	public static final int[] LONG_LIGHT_STRIP_PATTERN_SET_BLUE_ALLIANCE = { LONG_LIGHT_STRIP_PATTERN_BLUE_FLAMES, LONG_LIGHT_STRIP_PATTERN_BLUE_SNAKE };
	public static final int[] LONG_LIGHT_STRIP_PATTERN_SET_RED_ALLIANCE = { LONG_LIGHT_STRIP_PATTERN_RED_FLAMES, LONG_LIGHT_STRIP_PATTERN_RED_SNAKE }; // TODO use more patterns (or make more pattern sets)
	
	public static final int DIO_ID_ARM_ELBOW_LIMIT_SWITCH = 0;
	public static final boolean DIO_CONFIG_ARM_ELBOW_LIMIT_SWITCH_IS_ACTIVE_LOW = true;
	
	public static final boolean DEBUG_USE_LOCAL_PIPELINE = false;
	
	public static final double DRIVE_TRAIN_AUTONOMOUS_FORWARD_SPEED = .4; // TODO configure this
	
	public static final double CLIMBER_EXTENSION_TIME = 15; // TODO configure this
	public static final double CLIMBER_WAIT_BEFORE_DRIVE_TIME = 3; // TODO configure this
	public static final double CLIMBER_DRIVE_FORWARD_TIME = 10; // TODO configure this
	
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
	
	public static final int OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_UP = 17;
	public static final int OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_DOWN = 18;
	public static final int OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_UP = 19;
	public static final int OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_DOWN = 20;
	
	public static final int OP_CONTROLLER_PATTERN_OFF = 0;
	public static final int OP_CONTROLLER_PATTERN_SCROLLING_ORANGE = 1;
	
	public static final int OP_CONTROLLER_PATTERN_COUNTDOWN_2_ON = 2;
	public static final int OP_CONTROLLER_PATTERN_COUNTDOWN_4_ON = 3;
	public static final int OP_CONTROLLER_PATTERN_COUNTDOWN_6_ON = 4;
	public static final int OP_CONTROLLER_PATTERN_COUNTDOWN_8_ON = 5;
	
	public static final int OP_CONTROLLER_PATTERN_SOLID_RED = 6;
	public static final int OP_CONTROLLER_PATTERN_SOLID_GREEN = 8;
	public static final int OP_CONTROLLER_PATTERN_SOLID_BLUE = 10;
	public static final int OP_CONTROLLER_PATTERN_SOLID_YELLOW = 12;
	public static final int OP_CONTROLLER_PATTERN_SOLID_PURPLE = 14;
	
	public static final int OP_CONTROLLER_PATTERN_FLASH_RED = 7;
	public static final int OP_CONTROLLER_PATTERN_FLASH_GREEN = 9;
	public static final int OP_CONTROLLER_PATTERN_FLASH_BLUE = 11;
	public static final int OP_CONTROLLER_PATTERN_FLASH_YELLOW = 13;
	public static final int OP_CONTROLLER_PATTERN_FLASH_PURPLE = 15;
	
	public static final int[] OP_CONTROLLER_PATTERN_SET_COUNTDOWN = { OP_CONTROLLER_PATTERN_COUNTDOWN_2_ON, OP_CONTROLLER_PATTERN_COUNTDOWN_4_ON, OP_CONTROLLER_PATTERN_COUNTDOWN_6_ON, OP_CONTROLLER_PATTERN_COUNTDOWN_8_ON };
	
	public static final int[] OP_CONTROLLER_PATTERN_SET_RED_BUTTON = { OP_CONTROLLER_PATTERN_FLASH_RED, OP_CONTROLLER_PATTERN_SOLID_RED };
	public static final int[] OP_CONTROLLER_PATTERN_SET_GREEN_BUTTON = { OP_CONTROLLER_PATTERN_FLASH_GREEN, OP_CONTROLLER_PATTERN_SOLID_GREEN };
	public static final int[] OP_CONTROLLER_PATTERN_SET_BLUE_BUTTON = { OP_CONTROLLER_PATTERN_FLASH_BLUE, OP_CONTROLLER_PATTERN_SOLID_BLUE };
	public static final int[] OP_CONTROLLER_PATTERN_SET_YELLOW_BUTTON = { OP_CONTROLLER_PATTERN_FLASH_YELLOW, OP_CONTROLLER_PATTERN_SOLID_YELLOW };
	public static final int[] OP_CONTROLLER_PATTERN_SET_PURPLE_BUTTON = { OP_CONTROLLER_PATTERN_FLASH_PURPLE, OP_CONTROLLER_PATTERN_SOLID_PURPLE };
	
	public static final double JOYSTICK_MIN_AXIS_PRESS_TO_ACTIVATE_TRIGGER = .25;
	public static final double JOYSTICK_TIMED_TRIGGER_CONFIRM_TIME = 2;
	public static final double JOYSTICK_TIMED_TRIGGER_LONG_CONFIRM_TIME = 5;
	
	public static final double LOW_TIME = 30;
	
	public static final double CENTERING_ALLOWABLE_ERROR = .01; // TODO configure this
	public static final double CENTERING_SPEED_FRONT_OR_BACK = .1; // TODO configure this
	public static final double CENTERING_SPEED_LEFT_OR_RIGHT = .1; // TODO configure this
	public static final double CENTERING_SPEED_ROTATION = .1; // TODO configure this
	
	public static final String ARM_ARDUINO_WHOIS_RESPONSE = "arm-sensors";
	public static final String CENTERING_ARDUINO_WHOIS_RESPONSE = "ls-r2";
	public static final String LONG_LIGHT_STRIP_ARDUINO_WHOIS_RESPONSE = "led-r2";
	
	public static final double CARGO_MANIP_ROLLER_SPEED = 1;
	public static final double ARM_ELBOW_SPEED = .6;
	public static final double ARM_ELBOW_DOWN_SPEED = -.2;
	public static final double ARM_WRIST_SPEED = .8;
	public static final double ARM_WRIST_DOWN_SPEED = -.4;
	public static final double ARM_ELBOW_GRAV_COMP_SPEED = .1;
	
	public static final int JOYSTICK_ID_DRIVER = 0;
	public static final int JOYSTICK_ID_OPERATOR = 1;
	
	public static final int CAMERA_ID_VISION_REAL = 0;
	public static final int CAMERA_ID_VISION_IN_SIMULATOR = 1;
	public static final int CAMERA_ID_VISION = Robot.isReal() ? CAMERA_ID_VISION_REAL : CAMERA_ID_VISION_IN_SIMULATOR;
	
	public static final int CAMERA_ID_ARM_REAL = 1;
	public static final int CAMERA_ID_ARM_IN_SIMULATOR = 2;
	public static final int CAMERA_ID_ARM = Robot.isReal() ? CAMERA_ID_ARM_REAL : CAMERA_ID_ARM_IN_SIMULATOR;
	
	public static final String CAMERA_URL_VISION_PI_OUTPUT = "http://10.1.2.4:1181/?action=stream";
	
	public static final int CAN_ID_PDP = 0;
	
	public static final int PDP_ID_ARM_WRIST_ON_TESTBED = 10;
	public static final int PDP_ID_ARM_WRIST_REAL = 4;
	public static final int PDP_ID_ARM_WRIST = IS_TESTBED ? PDP_ID_ARM_WRIST_ON_TESTBED : PDP_ID_ARM_WRIST_REAL;
	public static final double PDP_MAX_CURRENT_ARM_WRIST = -1; // TODO configure me
	
	public static final int PWM_ID_ARM_WRIST_USING_VICTOR = 0;
	
	public static final int CAN_TALON_DRIVE_TRAIN_FRONT_LEFT = 2;
	public static final int CAN_TALON_DRIVE_TRAIN_FRONT_RIGHT = 1;
	public static final int CAN_TALON_DRIVE_TRAIN_REAR_LEFT = 4;
	public static final int CAN_TALON_DRIVE_TRAIN_REAR_RIGHT = 3;
	
	public static final int CAN_TALON_ARM_ELBOW_ON_TESTBED = 0;
	public static final int CAN_TALON_ARM_ELBOW_REAL= 5;
	public static final int CAN_TALON_ARM_ELBOW = IS_TESTBED ? CAN_TALON_ARM_ELBOW_ON_TESTBED : CAN_TALON_ARM_ELBOW_REAL;
	
	public static final int CAN_TALON_ARM_WRIST_ON_TESTBED = 1;
	public static final int CAN_TALON_ARM_WRIST_REAL = 6;
	public static final int CAN_TALON_ARM_WRIST = IS_TESTBED ? CAN_TALON_ARM_WRIST_ON_TESTBED : CAN_TALON_ARM_WRIST_REAL;
	
	public static final int CAN_TALON_CARGO_MANIP_ROLLER_ON_TESTBED = 2;
	public static final int CAN_TALON_CARGO_MANIP_ROLLER_REAL = 7;
	public static final int CAN_TALON_CARGO_MANIP_ROLLER = IS_TESTBED ? CAN_TALON_CARGO_MANIP_ROLLER_ON_TESTBED : CAN_TALON_CARGO_MANIP_ROLLER_REAL;
	
	public static final int SOLENOID_ARM_EXTENDER = 0;
	public static final int SOLENOID_HATCH_MANIP_EJECTOR = 1;
	public static final int SOLENOID_CLIMBER = 2;
	public static final int SOLENOID_CLIMBER_FRONT_STAGE = 3;
}