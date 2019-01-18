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

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * A {@link edu.wpi.first.wpilibj.buttons.Button Button} that triggers from a POV switch being in a certain position
 */
public class POVButton extends Button {
	/**
	 * Which position for the POV switch<br>
	 * <b>This is an internal field. It should not be used by the end user.</b>
	 */
	protected final POVPosition pos;
	
	/**
	 * Which {@link GenericHID HID device} and which POV switch to use<br>
	 * <b>This is an internal field. It should not be used by the end user.</b>
	 */
	protected final int hid, pov;
	
	/**
	 * Creates the Button, for POV switch zero on the device
	 * @param hid Which {@link GenericHID HID device} to use
	 * @param pos What position the POV switch must be in
	 */
	public POVButton(GenericHID hid, POVPosition pos) { this(hid, 0, pos); }
	
	/**
	 * Creates the Button
	 * @param hid Which {@link GenericHID HID device} to use
	 * @param pov Which POV switch to use
	 * @param pos What position the POV switch must be in
	 */
	public POVButton(GenericHID hid, int pov, POVPosition pos) { this(hid.getPort(), pov, pos); }
	
	/**
	 * Creates the Button
	 * @param hid Which {@link GenericHID HID device} to use
	 * @param pov Which POV switch to use
	 * @param pos What position the POV switch must be in
	 */
	public POVButton(int hid, int pov, POVPosition pos) {
		if(hid < 0 || hid > 5) throw new IllegalArgumentException("Must provide valid HID ID");
		if(pov < 0 || pov > DriverStation.getInstance().getStickPOVCount(hid)) throw new IllegalArgumentException("Must provide a valid POV switch ID");
		if(pos == null) throw new IllegalArgumentException("Must provide a valid POV switch direction");
		
		this.hid = hid;
		this.pov = pov;
		this.pos = pos;
	}
	
	public boolean get() { return pos.accepts(hid, pov); }
}