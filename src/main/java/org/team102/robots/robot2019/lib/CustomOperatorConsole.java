package org.team102.robots.robot2019.lib;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * A connection to our custom Operator Console
 */
public class CustomOperatorConsole {
	
	private GenericHID connection;
	
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
	
	public JoystickButton getButton(int which) {
		return new JoystickButton(connection, which);
	}
	
	public void setLightPattern(int patternID) {
		int patternOutputBits = patternID
				<< 22 // Shift the data bits to positions 7 to 10 (they should currently be in bits 29 to 32)
				& 0b00000011110000000000000000000000; // Clear all the bits except 7 to 10
		
		connection.setOutputs(patternOutputBits);
	}
}