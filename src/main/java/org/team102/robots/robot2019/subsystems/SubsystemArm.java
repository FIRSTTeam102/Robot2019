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

import org.team102.robots.robot2019.ArmConfig;
import org.team102.robots.robot2019.Robot;
import org.team102.robots.robot2019.RobotMap;
import org.team102.robots.robot2019.lib.CurrentLimitDetector;
import org.team102.robots.robot2019.lib.arduino.SubsystemWithArduino;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;

public class SubsystemArm extends SubsystemWithArduino {
	
	private DigitalInput elbowLimitLower;
	private CurrentLimitDetector wristLimit;
	
	private int elbowMin = Integer.MAX_VALUE;
	private int wristMin = Integer.MAX_VALUE;
	
	private int distanceElbow = 0;
	private int distanceWrist = 0;
	
	private SpeedController wrist;
	private WPI_TalonSRX elbow;
	
	private Solenoid extender;
	
	private boolean isInManualMode = false;
	private boolean manualModeIsWrist, manualModeIsReverse;
	
	private ArmSetpoint setpoint;
	
	private String elbowStatus = "Not updated yet?";
	private String wristStatus = "Not updated yet?";
	private String overallStatus = "Not updated yet?";
	
	public static double sigmoid(double val) {
		return 1 / (1 + Math.exp(-val));
	}
	
	public static double squish(double val, double min) {
		double modifiedSigmoid = 2 * sigmoid(val) - 1;
		return Math.max(modifiedSigmoid, min);
	}
	
	public SubsystemArm() {
		super("Arm", RobotMap.ARM_ARDUINO_WHOIS_RESPONSE, "LIDAR Control");
		
		if(RobotMap.ARM_USE_VICTOR_FOR_WRIST) {
			wrist = new VictorSP(RobotMap.PWM_ID_ARM_WRIST_USING_VICTOR);
		} else {
			wrist = new WPI_TalonSRX(RobotMap.CAN_TALON_ARM_WRIST);
		}
		
		elbow = new WPI_TalonSRX(RobotMap.CAN_TALON_ARM_ELBOW);
		elbow.setNeutralMode(NeutralMode.Brake);
		
		addChild("Wrist Motor", (Sendable)wrist);
		addChild("Elbow Motor", elbow);
		
		extender = new Solenoid(RobotMap.SOLENOID_ARM_EXTENDER);		
		addChild("Extender Cylinder", extender);
		
		elbowLimitLower = addLimitSwitch(RobotMap.DIO_ID_ARM_ELBOW_LIMIT_SWITCH, "Elbow");
		
		wristLimit = new CurrentLimitDetector(Robot.pdp, RobotMap.PDP_ID_ARM_WRIST, RobotMap.PDP_MAX_CURRENT_ARM_WRIST);
		addChild("Wrist Limit Current Sensor", wristLimit);
	}
	
	private DigitalInput addLimitSwitch(int id, String name) {
		if(id == -1) {
			return null;
		}
		
		DigitalInput input = new DigitalInput(id);
		addChild(name + " Limit Switch", input);
		return input;
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
		
		if(getTimeSinceLastArduinoMessage() >= 2) {
			overallStatus = "CONNECTION LOST";
		}
		
		double elbowSpeed = 0, wristSpeed = 0;
		
		if(isInManualMode) {
			if(manualModeIsWrist) {
				if(manualModeIsReverse) {
					wristSpeed = RobotMap.ARM_WRIST_DOWN_SPEED;
				} else {
					wristSpeed = RobotMap.ARM_WRIST_SPEED;
				}
			} else {
				if(manualModeIsReverse) {
					elbowSpeed = RobotMap.ARM_ELBOW_DOWN_SPEED;
				} else {
					elbowSpeed = RobotMap.ARM_ELBOW_SPEED;
				}
			}
			
			overallStatus = "Moving manually";
		} else if(hasSetpoint()) {
			boolean elbowInRange = isElbowInRange();
			boolean wristInRange = isWristInRange();
			
			elbowSpeed = RobotMap.ARM_ELBOW_SPEED;
			wristSpeed = RobotMap.ARM_WRIST_SPEED;
			
			if(elbowInRange) {
				elbowSpeed = 0;
			} else {
				if(distanceElbow > setpoint.elbowSetpoint) {
					elbowSpeed = RobotMap.ARM_ELBOW_DOWN_SPEED;
				}
				
				if(RobotMap.ARM_ENABLE_DAMPENING) {
					elbowSpeed *= squish(getElbowAbsError(), RobotMap.ARM_ELBOW_GRAV_COMP_SPEED * 1.5);
				}
			}
			
			if(wristInRange) {
				wristSpeed = 0;
			} else {
				if(distanceWrist < setpoint.wristSetpoint) {
					wristSpeed = RobotMap.ARM_WRIST_DOWN_SPEED;
				}
				
				if(RobotMap.ARM_ENABLE_DAMPENING) {
					wristSpeed *= squish(getWristAbsError(), 0);
				}
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
		
		if(elbowSpeed < 0 && isElbowLimitedDown()) {
			elbowSpeed = 0;
		}
		
		if(isWristLimited()) {
			wristSpeed = 0;
		}
		
		if(elbowSpeed == 0) {
			elbowSpeed = RobotMap.ARM_ELBOW_GRAV_COMP_SPEED;
		}
		
		if(RobotMap.ARM_REVERSE_ELBOW) {
			elbowSpeed *= -1;
		}
		
		if(RobotMap.ARM_REVERSE_WRIST) {
			wristSpeed *= -1;
		}
		
		elbow.set(elbowSpeed);
		wrist.set(wristSpeed);
	}
	
	public boolean isElbowLimitedDown() {
		if(elbowLimitLower == null || Robot.limitSwitchesDisabled) {
			return false;
		}
		
		boolean active = elbowLimitLower.get();
		if(RobotMap.DIO_CONFIG_ARM_ELBOW_LIMIT_SWITCH_IS_ACTIVE_LOW) {
			active = !active;
		}
		
		return active;
	}
	
	public boolean isWristLimited() {
		return wristLimit.isOverCurrent();
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void setExtender(boolean active) {
		extender.set(active);
	}
	
	public boolean getExtender() {
		return extender.get();
	}
	
	protected void onArduinoLineReceived(String line) {
		try {
			String[] tokens = line.split(",");
			
			int elbow = Integer.parseInt(tokens[0]) / 5;
			if(elbowMin == Integer.MAX_VALUE) {
				elbowMin = elbow;
			}
			
			int wrist = Integer.parseInt(tokens[1]) / 5;
			if(wristMin == Integer.MAX_VALUE) {
				wristMin = wrist;
			}
			
			distanceElbow = -(elbow - elbowMin);
			elbowStatus = "OK (Range: " + distanceElbow + ")";
			
			distanceWrist = -(wrist - wristMin);
			wristStatus = "OK (Range: " + distanceWrist + ")";
		} catch(Exception e) {
			System.err.println("Warning: Invalid data \"" + line + "\" from the arm distance sensor Arduino!");
		}
	}
	
	public void resetWristAccelerometer() {
		wristMin += distanceWrist;
	}
	
	public void resetElbowAccelerometer() {
		elbowMin += distanceElbow;
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
	
	private int getElbowAbsError() {
		return Math.abs(distanceElbow - setpoint.elbowSetpoint);
	}
	
	private boolean isElbowInRange() {
		if(hasSetpoint() && setpoint.elbowSetpoint != -1) {
			return getElbowAbsError() <= ArmConfig.ELBOW_MARGIN_OF_ERROR;
		} else {
			return true;
		}
	}
	
	private int getWristAbsError() {
		return Math.abs(distanceWrist - setpoint.wristSetpoint);
	}
	
	private boolean isWristInRange() {
		if(hasSetpoint() && setpoint.wristSetpoint != -1) {
			return getWristAbsError() <= ArmConfig.WRIST_MARGIN_OF_ERROR;
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