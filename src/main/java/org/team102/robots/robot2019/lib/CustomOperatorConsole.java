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

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * A connection to our custom Operator Console
 */
public class CustomOperatorConsole {
	
	private GenericHID connection;
	
	/**
	 * Create the console
	 * @param port Which {@link GenericHID HID port} to connect to
	 */
	public CustomOperatorConsole(int port) {
		connection = new GenericHID(port) {
			
			@Override
			public double getX(Hand hand) {
				return 0;
			}
			
			@Override
			public double getY(Hand hand) {
				return 0;
			}
		};
	}
	
	/**
	 * Gets the given button on the controller
	 * @param which Which button
	 * @return The {@link JoystickButton button version} of the given ID
	 */
	public JoystickButton getButton(int which) {
		return new JoystickButton(connection, which);
	}
	
	/**
	 * Sets the light pattern to the given four-bit ID
	 * @param patternID The light pattern's ID
	 */
	public void setLightPattern(int patternID) {
		int patternOutputBits = ~patternID				// Invert the current bits (the light system is active-low)
				<< 6									// Shift the data bits to the correct positions (from bits 29 - 32)
				& 0b00000000000000000000001111000000;	// Clear all the bits except the ones we want
		
		connection.setOutputs(patternOutputBits);
	}
}