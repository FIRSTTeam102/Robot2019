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

package org.team102.robots.robot2019;

import org.team102.robots.robot2019.subsystems.SubsystemArm.ArmSetpoint;

public class ArmConfig {
	
	// CONFIGURE THIS BEFORE EVERY COMPETITION
	
	public static final int WRIST_VERTICAL = 0;
	public static final int WRIST_HORIZONTAL = -13;
	public static final int WRIST_ANGLED_DOWN = -23;
	
	public static final int ELBOW_FLOOR_CARGO = -3;
	
	public static final int ELBOW_ROCKET_HATCH_TOP = -17;
	public static final int ELBOW_ROCKET_HATCH_MIDDLE = -8;
	public static final int ELBOW_ROCKET_HATCH_BOTTOM = -1;
	
	public static final int ELBOW_ROCKET_CARGO_TOP = -21;
	public static final int ELBOW_ROCKET_CARGO_MIDDLE = -13;
	public static final int ELBOW_ROCKET_CARGO_BOTTOM = -4;
	
	public static final int ELBOW_CARGO_SHIP_CARGO = -13;
	public static final int ELBOW_CARGO_SHIP_OR_LOADING_STATION_HATCH = 0;
	
	// END PER-COMPETITION CONFIGURABLES
	
	public static final int NO_MOVE = -1;
	
	public static final int ELBOW_MARGIN_OF_ERROR = 2; // TODO configure this
	public static final int WRIST_MARGIN_OF_ERROR = 1; // TODO configure this
	
	public static final ArmSetpoint ROCKET_HATCH_TOP = new ArmSetpoint("Rocket: Hatch Top", false, ELBOW_ROCKET_HATCH_TOP, WRIST_VERTICAL);
	public static final ArmSetpoint ROCKET_HATCH_MIDDLE = new ArmSetpoint("Rocket: Hatch Middle", false, ELBOW_ROCKET_HATCH_MIDDLE, WRIST_VERTICAL);
	public static final ArmSetpoint ROCKET_HATCH_BOTTOM = new ArmSetpoint("Rocket: Hatch Bottom", true, ELBOW_ROCKET_HATCH_BOTTOM, WRIST_VERTICAL);
	public static final ArmSetpoint ROCKET_CARGO_TOP = new ArmSetpoint("Rocket: Cargo Top", false, ELBOW_ROCKET_CARGO_TOP, WRIST_HORIZONTAL);
	public static final ArmSetpoint ROCKET_CARGO_MIDDLE = new ArmSetpoint("Rocket: Cargo Middle", false, ELBOW_ROCKET_CARGO_MIDDLE, WRIST_HORIZONTAL);
	public static final ArmSetpoint ROCKET_CARGO_BOTTOM = new ArmSetpoint("Rocket: Cargo Bottom", false, ELBOW_ROCKET_CARGO_BOTTOM, WRIST_HORIZONTAL);
	
	public static final ArmSetpoint CARGO_SHIP_CARGO = new ArmSetpoint("Cargo Ship: Cargo", false, ELBOW_CARGO_SHIP_CARGO, WRIST_ANGLED_DOWN);
	public static final ArmSetpoint CARGO_SHIP_OR_LOADING_STATION_HATCH = new ArmSetpoint("Cargo Ship/Loading Station: Hatch", false, ELBOW_CARGO_SHIP_OR_LOADING_STATION_HATCH, WRIST_VERTICAL);
	
	public static final ArmSetpoint FLOOR_CARGO = new ArmSetpoint("Floor: Cargo", false, ELBOW_FLOOR_CARGO, WRIST_ANGLED_DOWN);
	
	public static final ArmSetpoint VERTICAL_VERTICAL = new ArmSetpoint("Vertical Wrist", false, NO_MOVE, WRIST_VERTICAL);
	public static final ArmSetpoint HORIZONTAL_WRIST = new ArmSetpoint("Horizontal Wrist", false, NO_MOVE, WRIST_HORIZONTAL);
}