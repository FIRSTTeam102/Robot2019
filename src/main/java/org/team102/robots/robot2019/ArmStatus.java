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

public class ArmStatus {
	
	public final String name;
	
	public final boolean isExtended;
	public final int elbowSetpoint, wristSetpoint;
	
	public final boolean isHatchEjectorExtended;
	
	public final double cargoRollerActiveTime;
	public final boolean isCargoRollerReverse;
	
	public ArmStatus(String name, boolean isExtended, int elbowSetpoint, int wristSetpoint, boolean isHatchEjectorExtended, double cargoRollerActiveTime, boolean isCargoRollerReverse) {
		this.name = name;
		
		this.isExtended = isExtended;
		this.elbowSetpoint = elbowSetpoint;
		this.wristSetpoint = wristSetpoint;
		
		this.isHatchEjectorExtended = isHatchEjectorExtended;
		
		this.cargoRollerActiveTime = cargoRollerActiveTime;
		this.isCargoRollerReverse = isCargoRollerReverse;
	}
	
	public String toString() {
		return "{ Elbow: " + elbowSetpoint + ", Wrist: " + wristSetpoint + " }";
	}
}