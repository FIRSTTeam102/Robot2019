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

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * A detector to use a PDP channel as a limit switch, when it exceeds a given current
 */
public class CurrentLimitDetector extends SendableBase {
	private PowerDistributionPanel pdp;
	private int channel;
	private double triggerCurrent;
	
	/**
	 * Create the current detector
	 * @param pdp The {@link PowerDistributionPanel PDP} to use
	 * @param channel Which channel on the PDP
	 * @param triggerCurrent The maximum current
	 */
	public CurrentLimitDetector(PowerDistributionPanel pdp, int channel, double triggerCurrent) {
		this.pdp = pdp;
		this.channel = channel;
		this.triggerCurrent = triggerCurrent;
	}
	
	/**
	 * Gives back the current currently going through the PDP channel
	 * @return The current
	 */
	public double getChannelCurrent() {
		if(channel == -1) {
			return 0;
		}
		
		return pdp.getCurrent(channel);
	}
	
	/**
	 * Sets the current needed to trigger the limit, or -1 to disable
	 * @param maxCurrent The current
	 */
	public void setTriggerCurrent(double maxCurrent) {
		this.triggerCurrent = maxCurrent;
	}
	
	/**
	 * Gives back the current needed to trigger the limit
	 * @return The current
	 */
	public double getTriggerCurrent() {
		return triggerCurrent;
	}
	
	/**
	 * Gives back whether or not the limit is currently triggered,
	 * based on the {@link #setTriggerCurrent(double) trigger current} and the {@link #getChannelCurrent() channel current}
	 * @return
	 */
	public boolean isOverCurrent() {
		double max = getTriggerCurrent();
		if(max == -1) {
			return false;
		}
		
		return getChannelCurrent() >= getTriggerCurrent();
	}
	
	@Override
	public void initSendable(SendableBuilder builder) {
		builder.addDoubleProperty("ChannelCurrrent", this::getChannelCurrent, null);
		builder.addDoubleProperty("TriggerCurrent", this::getTriggerCurrent, this::setTriggerCurrent);
		builder.addBooleanProperty("IsOverCurrent", this::isOverCurrent, null);
	}
}