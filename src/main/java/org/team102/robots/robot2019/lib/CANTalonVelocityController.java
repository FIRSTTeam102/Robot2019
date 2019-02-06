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

package org.team102.robots.robot2019.lib;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * A utility class to use the internal PID control system of a CanTalon
 */
public class CANTalonVelocityController extends SendableBase {
	
	/** The CANTalon */
	private final TalonSRX talon;
	
	/** Which internal PID loop we're using */
	private final int pidLoopID;
	
	/** Our confirm timeout */
	private final int timeoutMS;
	
	/** The PID constants */
	private double p, i, d, f;
	
	/**
	 * Create the velocity controller, with PID loop #0 and a 30ms timeout period
	 * @param talon The CanTalon
	 * @param isEncoderAbsolute Whether or not the connected encoder is absolute or relative
	 * @param isSensorInPhase Whether or not the sensor is in phase 
	 * @param p The P coefficient for the PID loop
	 * @param i The I coefficient for the PID loop
	 * @param d The D coefficient for the PID loop
	 * @param f The feed-forward value for the PID loop
	 */
	public CANTalonVelocityController(TalonSRX talon, boolean isEncoderAbsolute, boolean isSensorInPhase, double p, double i, double d, double f) {
		this(talon, isEncoderAbsolute, 0, 30, isSensorInPhase, p, i, d, f);
	}
	
	/**
	 * Create the velocity controller
	 * @param talon The CanTalon
	 * @param isEncoderAbsolute Whether or not the connected encoder is absolute or relative
	 * @param pidLoopID Which PID loop inside the talon to use
	 * @param timeoutMS The timeout to use (in milliseconds)
	 * @param isSensorInPhase Whether or not the sensor is in phase 
	 * @param p The P coefficient for the PID loop
	 * @param i The I coefficient for the PID loop
	 * @param d The D coefficient for the PID loop
	 * @param f The feed-forward value for the PID loop
	 */
	public CANTalonVelocityController(TalonSRX talon, boolean isEncoderAbsolute, int pidLoopID, int timeoutMS, boolean isSensorInPhase, double p, double i, double d, double f) {
		this.talon = talon;
		
		this.pidLoopID = pidLoopID;
		this.timeoutMS = timeoutMS;
		
		talon.configFactoryDefault();
		
		talon.configSelectedFeedbackSensor(isEncoderAbsolute ? FeedbackDevice.CTRE_MagEncoder_Absolute : FeedbackDevice.CTRE_MagEncoder_Relative, pidLoopID, timeoutMS);
		talon.setSensorPhase(isSensorInPhase);
		
		talon.configNominalOutputForward(0, timeoutMS);
		talon.configNominalOutputReverse(0, timeoutMS);
		talon.configPeakOutputForward(1, timeoutMS);
		talon.configPeakOutputReverse(-1, timeoutMS);
		
		setPIDP(p);
		setPIDI(i);
		setPIDD(d);
		setPIDF(f);
	}
	
	/**
	 * Sets the CANTalon's internal PID loop's P value
	 * @param p The value to set
	 */
	public void setPIDP(double p) {
		talon.config_kP(pidLoopID, p, timeoutMS);
		this.p = p;
	}
	
	/**
	 * Sets the CANTalon's internal PID loop's I value
	 * @param i The value to set
	 */
	public void setPIDI(double i) {
		talon.config_kI(pidLoopID, i, timeoutMS);
		this.i = i;
	}
	
	/**
	 * Sets the CANTalon's internal PID loop's D value
	 * @param d The value to set
	 */
	public void setPIDD(double d) {
		talon.config_kD(pidLoopID, d, timeoutMS);
		this.d = d;
	}
	
	/**
	 * Sets the CANTalon's internal PID loop's F value
	 * @param f The value to set
	 */
	public void setPIDF(double f) {
		talon.config_kF(pidLoopID, f, timeoutMS);
		this.f = f;
	}
	
	/**
	 * Gets the CANTalon's internal PID loop's P value
	 * @return The value
	 */
	public double getPIDP() {
		return p;
	}
	
	/**
	 * Gets the CANTalon's internal PID loop's I value
	 * @return The value
	 */
	public double getPIDI() {
		return i;
	}
	
	/**
	 * Gets the CANTalon's internal PID loop's D value
	 * @return The value
	 */
	public double getPIDD() {
		return d;
	}
	
	/**
	 * Gets the CANTalon's internal PID loop's F value
	 * @return The value
	 */
	public double getPIDF() {
		return f;
	}
	
	/**
	 * Sets the set point for the angular velocity of the CANTalon
	 * @param velocity The velocity set point, in degrees per second
	 */
	public void setTargetVelocity(double velocity) {
		// Velocity in degrees/second
		//  -> / 360	degrees/second -> rotations/second
		//  -> * 4096	rotations/second -> encoder units/second
		//  -> / 10		encoder units/second -> encoder units/100ms
		
		talon.set(ControlMode.Velocity, velocity / 360 * 4096 / 10);
	}
	
	/**
	 * Gets the current angular velocity of the CANTalon
	 * @return The velocity, in degrees per second
	 */
	public double getCurrentVelocity() {
		// Velocity in encoder units/100ms
		//  -> * 10		encoder units/100ms -> encoder units/second
		//  -> / 4096	encoder units/second -> rotations/second
		//  -> * 360	rotations/second -> degrees/second
		
		return talon.getSelectedSensorVelocity(pidLoopID) * 10 / 4096 * 360;
	}
	
	/**
	 * Stops the motor
	 */
	public void stop() {
		talon.set(ControlMode.Disabled, 0);
	}
	
	@Override
	public void initSendable(SendableBuilder builder) {
		builder.addDoubleProperty("Velocity", this::getCurrentVelocity, this::setTargetVelocity);
		builder.setSafeState(this::stop);
		
		builder.addDoubleProperty("PID Kp", this::getPIDP, this::setPIDP);
		builder.addDoubleProperty("PID Ki", this::getPIDI, this::setPIDI);
		builder.addDoubleProperty("PID Kd", this::getPIDD, this::setPIDD);
		builder.addDoubleProperty("PID FF", this::getPIDF, this::setPIDF);
	}
}