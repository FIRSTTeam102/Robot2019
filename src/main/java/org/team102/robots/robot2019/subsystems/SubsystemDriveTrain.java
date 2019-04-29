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

package org.team102.robots.robot2019.subsystems;

import org.team102.robots.robot2019.RobotMap;
import org.team102.robots.robot2019.commands.CommandDriveWithJoystick;
import org.team102.robots.robot2019.lib.CommonIDs;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;

public class SubsystemDriveTrain extends Subsystem {
	
	private RobotDriveBase drive;
	
	private WPI_TalonSRX frontLeft;
	private WPI_TalonSRX frontRight;
	private WPI_TalonSRX rearLeft;
	private WPI_TalonSRX rearRight;
	
	public SubsystemDriveTrain() {
		super("Drive Train");
		
		frontLeft = new WPI_TalonSRX(RobotMap.CAN_TALON_DRIVE_TRAIN_FRONT_LEFT);
		frontRight = new WPI_TalonSRX(RobotMap.CAN_TALON_DRIVE_TRAIN_FRONT_RIGHT);
		rearLeft = new WPI_TalonSRX(RobotMap.CAN_TALON_DRIVE_TRAIN_REAR_LEFT);
		rearRight = new WPI_TalonSRX(RobotMap.CAN_TALON_DRIVE_TRAIN_REAR_RIGHT);
		addChild("Front Left Motor", frontLeft);
		addChild("Front Right Motor", frontRight);
		addChild("Rear Left Motor", rearLeft);
		addChild("Rear Right Motor", rearRight);
		
		if(RobotMap.IS_TANK_MODE) {
			drive = new DifferentialDrive(
					new SpeedControllerGroup(frontLeft, rearLeft),
					new SpeedControllerGroup(frontRight, rearRight));
		} else {
			drive = new MecanumDrive(frontLeft, frontRight, rearLeft, rearRight);
		}
		
		drive.setSafetyEnabled(true);
		addChild("Drive Train", drive);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new CommandDriveWithJoystick());
	}
	
	public void drive(double frontBack, double leftRight, double rotation) {
		if(drive instanceof MecanumDrive) {
			((MecanumDrive)drive).driveCartesian(-frontBack, leftRight, rotation);
		} else if(drive instanceof DifferentialDrive) {
			((DifferentialDrive)drive).arcadeDrive(-frontBack, rotation);
		} else {
			DriverStation.reportError("DRIVE TRAIN IS IN AN INVALID STATE!!! THIS SHOULD BE IMPOSSIBLE!!!", false);
		}
	}
	
	public void driveWithJoystick(Joystick joy, boolean safetyEnabled) {
		double frontBack = joy.getRawAxis(CommonIDs.Gamepad.AXIS_LEFT_Y);
		double leftRight = joy.getRawAxis(CommonIDs.Gamepad.AXIS_LEFT_X);
		double rotation = joy.getRawAxis(CommonIDs.Gamepad.AXIS_RIGHT_X);
		
		if(safetyEnabled) {
			frontBack /= RobotMap.SAFETY_SENSOR_SPEED_DIVISOR;
		}
		
		drive(frontBack, leftRight, rotation);
	}
}