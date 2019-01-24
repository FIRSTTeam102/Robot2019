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

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class SubsystemDriveTrain  extends Subsystem {
	
	private MecanumDrive drive;
	private WPI_TalonSRX frontLeft;
	private WPI_TalonSRX frontRight;
	private WPI_TalonSRX backLeft;
	private WPI_TalonSRX backRight;
	
	public SubsystemDriveTrain() {
		super("Drive Train");
		
		//creates and instantiates motor controllers
		frontLeft = new WPI_TalonSRX(RobotMap.m0); 
		frontRight = new WPI_TalonSRX(RobotMap.m1);
		 backLeft = new WPI_TalonSRX(RobotMap.m2);
		backRight = new WPI_TalonSRX(RobotMap.m3);
		
		//creates and instantiates mecanum drive train
		drive = new MecanumDrive(frontLeft, frontRight, backLeft, backRight);
		
		drive.setSafetyEnabled(true);
		
	}

	@Override
	protected void initDefaultCommand() {
		
		//drive with controller will be here eventually
	}
	//right now takes in the speeds because I'm unfamiliar with new control system
	//should eventually take in controller and .get controller values in this method
	public void driveWithController( double ySpeed, double xSpeed, double zRotation) {
		
		
		drive.driveCartesian(ySpeed, xSpeed, zRotation);
	}
	
}