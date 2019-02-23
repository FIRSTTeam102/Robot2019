package org.team102.robots.robot2019.subsystems;

import org.team102.robots.robot2019.RobotMap;
import org.team102.robots.robot2019.lib.arduino.SubsystemWithArduino;

public class SubsystemAesthetics extends SubsystemWithArduino {
	
	private int[] currentPatternSet;
	private int currentPattern;
	
	private long currentPatternStartTime = -1;
	private boolean patternSetChanged = false;
	
	public SubsystemAesthetics() {
		super("Robot Aesthetics", RobotMap.LONG_LIGHT_STRIP_ARDUINO_WHOIS_RESPONSE, "Long Light Strip");
	}
	
	@Override
	protected void onArduinoLineReceived(String line) {
		
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	@Override
	public void periodic() {
		if(currentPatternSet != null && currentPatternSet.length > 0) {
			long now = System.nanoTime();
			
			if(currentPatternStartTime == -1) {
				currentPatternStartTime = now;
			}
			
			double delta = (now - currentPatternStartTime) / 1e9D;
			
			if(delta >= RobotMap.AESTHETICS_PATTERN_CHANGE_TIME || patternSetChanged) {
				currentPatternStartTime = now;
				patternSetChanged = false;
				
				currentPattern = (currentPattern + 1) % currentPatternSet.length;
				setLongLightStripPattern(currentPatternSet[currentPattern]);
			}
		}
	}
	
	public void setLongLightStripPattern(int pattern) {
		sendLineToArduino("PAT:" + pattern);
	}
	
	public void setLongLightStripPatternSet(int[] patterns) {
		currentPatternSet = patterns;
		patternSetChanged = true;
	}
}