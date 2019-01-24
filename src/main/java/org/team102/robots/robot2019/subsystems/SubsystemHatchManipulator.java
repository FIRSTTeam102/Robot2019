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

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SubsystemHatchManipulator  extends Subsystem {
	
	private Solenoid ejector1;
	private Solenoid ejector2;
	
	public SubsystemHatchManipulator() {
		super("Hatch Manipulator");
		
		ejector1 = new Solenoid(RobotMap.SOLENOID_HATCH_MANIP_EJECTOR_1);
		ejector2 = new Solenoid(RobotMap.SOLENOID_HATCH_MANIP_EJECTOR_2);
		addChild("Ejection Cylinder 1", ejector1);
		addChild("Ejection Cylinder 2", ejector2);
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void setEjector(boolean extended) {
		ejector1.set(extended);
		ejector2.set(extended);
	}
}