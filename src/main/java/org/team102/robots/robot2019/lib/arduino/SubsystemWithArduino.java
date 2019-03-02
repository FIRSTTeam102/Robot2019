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

package org.team102.robots.robot2019.lib.arduino;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * A {@link Subsystem} that connects to an Arduino using an {@link ArduinoConnection}
 */
public abstract class SubsystemWithArduino extends Subsystem {
	
	private ArduinoConnection arduino;
	
	/**
	 * Create the subsystem
	 * @param subsystemName The name for the subsystem
	 * @param arduinoWhoisResponse What the Arduino's WHOIS response will be
	 * @param humanReadableArduinoName The human-readable name of the arduino
	 */
	public SubsystemWithArduino(String subsystemName, String arduinoWhoisResponse, String humanReadableArduinoName) {
		super(subsystemName);
		
		arduino = ArduinoConnection.lookUpByWhois(arduinoWhoisResponse);
		if(arduino == null) {
			System.out.println("Warning: " + humanReadableArduinoName + " arduino not found!");
		} else {
			arduino.setLineListener(this::onArduinoLineReceived);
			addChild(humanReadableArduinoName + " Arduino", arduino);
		}
	}
	
	/**
	 * Sends the given line of text to the Arduino (no CR or LF required), if it's connected
	 * @param line The text
	 */
	protected void sendLineToArduino(String line) {
		if(arduino != null) {
			arduino.write(line);
		}
	}
	
	/**
	 * Called by the {@link #periodic()} method when a line of text from the Arduino has been received
	 * @param line The text that has been received
	 */
	protected abstract void onArduinoLineReceived(String line);
	
	@Override
	public void periodic() {
		if(arduino != null) {
			arduino.update();
		}
	}
}