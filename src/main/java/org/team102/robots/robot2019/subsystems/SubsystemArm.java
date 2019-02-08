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
import org.team102.robots.robot2019.lib.ArduinoConnection;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SubsystemArm extends Subsystem {
	
	private ArduinoConnection armArduino;
	
	private int distanceElbow = -1;
	private int distanceWristLower = -1, distanceWristUpper = -1;
	
	private long lastArduinoCommTime;
	
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
		
		armArduino = new ArduinoConnection(RobotMap.SERIAL_PORT_ID_ARM_ARDUINO);
		armArduino.setLineListener(this::onArduinoLineReceived);
		addChild("LIDAR Control Arduino", armArduino);
		
		setArduinoCommTime();
	}
	
	@Override
	public void periodic() {
		armArduino.update();
		
		double lastCommTime = getTimeSinceLastArduinoComm();
		if(lastCommTime > 1 && lastCommTime % 5 < .03) {
			System.out.println("Warning: Last Arduino comm time was " + lastCommTime + " seconds ago!");
		}
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void setExtender(boolean active) {
		extender.set(active);
	}
	
	private void onArduinoLineReceived(String line) {
		String[] parts = line.split(",");
		
		try {
			distanceElbow = Integer.parseInt(parts[0]);
			distanceWristLower = Integer.parseInt(parts[1]);
			distanceWristUpper = Integer.parseInt(parts[2]);
			
			setArduinoCommTime();
		} catch(Exception e) {
			System.err.println("Warning: Invalid data \"" + line + "\" from the arm distance sensor Arduino!");
		}
	}
	
	private void setArduinoCommTime() {
		lastArduinoCommTime = System.currentTimeMillis();
	}
	
	public double getTimeSinceLastArduinoComm() {
		return (System.currentTimeMillis() - lastArduinoCommTime) / 1e3D;
	}
}