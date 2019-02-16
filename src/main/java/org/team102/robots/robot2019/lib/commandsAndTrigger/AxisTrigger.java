package org.team102.robots.robot2019.lib.commandsAndTrigger;

import java.util.function.Predicate;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * A {@link edu.wpi.first.wpilibj.buttons.Trigger Trigger} that is tripped when an axis on a {@link GenericHID HID device} is accepted by the given {@link Predicate selector}
 */
public class AxisTrigger extends Trigger {
	/** {@link Predicate Selector} for the values of the axis */
	private final Predicate<Double> valueSelector;
	/** What HID device and axis */
	private final int channel, axis;
	
	/**
	 * Creates the trigger
	 * @param hid The {@link GenericHID HID device}
	 * @param axis The axis
	 * @param valueSelector {@link Predicate Selector} for the values of the axis
	 */
	public AxisTrigger(GenericHID hid, int axis, Predicate<Double> valueSelector) { this(hid.getPort(), axis, valueSelector); }
	
	/**
	 * Creates the trigger
	 * @param channel The {@link GenericHID HID device channel}
	 * @param axis The axis
	 * @param valueSelector {@link Predicate Selector} for the values of the axis
	 */
	public AxisTrigger(int channel, int axis, Predicate<Double> valueSelector) { this.channel = channel; this.axis = axis; this.valueSelector = valueSelector; }
	
	public boolean get() {
		double val = DriverStation.getInstance().getStickAxis(channel, axis);
		return valueSelector.test(val);
	}
	
	/**
	 * Creates a trigger that returns true for values such that {@code max > val > min}
	 * @param channel The {@link GenericHID HID device channel}
	 * @param axis The axis
	 * @param min The min. value
	 * @param max The max. value
	 * @return The trigger
	 */
	public static AxisTrigger forRange(int channel, int axis, double min, double max) { return new AxisTrigger(channel, axis, i -> i > min && i < max); }
	
	/**
	 * Creates a trigger that returns true for values such that {@code max > val}
	 * @param channel The {@link GenericHID HID device channel}
	 * @param axis The axis
	 * @param max The max. value
	 * @return The trigger
	 */
	public static AxisTrigger forLessThan(int channel, int axis, double max) { return new AxisTrigger(channel, axis, i -> i < max); }
	
	/**
	 * Creates a trigger that returns true for values such that {@code val > min}
	 * @param channel The {@link GenericHID HID device channel}
	 * @param axis The axis
	 * @param min The min. value
	 * @return The trigger
	 */
	public static AxisTrigger forGreaterThan(int channel, int axis, double min) { return new AxisTrigger(channel, axis, i -> i > min); }
	
	/**
	 * Creates a trigger that returns true for values such that {@code val == eq}<br>
	 * <b>Warning: This uses </b>{@code double}<b>s, where getting an exact equals is rare.</b>
	 * @param channel The {@link GenericHID HID device channel}
	 * @param axis The axis
	 * @param eq The value to equal to
	 * @return The trigger
	 */
	public static AxisTrigger forEqualTo(int channel, int axis, double eq) { return new AxisTrigger(channel, axis, ((Double)eq)::equals); }
	
	/**
	 * Creates a trigger that returns true for values such that {@code max > val > min}
	 * @param hid The {@link GenericHID HID device}
	 * @param axis The axis
	 * @param min The min. value
	 * @param max The max. value
	 * @return The trigger
	 */
	public static AxisTrigger forRange(GenericHID hid, int axis, double min, double max) { return forRange(hid.getPort(), axis, min, max); }
	
	/**
	 * Creates a trigger that returns true for values such that {@code max > val}
	 * @param hid The {@link GenericHID HID device}
	 * @param axis The axis
	 * @param max The max. value
	 * @return The trigger
	 */
	public static AxisTrigger forLessThan(GenericHID hid, int axis, double max) { return forLessThan(hid.getPort(), axis, max); }
	
	/**
	 * Creates a trigger that returns true for values such that {@code val > min}
	 * @param hid The {@link GenericHID HID device}
	 * @param axis The axis
	 * @param min The min. value
	 * @return The trigger
	 */
	public static AxisTrigger forGreaterThan(GenericHID hid, int axis, double min) { return forGreaterThan(hid.getPort(), axis, min); }
	
	/**
	 * Creates a trigger that returns true for values such that {@code val == eq}<br>
	 * <b>Warning: This uses </b>{@code double}<b>s, where getting an exact equals is rare.</b>
	 * @param hid The {@link GenericHID HID device}
	 * @param axis The axis
	 * @param eq The value to equal to
	 * @return The trigger
	 */
	public static AxisTrigger forEqualTo(GenericHID hid, int axis, double eq) { return forEqualTo(hid.getPort(), axis, eq); }
}