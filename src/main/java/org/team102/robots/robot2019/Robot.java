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


import org.team102.robots.robot2019.subsystems.*;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot {
	public static SubsystemDriveTrain driveTrain;
	public static SubsystemArm arm;
	public static SubsystemCargoManipulator cargoManip;
	public static SubsystemHatchManipulator hatchManip;
	public static SubsystemClimber climber;
	public static SubsystemCameras cameras;
	
	public static OI oi;
	
	
	int testIntA;
	int testIntB;
	int testIntC;
	boolean testBoolA;
	
	// AWLAYS include this!!
	public static void main(String[] args) {
		TimedRobot.startRobot(Robot::new);
	}
	
	public void robotInit() {
		driveTrain = new SubsystemDriveTrain();
		arm = new SubsystemArm();
		cargoManip = new SubsystemCargoManipulator();
		hatchManip = new SubsystemHatchManipulator();
		climber = new SubsystemClimber();
		cameras = new SubsystemCameras();
		
		oi = new OI();
		
		/*testIntA = 0;
		testIntB = 0;
		testIntC = 10;
		testBoolA = false;

		SmartDashboard.putNumber("Test Int A", testIntA);
		SmartDashboard.putNumber("Test Int B", testIntB);
		SmartDashboard.putNumber("Test Int C", testIntC);
		SmartDashboard.putBoolean("Test Bool A", testBoolA);*/
	}
	
	@Override
	public void robotPeriodic() {
		
		/*testBoolA = SmartDashboard.getBoolean("Test Bool A", false);
		testIntC = (int)SmartDashboard.getNumber("Test Int C", 10);
		
		if(testBoolA) {
			testIntA = (int)(Math.random()*testIntC);
		}
		
		testIntB = testIntA;
		
		SmartDashboard.putNumber("Test Int A", testIntA);
		SmartDashboard.putNumber("Test Int B", testIntB);*/
		
	}
	
	
}