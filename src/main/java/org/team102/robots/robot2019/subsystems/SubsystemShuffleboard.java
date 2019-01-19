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

import org.team102.robots.robot2019.OI;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SubsystemShuffleboard extends Subsystem {
	
	int testIntA;
	int testIntB;
	int testIntC;
	boolean testBoolA;
	
	public SubsystemShuffleboard() {
		
		testIntA = 0;
		testIntB = 0;
		testIntC = 10;
		testBoolA = false;

		SmartDashboard.putNumber("Test Int A", testIntA);
		SmartDashboard.putNumber("Test Int B", testIntB);
		SmartDashboard.putNumber("Test Int C", testIntC);
		SmartDashboard.putBoolean("Test Bool A", testBoolA);
		
	}
	
	@Override
	public void periodic() {
		System.out.println("Test");
	}

	@Override
	protected void initDefaultCommand() {
		
	}
}
