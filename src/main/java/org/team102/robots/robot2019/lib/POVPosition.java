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

import org.team102.robots.robot2019.lib.CommonIDs.POVSwitch;
import org.team102.robots.robot2019.lib.commandsAndTrigger.POVButton;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;

/**
 * A class representing the possible positions of the switch, with both compound and axial positions supported
 */
public class POVPosition {
	/** Not pressed */				public static final POVPosition NONE = new POVPosition(POVSwitch.NOT_PRESSED);
	/** Center pressed */			public static final POVPosition CENTER = new POVPosition(POVSwitch.CENTER);
	
	/** 0 degrees */				public static final POVPosition UP_CENTER = new POVPosition(POVSwitch.UP_CENTER);
	/** 45 degrees */				public static final POVPosition UP_RIGHT = new POVPosition(POVSwitch.UP_RIGHT);
	/** 90 degrees */				public static final POVPosition CENTER_RIGHT = new POVPosition(POVSwitch.CENTER_RIGHT);
	/** 135 degrees */				public static final POVPosition DOWN_RIGHT = new POVPosition(POVSwitch.DOWN_RIGHT);
	/** 180 degrees */				public static final POVPosition DOWN_CENTER = new POVPosition(POVSwitch.DOWN_CENTER);
	/** 225 degrees */				public static final POVPosition DOWN_LEFT = new POVPosition(POVSwitch.DOWN_LEFT);
	/** 270 degrees */				public static final POVPosition CENTER_LEFT = new POVPosition(POVSwitch.CENTER_LEFT);
	/** 315 degrees */				public static final POVPosition UP_LEFT = new POVPosition(POVSwitch.UP_LEFT);
	
	/** Anywhere on the top */		public static final POVPosition ALL_UPS = new POVPosition(POVSwitch.UP_LEFT, POVSwitch.UP_CENTER, POVSwitch.UP_RIGHT);
	/** Anywhere on the right */	public static final POVPosition ALL_RIGHTS = new POVPosition(POVSwitch.UP_RIGHT, POVSwitch.CENTER_RIGHT, POVSwitch.DOWN_RIGHT);
	/** Anywhere on the bottom */	public static final POVPosition ALL_DOWNS = new POVPosition(POVSwitch.DOWN_LEFT, POVSwitch.DOWN_CENTER, POVSwitch.DOWN_RIGHT);
	/** Anywhere on the left */		public static final POVPosition ALL_LEFTS = new POVPosition(POVSwitch.UP_LEFT, POVSwitch.CENTER_LEFT, POVSwitch.DOWN_LEFT);
	
	/**
	 * Which IDs are accepted<br>
	 * <b>This is an internal field. It should not be used by the end user.</b>
	 */
	protected final int[] ids;
	
	/**
	 * Creates the POV position
	 * @param ids Which IDs are accepted in this position
	 */
	public POVPosition(int... ids) {
		if(ids.length < 1) throw new IllegalArgumentException("Must provide at least one ID");
		for(int id : ids) if(!POVSwitch.isValid(id)) throw new IllegalArgumentException("One or more of the given IDs were invalid");
		
		this.ids = ids;
	}
	
	/**
	 * Does this POV position include the given ID?
	 * @param id The ID to check
	 * @return Whether or not it was accepted
	 */
	public boolean accepts(int id) {
		for(int i : ids) if(i == id) return true;
		return false;
	}
	
	/**
	 * Does this POV position include {@link GenericHID HID Device}'s given POV switch's value?
	 * @param hid The {@link GenericHID HID Device}
	 * @param pov The POV switch
	 * @return Whether or not it was accepted
	 */
	public boolean accepts(int hid, int pov) { return accepts(DriverStation.getInstance().getStickPOV(hid, pov)); }
	
	/**
	 * Does this POV position include {@link GenericHID HID Device}'s given POV switch's value?
	 * @param hid The {@link GenericHID HID Device}
	 * @param pov The POV switch
	 * @return Whether or not it was accepted
	 */
	public boolean accepts(GenericHID hid, int pov) { return accepts(hid.getPort(), pov); }
	
	/**
	 * Does this POV position include {@link GenericHID HID Device}'s given zeroth POV switch's value?
	 * @param hid The {@link GenericHID HID Device}
	 * @return Whether or not it was accepted
	 */
	public boolean accepts(GenericHID hid) { return accepts(hid, 0); }
	
	/**
	 * Makes this into a {@link edu.wpi.first.wpilibj.buttons.Button Button}
	 * @param hid The {@link GenericHID HID Device} to use
	 * @return The {@link edu.wpi.first.wpilibj.buttons.Button Button} form of this, for the given {@link GenericHID HID Device}
	 */
	public POVButton createButton(GenericHID hid) { return new POVButton(hid, this); }
}