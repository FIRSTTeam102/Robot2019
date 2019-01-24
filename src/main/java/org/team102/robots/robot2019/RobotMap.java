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
	
	public static final int CAMERA_ID_VISION_REAL = 0;
	public static final int CAMERA_ID_VISION_IN_SIMULATOR = 1;
	
	public static final int CAMERA_ID_VISION = Robot.isReal() ? CAMERA_ID_VISION_REAL : CAMERA_ID_VISION_IN_SIMULATOR;
	
	public static final int CAN_TALON_DRIVE_TRAIN_FRONT_LEFT = 0;
	public static final int CAN_TALON_DRIVE_TRAIN_FRONT_RIGHT = 1;
	public static final int CAN_TALON_DRIVE_TRAIN_REAR_LEFT = 2;
	public static final int CAN_TALON_DRIVE_TRAIN_REAR_RIGHT = 3;
	
	public static final int CAN_TALON_ARM_ELBOW = 4;
	public static final int CAN_TALON_ARM_WRIST = 5;
	
	public static final int CAN_TALON_CARGO_MANIP_ROLLER = 6;
	
	public static final int SOLENOID_ARM_EXTENDER = 0;
	
	public static final int SOLENOID_HATCH_MANIP_EJECTOR_1 = 1;
	public static final int SOLENOID_HATCH_MANIP_EJECTOR_2 = 2;
	
	public static final int SOLENOID_CLIMBER = 3;
}