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

import java.util.Locale;

import org.team102.robots.robot2019.RobotMap;
import org.team102.robots.robot2019.lib.arduino.SubsystemWithArduino;

public class SubsystemCentering extends SubsystemWithArduino {
	
	private double frontSensorVal, rearSensorVal;
	private boolean enabled = true;
	
	public SubsystemCentering() {
		super("Centering", RobotMap.CENTERING_ARDUINO_WHOIS_RESPONSE, "Centering Sensor Control");
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void enable() {
		setEnabled(true);
	}
	
	public void disable() {
		setEnabled(false);
	}
	
	public double getFrontValue() {
		return frontSensorVal;
	}
	
	public double getRearValue() {
		return rearSensorVal;
	}
	
	public static boolean isCentered(double val) {
		return Math.abs(val) < RobotMap.CENTERING_ALLOWABLE_ERROR;
	}
	
	public MoveDirection getMoveDirection() {
		double front = getFrontValue();
		double rear = getRearValue();
		
		boolean frontLeft = front <= 0, rearLeft = rear <= 0;
		boolean frontRight = front > 0, rearRight = rear > 0;
		
		boolean frontCentered = isCentered(front);
		boolean rearCentered = isCentered(rear);
		
		if(!isEnabled() || (frontCentered && rearCentered)) {
			return MoveDirection.NONE;
		} else if(frontCentered) {
			return MoveDirection.FRONT;
		} else if(rearCentered) {
			return MoveDirection.BACK;
		} else if(frontLeft && rearLeft) {
			return MoveDirection.LEFT;
		} else if(frontRight && rearRight) {
			return MoveDirection.RIGHT;
		} else if(frontLeft && rearRight) {
			return MoveDirection.TURN_CCW;
		} else if(frontRight && rearLeft) {
			return MoveDirection.TURN_CW;
		} else {
			System.out.println("Warning: The centering subsystem is in an impossible state!");
			return MoveDirection.NONE;
		}
	}
	
	public String getStatus() {
		return getMoveDirection().message;
	}

	@Override
	protected void onArduinoLineReceived(String line) {
		try {
			if(line.toUpperCase(Locale.ROOT).startsWith("F:")) {
				frontSensorVal = parseVal(line);
			} else if(line.toUpperCase(Locale.ROOT).startsWith("B:")) {
				rearSensorVal = parseVal(line);
			} else {
				throw new UnsupportedOperationException("Unknown prefix on line");
			}
		} catch(Exception e) {
			System.err.println("Warning: Invalid data from the centering arduino: \"" + line + "\"");
		}
	}
	
	private static double parseVal(String line) {
		return Integer.parseInt(line.substring(2)) / 2000D - 1;
	}
	
	public static enum MoveDirection {
		NONE("Disabled"),
		FRONT("Move Forward"), BACK("Move Backward"),
		LEFT("Move Left"), RIGHT("Move Right"),
		TURN_CW("Turn Clockwise"), TURN_CCW("Turn Counter-Clockwise");
		
		public final String message;
		
		private MoveDirection(String message) {
			this.message = message;
		}
	}
}