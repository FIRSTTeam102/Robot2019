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

import java.io.Closeable;
import java.util.ArrayList;
import java.util.function.Consumer;

import com.fazecast.jSerialComm.SerialPort;

/**
 * A connection to an Arduino via a serial port
 */
public class ArduinoConnection implements Closeable, AutoCloseable {
	private String eol = "\n";
	
	private SerialPort port;
	private String dataBuff = "";
	
	private ArrayList<String> lines = new ArrayList<>();
	private Consumer<String> lineListener = null;
	
	/**
	 * Create the connection
	 * @param portID The name of the serial port, i.e. {@code /dev/tty0}
	 */
	public ArduinoConnection(String portID) {
		try {
			port = SerialPort.getCommPort(portID);
			port.setBaudRate(9600);
			port.setParity(SerialPort.NO_PARITY);
			port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
			port.setNumStopBits(1);
			port.setNumDataBits(8);
			port.openPort();
		} catch(Exception e) {
			if(port.isOpen()) {
				System.out.println("Something wrong may have happened whilst opening serial port " + portID + ", but if everything still works fine, it shouldn't matter");
			} else {
				throw new IllegalStateException("Failed to open port", e);
			}
		}
	}
	
	@Override
	public void close() {
		port.closePort();
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
	 * Writes the given line of text to the Arduino, then a new-line character
	 * @param line The line
	 */
	public void write(String line) {
		byte[] buff = (line + eol).getBytes();
		port.writeBytes(buff, buff.length);
	}
	
	/**
	 * Polls the serial port for new data from the Arduino
	 */
	public void update() {
		int bytesAvailable = port.bytesAvailable();
		
		if(bytesAvailable > 0) {
			byte[] buff = new byte[bytesAvailable];
			port.readBytes(buff, bytesAvailable);
			
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
					if(lineListener == null) {
						lines.add(tok);
					} else {
						lineListener.accept(tok);
					}
				}
			}
		}
	}
}