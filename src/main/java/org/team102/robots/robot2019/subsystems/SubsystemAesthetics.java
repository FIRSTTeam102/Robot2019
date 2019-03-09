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