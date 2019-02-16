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
import org.team102.robots.robot2019.lib.arduino.SubsystemWithArduino;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;

public class SubsystemArm extends SubsystemWithArduino {
	
	private int distanceElbow = 0;
	private int distanceWrist = 0;
	
	private WPI_TalonSRX wrist;
	private WPI_TalonSRX elbow;
	
	private Solenoid extender;
	
	private boolean isInManualMode = false;
	private boolean manualModeIsWrist, manualModeIsReverse;
	
	private ArmSetpoint setpoint;
	
	private String elbowStatus = "Not updated yet?";
	private String wristStatus = "Not updated yet?";
	private String overallStatus = "Not updated yet?";
	
	public SubsystemArm() {
		super("Arm", RobotMap.ARM_ARDUINO_WHOIS_RESPONSE, "LIDAR Control");
		
		wrist = new WPI_TalonSRX(RobotMap.CAN_TALON_ARM_WRIST);
		elbow = new WPI_TalonSRX(RobotMap.CAN_TALON_ARM_ELBOW);
		addChild("Wrist Motor", wrist);
		addChild("Elbow Motor", elbow);
		
		extender = new Solenoid(RobotMap.SOLENOID_ARM_EXTENDER);		
		addChild("Extender Cylinder", extender);
	}
	
	public String getElbowStatus() {
		return elbowStatus;
	}
	
	public String getWristStatus() {
		return wristStatus;
	}
	
	public String getOverallStatus() {
		return overallStatus;
	}
	
	@Override
	public void periodic() {
		super.periodic();
		
		double elbowSpeed = 0, wristSpeed = 0;
		
		if(isInManualMode) {
			if(manualModeIsWrist) {
				wristSpeed = RobotMap.ARM_WRIST_SPEED;
			} else {
				elbowSpeed = RobotMap.ARM_ELBOW_SPEED;
			}
			
			if(manualModeIsReverse) {
				elbowSpeed *= -1;
				wristSpeed *= -1;
			}
			
			overallStatus = "Moving manually";
		} else if(hasSetpoint()) {
			boolean elbowInRange = isElbowInRange();
			boolean wristInRange = isWristInRange();
			
			elbowSpeed = RobotMap.ARM_ELBOW_SPEED;
			wristSpeed = RobotMap.ARM_WRIST_SPEED;
			
			if(elbowInRange) {
				elbowSpeed = 0;
			} else if(distanceElbow > setpoint.elbowSetpoint) {
				elbowSpeed *= -1;
			}
			
			if(wristInRange) {
				wristSpeed = 0;
			} else if(distanceWrist > setpoint.wristSetpoint) {
				wristSpeed *= -1;
			}
			
			if(elbowInRange && wristInRange) {
				overallStatus = "At";
			} else {
				overallStatus = "Moving to";
			}
			
			overallStatus += (" setpoint " + setpoint.name);
		} else {
			overallStatus = "Idle";
		}
		
		elbow.set(elbowSpeed);
		wrist.set(wristSpeed);
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void setExtender(boolean active) {
		extender.set(active);
	}
	
	protected void onArduinoLineReceived(String line) {
		String[] parts = line.split(",");
		
		try {
			distanceElbow = Integer.parseInt(parts[0]);
			boolean elbowInRange = distanceElbow != -1;
			
			int distanceWristLower = Integer.parseInt(parts[1]);
			int distanceWristUpper = Integer.parseInt(parts[2]);
			boolean wristLowerInRange = distanceWristLower != -1;
			boolean wristUpperInRange = distanceWristUpper != -1;
			
			boolean ignoreWristLower = false;
			boolean ignoreWristUpper = false;
			
			if(wristLowerInRange && wristUpperInRange) {
				if(distanceWristLower < distanceWristUpper) {
					ignoreWristLower = true;
				} else {
					ignoreWristUpper = true;
				}
			}
			
			if((!wristLowerInRange || ignoreWristLower) && (!wristUpperInRange && ignoreWristUpper)) {
				distanceWrist = 0;
			} else if(wristLowerInRange && !ignoreWristLower) {
				distanceWrist = -distanceWristLower;
			} else {
				distanceWrist = distanceWristUpper;
			}
			
			if(!wristLowerInRange && !wristUpperInRange) {
				wristStatus = "OK (Centered)";
			} else if(wristLowerInRange != wristUpperInRange) {
				wristStatus = "OK (Range: " + distanceWrist + ")";
			} else if(ignoreWristUpper && !ignoreWristLower) {
				wristStatus = "Both in range, using lower";
			} else if(ignoreWristLower && !ignoreWristUpper) {
				wristStatus = "Both in range, using upper";
			} else {
				wristStatus = "Impossible state (Please report this)";
			}
			
			if(elbowInRange) {
				elbowStatus = "OK (Range: " + distanceElbow + ")";
			} else {
				elbowStatus = "Out of range";
			}
		} catch(Exception e) {
			System.err.println("Warning: Invalid data \"" + line + "\" from the arm distance sensor Arduino!");
		}
	}
	
	public void setArmManual(boolean isWrist, boolean isReverse) {
		manualModeIsWrist = isWrist;
		manualModeIsReverse = isReverse;
		isInManualMode = true;
	}
	
	public void endManualMode() {
		isInManualMode = false;
	}
	
	public void setSetpoint(ArmSetpoint setpoint) {
		this.setpoint = setpoint;
	}
	
	public boolean hasSetpoint() {
		return setpoint != null;
	}
	
	private boolean isElbowInRange() {
		if(hasSetpoint()) {
			return Math.abs(distanceElbow - setpoint.elbowSetpoint) <= RobotMap.ARM_ELBOW_ACCEPTABLE_RANGE_OF_ERROR;
		} else {
			return true;
		}
	}
	
	private boolean isWristInRange() {
		if(hasSetpoint()) {
			return Math.abs(distanceWrist - setpoint.wristSetpoint) <= RobotMap.ARM_WRIST_ACCEPTABLE_RANGE_OF_ERROR;
		} else {
			return true;
		}
	}
	
	public boolean isWithinMarginOfErrorOfSetpoints() {
		return isElbowInRange() && isWristInRange();
	}
	
	public static class ArmSetpoint {
		
		public final String name;
		public final boolean isExtended;
		public final int elbowSetpoint, wristSetpoint;
		
		public ArmSetpoint(String name, boolean isExtended, int elbowSetpoint, int wristSetpoint) {
			this.name = name;
			this.isExtended = isExtended;
			this.elbowSetpoint = elbowSetpoint;
			this.wristSetpoint = wristSetpoint;
		}
		
		public String toString() {
			return "{ Elbow: " + elbowSetpoint + ", Wrist: " + wristSetpoint + " }";
		}
	}
}