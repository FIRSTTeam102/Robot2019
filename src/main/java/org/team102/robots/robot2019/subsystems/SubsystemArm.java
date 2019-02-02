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
import org.team102.robots.robot2019.lib.SupplierPIDSource;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SubsystemArm extends Subsystem {
	
	private PIDController wristController;
	private PIDController elbowController;
	
	private WPI_TalonSRX wrist;
	private WPI_TalonSRX elbow;
	
	private Solenoid extender;
	
	public SubsystemArm() {
		super("Arm");
		
		wrist = new WPI_TalonSRX(RobotMap.CAN_TALON_ARM_WRIST);
		elbow = new WPI_TalonSRX(RobotMap.CAN_TALON_ARM_ELBOW);
		addChild("Wrist Motor", wrist);
		addChild("Elbow Motor", elbow);
		
		extender = new Solenoid(RobotMap.SOLENOID_ARM_EXTENDER);		
		addChild("Extender Cylinder", extender);
		
		PIDSource wristSrc = new SupplierPIDSource(this::getWristSensorValue);
		PIDSource elbowSrc = new SupplierPIDSource(this::getElbowSensorValue);
		
		wristController = new PIDController(0, 0, 0, wristSrc, wrist); // TODO Add PID constants
		elbowController = new PIDController(0, 0, 0, elbowSrc, elbow);
		addChild("Wrist PID loop", wristController);
		addChild("Elbow PID loop", elbowController);
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void setExtender(boolean active) {
		extender.set(active);
	}
	
	public void setPIDEnabled(boolean on) {
		wristController.setEnabled(on);
		elbowController.setEnabled(on);
	}
	
	public void setWristAngle(double angle) {
		wristController.setSetpoint(angle);
	}
	
	public void setElbowAngle(double angle) {
		elbowController.setSetpoint(angle);
	}
	
	private double getWristSensorValue() {
		return 0; // TODO Implement
	}
	
	private double getElbowSensorValue() {
		return 0; // TODO Implement
	}
}