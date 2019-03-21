package org.team102.robots.robot2019;

import org.team102.robots.robot2019.subsystems.SubsystemArm.ArmSetpoint;

public class ArmConfig {
	
	// CONFIGURE THIS BEFORE EVERY COMPETITION
	
	public static final int WRIST_VERTICAL = 0;
	public static final int WRIST_HORIZONTAL = -13;
	public static final int WRIST_ANGLED_DOWN = -23;
	
	public static final int ELBOW_FLOOR_CARGO = 16;
	
	public static final int ELBOW_ROCKET_HATCH_TOP = 34;
	public static final int ELBOW_ROCKET_HATCH_MIDDLE = 24;
	public static final int ELBOW_ROCKET_HATCH_BOTTOM = 16;
	
	public static final int ELBOW_ROCKET_CARGO_TOP = 41;
	public static final int ELBOW_ROCKET_CARGO_MIDDLE = 27;
	public static final int ELBOW_ROCKET_CARGO_BOTTOM = 20;
	
	public static final int ELBOW_CARGO_SHIP_CARGO = 29;
	public static final int ELBOW_CARGO_SHIP_OR_LOADING_STATION_HATCH = 14;
	
	public static final int ELBOW_FLOOR_HATCH_ABOVE = -1; // TODO configure this
	public static final int ELBOW_FLOOR_HATCH = -1; // TODO configure this
	
	// END PER-COMPETITION CONFIGURABLES
	
	public static final int NO_MOVE = -1;
	
	public static final int ELBOW_MARGIN_OF_ERROR = 2; // TODO configure this
	public static final int WRIST_MARGIN_OF_ERROR = 2; // TODO configure this
	
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