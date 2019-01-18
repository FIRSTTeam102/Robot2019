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

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;

/**
 * An object-oriented representation of a series of {@link Rumble rumbles} to be played in order, for a certain ammount of time
 */
public class RumbleSeries {
	/** The {@link Rumble rumbles} in order */
	private ArrayList<Rumble> rumbles = new ArrayList<Rumble>();
	/** The amount of time to play each rumble for */
	private ArrayList<Float> times = new ArrayList<Float>();
	
	/**
	 * Adds a {@link Rumble} to the series, and returns itself for ease of constructing
	 * @param rumble The {@link Rumble} to add
	 * @param time How long to add it for, in seconds
	 * @return itself, for ease of construction
	 */
	public RumbleSeries addRumble(Rumble rumble, float time) {
		rumbles.add(rumble);
		times.add(time);
		
		return this;
	}
	
	/**
	 * Adds a {@link Rumble#SOFT soft} {@link Rumble} for the specified amount of time
	 * @param time How long to add it for, in seconds
	 * @return itself, for ease of construction
	 */
	public RumbleSeries addSoft(float time) { return addRumble(Rumble.SOFT, time); }
	
	/**
	 * Adds a {@link Rumble#ROUGH rough} {@link Rumble} for the specified amount of time
	 * @param time How long to add it for, in seconds
	 * @return itself, for ease of construction
	 */
	public RumbleSeries addRough(float time) { return addRumble(Rumble.ROUGH, time); }
	
	/**
	 * Adds a {@link Rumble#BOTH both} {@link Rumble} for the specified amount of time
	 * @param time How long to add it for, in seconds
	 * @return itself, for ease of construction
	 */
	public RumbleSeries addBoth(float time) { return addRumble(Rumble.BOTH, time); }
	
	/**
	 * Adds a {@link Rumble#BREAK gap} for the specified amount of time
	 * @param time How long to add it for, in seconds
	 * @return itself, for ease of construction
	 */
	public RumbleSeries addBreak(float time) { return addRumble(Rumble.BREAK, time); }
	
	/**
	 * Plays the rumble to the specified {@link GenericHID joystick}, asynchronously
	 * @param joystick The {@link GenericHID joystick}
	 */
	public void play(GenericHID joystick) { play(joystick, true); }
	
	/**
	 * Plays the rumble to the specified {@link GenericHID joystick}, optionally asynchronously
	 * @param joystick The {@link GenericHID joystick}
	 * @param async Whether or not to play asynchronously
	 */
	public void play(GenericHID joystick, boolean async) {
		Runnable run = () -> {
			try {
				for(int i = 0; i < rumbles.size(); i++) {
					rumbles.get(i).play(joystick);
					Timer.delay(times.get(i));
				}
				
				Rumble.BREAK.play(joystick);
			} catch(Exception e) {
				DriverStation.reportError("Error whilst playing rumble: " + e.getLocalizedMessage(), e.getStackTrace());
			}
		};
		
		if(async) {
			Thread daemon = new Thread(run, "Rumbler daemon: " + hashCode() + " : " + System.nanoTime());
			daemon.setDaemon(true);
			daemon.start();
		} else {
			run.run();
		}
	}
	
	/**
	 * @return How long it would take to play this {@link RumbleSeries}
	 */
	public float getTotalTime() {
		float time = 0;
		for(Float d : times) time += d;
		
		return time;
	}
	
	/**
	 * The available types of rumble
	 */
	public static enum Rumble {
		/** What would normally be a soft {@link Rumble}, on the {@link GenericHID.RumbleType#kRightRumble right} side */
		SOFT(true, false),
		
		/** What would normally be a rough {@link Rumble}, on the {@link GenericHID.RumbleType#kLeftRumble left} side */
		ROUGH(false, true),
		
		/** Both a {@link #SOFT soft} and a {@link #ROUGH rough} {@link Rumble} at the same time */
		BOTH(true, true),
		
		/** A period of time without a {@link Rumble} */
		BREAK(false, false);
		
		/** Whether to {@link Rumble rumble} on the {@link #SOFT soft} side */
		private final boolean soft;
		
		/** Whether to {@link Rumble rumble} on the {@link #ROUGH rough} side */
		private final boolean rough;
		
		/**
		 * Enum constructor
		 * @param soft Whether to {@link Rumble rumble} on the {@link #SOFT soft} side 
		 * @param rough Whether to {@link Rumble rumble} on the {@link #ROUGH rough} side
		 */
		private Rumble(boolean soft, boolean rough) { this.soft = soft; this.rough = rough; }
		
		/**
		 * Plays this {@link Rumble rumble type} to the provided {@link GenericHID joystick}
		 * @param joystick The {@link GenericHID joystick}
		 */
		public void play(GenericHID joystick) {
			joystick.setRumble(GenericHID.RumbleType.kRightRumble, soft ? 1 : 0);
			joystick.setRumble(GenericHID.RumbleType.kLeftRumble, rough ? 1 : 0);
		}
	}
}