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

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class SubsystemShuffleboard extends Subsystem {
	NetworkTableEntry testIntA;
	NetworkTableEntry testIntB;
	NetworkTableEntry testIntC;
	NetworkTableEntry testBoolA;
	ShuffleboardTab tab1;
	
	public SubsystemShuffleboard() {
		tab1 = Shuffleboard.getTab("Tab1");
		
		testIntA = tab1
				.add("Test Int A", 0)
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("min", 0, "max", 100))
				.getEntry();
		
		testIntB = tab1
				.add("Test Int B", 0).getEntry();
		
		testIntC = tab1
				.add("Test Int C", 0)
				.withWidget(BuiltInWidgets.kGraph)
				.getEntry();	
	}
	
	@Override
	public void periodic() {
		testIntB.setNumber( (int) (Math.random() * (testIntA.getNumber(0).intValue())));
		testIntC.setNumber(testIntB.getNumber(0));
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
}