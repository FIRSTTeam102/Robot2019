package org.team102.robots.robot2019.lib.arduino;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class SubsystemWithArduino extends Subsystem {
	
	private ArduinoConnection arduino;
	
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
	
	protected void sendLineToArduino(String line) {
		if(arduino != null) {
			arduino.write(line);
		}
	}
	
	protected abstract void onArduinoLineReceived(String line);
	
	@Override
	public void periodic() {
		if(arduino != null) {
			arduino.update();
		}
	}
}