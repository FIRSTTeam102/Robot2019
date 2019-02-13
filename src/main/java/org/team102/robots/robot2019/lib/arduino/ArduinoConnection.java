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

package org.team102.robots.robot2019.lib.arduino;

import java.io.Closeable;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.function.Consumer;

import org.team102.robots.robot2019.Robot;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * A connection to an Arduino via a serial port
 */
public class ArduinoConnection extends SendableBase implements Closeable, AutoCloseable {
	public static ArrayList<String> serialPortPrefixes = new ArrayList<>();
	private static HashMap<String, ArduinoConnection> locatedArduinos = new HashMap<>();
	
	static {
		serialPortPrefixes.add("COM");
		serialPortPrefixes.add("tty");
	}
	
	private String eol = "" + (char)10;
	
	private SerialImpl portImpl;
	private String dataBuff = "";
	
	private ArrayList<String> lines = new ArrayList<>();
	private Consumer<String> lineListener = null;
	
	private static String getListSerialPortsCommand() {
		if(System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("window")) {
			return System.getenv("windir") + "\\System32\\mode.com";
		} else {
			return "dmesg";
		}
	}
	
	/**
	 * Performs a WHOIS lookup on however many serial ports
	 * @param numSerialPortsToSearch How many serial ports to search
	 */
	public static void findArduinos() {
		System.out.println("Finding Arduinos...");
		
		try {
			Process proc = Runtime.getRuntime().exec(getListSerialPortsCommand());
			InputStream stream = proc.getInputStream();
			String input = "";
			
			while(proc.isAlive() || stream.available() > 0) {
				input += (char)stream.read();
			}
			
			input.replace("\n", " ");
			String port = null;
			
			outer:
			for(String token : input.split("\\s+")) {
				for(String prefix : serialPortPrefixes) {
					if(token.startsWith(prefix)) {
						port = token;
						break outer;
					}
				}
			}
			
			if(port != null) {
				if(port.endsWith(":")) {
					port = port.substring(0, port.length() - 1);
				}
				
				if(port.startsWith("tty")) {
					port = ("/dev/" + port);
				}
				
				System.out.print("On serial port \"" + port + "\": ");
				
				String name = findArduino(port);
				if(name == null) {
					System.out.println("No Arduino.");
				} else {
					System.out.println("Found Arduino \"" + name + "\".");
				}
			}
		} catch(Exception e) {
			System.err.println("Failed to list serial ports: ");
			e.printStackTrace();
		}
	}
	
	private static String findArduino(String port) {
		ArduinoConnection conn = new ArduinoConnection(port);
		
		conn.write("WHOIS");
		Timer.delay(.75); // Allow time for a reply
		
		conn.update();
		
		String reply = null;
		if(conn.hasLines()) {
			for(String str = conn.getLine(); str != null; str = conn.getLine()) {
				if(str.toUpperCase(Locale.ROOT).startsWith("IAM ")) {
					reply = str.substring(4);
					break;
				}
			}
		}
		
		if(reply == null) {
			conn.close();
			return null;
		} else {
			locatedArduinos.put(reply, conn);
			return reply;
		}
	}
	
	/**
	 * Look up an Arduino by WHOIS response
	 * @param who The response that the Arduino you want would give
	 * @return The Arduino, or null if none is found
	 */
	public static ArduinoConnection lookUpByWhois(String who) {
		for(String name : locatedArduinos.keySet()) {
			if(name.equalsIgnoreCase(who)) return locatedArduinos.get(name);
		}
		
		return null;
	}
	
	/**
	 * Create the connection<br>
	 * <strong>It is not recommended to use this directly.</strong><br>
	 * Instead, look up the Arduino by WHOIS response, using {@link #lookUpByWhois(String)}.
	 * @param portID The name of the serial port, i.e. {@code /dev/tty0}
	 */
	public ArduinoConnection(String portID) {
		if(Robot.isReal()) {
			portImpl = new RioSerialImpl(portID);
		} else {
			portImpl = new DesktopSerialImpl(portID);
		}
		
		// Magic sleep time to make sure we can actually write some data
		Timer.delay(1.75);
	}
	
	/**
	 * This does the same thing as {@link #close()}
	 */
	@Override
	public void free() {
		close();
	}
	
	@Override
	public void close() {
		portImpl.close();
	}
	
	/**
	 * Gives back the currently-used end-of-line marker, which is used for parsing and writing lines
	 * @return The current end-of-line marker
	 */
	public String getEOL() {
		return eol;
	}
	
	/**
	 * Sets the end-of-line marker used for parsing and writing lines
	 * @param eol The new end-of-line marker
	 */
	public void setEOL(String eol) {
		this.eol = eol;
	}
	
	/**
	 * Gives back whether or not there are more lines to be manually queried from the Arduino.
	 * <strong>You can only query the lines manually if the {@link #setLineListener(Consumer) line listener} is null.</strong>
	 * @return Whether or not there are any lines available to be manually queried
	 */
	public boolean hasLines() {
		return !lines.isEmpty();
	}
	
	/**
	 * Gives back the oldest available line (FIFO) from the Arduino.
	 * <strong>You can only query the lines manually if the {@link #setLineListener(Consumer) line listener} is null.</strong>
	 * @return The oldest available line, or {@code null} if none are available
	 */
	public String getLine() {
		if(!hasLines()) {
			return null;
		} else {
			return lines.remove(0);
		}
	}
	
	/**
	 * Sets the listener for new lines of data from the Arduino.
	 * <strong>You can only query the lines manually if this is null.</strong>
	 * @param lineListener The listener
	 */
	public void setLineListener(Consumer<String> lineListener) {
		this.lineListener = lineListener;
	}
	
	/**
	 * Gets whether or not a {@link #setLineListener(Consumer) line listener} is currently attached
	 * @return Whether or not a line listener is attached
	 */
	public boolean hasLineListener() {
		return lineListener != null;
	}
	
	/**
	 * Writes the given line of text to the Arduino, then a new-line character
	 * @param line The line
	 */
	public void write(String line) {
		byte[] buff = (line + eol).getBytes(Charset.forName("ASCII"));
		portImpl.write(buff);
	}
	
	/**
	 * Polls the serial port for new data from the Arduino
	 */
	public void update() {
		int bytesAvailable = portImpl.bytesAvailable();
		
		if(bytesAvailable > 0) {
			//byte[] buff = new byte[bytesAvailable];
			//port.readBytes(buff, bytesAvailable);
			byte[] buff = portImpl.read(bytesAvailable);
			
			dataBuff += new String(buff);
		}
		
		if(dataBuff.contains(eol)) {
			boolean useLastToken = dataBuff.endsWith(eol);
			String[] tokens = dataBuff.split(eol);
			int max = tokens.length - 1;
			
			if(useLastToken) {
				dataBuff = "";
			} else {
				dataBuff = tokens[max];
				max--;
			}
			
			for(int i = 0; i <= max; i++) {
				String tok = tokens[i].trim();
				
				if(tok.length() > 0) {
					if(hasLineListener()) {
						lineListener.accept(tok);
					} else {
						lines.add(tok);
					}
				}
			}
		}
	}
	
	@Override
	public void initSendable(SendableBuilder builder) {
		builder.addBooleanProperty("serialPortConnected", portImpl::isOpen, null);
		builder.addDoubleProperty("numUnprocessedLines", lines::size, null);
		builder.addBooleanProperty("hasLineListener", this::hasLineListener, null);
		builder.addStringProperty("serialPortID", portImpl::getName, null);
	}
	
	static interface SerialImpl {
		boolean isOpen();
		String getName();
		
		int bytesAvailable();
		
		void write(byte[] data);
		byte[] read(int len);
		
		void close();
	}
}