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

package org.team102.robots.robot2019.lib;

/** An enclosure for commonly-used ID numbers */
public final class CommonIDs {
	
	/** Button and Axis IDs for an XBOX360 controller-compatible gamepad */
	public static final class Gamepad {
		/** Left X Axis */	public static final int AXIS_LEFT_X = 0;
		/** Left Y Axis */	public static final int AXIS_LEFT_Y = 1;
		/** Right X Axis */	public static final int AXIS_RIGHT_X = 4;
		/** Right Y Axis */	public static final int AXIS_RIGHT_Y = 5;
		/** LT Axis */		public static final int AXIS_LEFT_TRIGGER = 2;
		/** RT Axis*/		public static final int AXIS_RIGHT_TRIGGER = 3;
		
		/** A Button */			public static final int BTN_A = 1;
		/** B Button */			public static final int BTN_B = 2;
		/** X Button */			public static final int BTN_X = 3;
		/** Y Button */			public static final int BTN_Y = 4;
		/** Start Button */		public static final int BTN_START = 8;
		/** Back Button */		public static final int BTN_BACK = 7;
		/** LB Button */		public static final int BTN_LEFT_BUMPER = 5;
		/** RB Button */		public static final int BTN_RIGHT_BUMPER = 6;
		/** Left JS Button */	public static final int BTN_LEFT_STICK = 9;
		/** Right JS Button*/	public static final int BTN_RIGHT_STICK = 10;
	}
	
	/** Button and Axis IDs for a standard Joystick */
	public static final class Joystick {
		/** X Axis */			public static final int AXIS_X = 0;
		/** Y Axis */			public static final int AXIS_Y = 1;
		/** Z Axis */			public static final int AXIS_Z = 2;
		/** Twist Axis */		public static final int AXIS_TWIST = 2;
		/** Throttle Axis */	public static final int AXIS_THROTTLE = 3;
		
		/** Trigger Button */	public static final int BTN_TRIGGER = 1;
		/** Top Button */		public static final int BTN_TOP = 2;
	}
	
	/** Positions for a POV/Hat/DPad switch */
	public static final class POVSwitch {
		/** POV is unpressed */		public static final int NOT_PRESSED = -1;
		/** POV is center-pressed*/	public static final int CENTER = 0;
		
		/** 0 degrees */	public static final int UP_CENTER = 1;
		/** 45 degrees */	public static final int UP_RIGHT = 2;
		/** 90 degrees */	public static final int CENTER_RIGHT = 3;
		/** 135 degrees */	public static final int DOWN_RIGHT = 4;
		/** 180 degrees */	public static final int DOWN_CENTER = 5;
		/** 225 degrees */	public static final int DOWN_LEFT = 6;
		/** 270 degrees */	public static final int CENTER_LEFT = 7;
		/** 315 degrees */	public static final int UP_LEFT = 8;
		
		/**
		 * Is this a valid POV ID?
		 * @param id The ID to check
		 * @return If it is a valid ID or not
		 */
		public static boolean isValid(int id) { return id >= NOT_PRESSED && id <= UP_LEFT; }
	}
}