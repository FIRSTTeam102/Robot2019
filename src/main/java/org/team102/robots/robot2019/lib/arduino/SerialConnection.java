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
import java.util.ArrayList;
import java.util.Locale;

import com.fazecast.jSerialComm.SerialPort;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * A serial connection which works both on the robot and in the sim
 */
public class SerialConnection extends SendableBase implements Closeable, AutoCloseable {
	public static ArrayList<String> serialPortPrefixes = new ArrayList<>();
	
	static {
		serialPortPrefixes.add("COM");
		serialPortPrefixes.add("tty");
	}
	
	private final SerialPort port;
	private String dataBuff = "";
	
	private static String getListSerialPortsCommand() {
		if(System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("window")) {
			return System.getenv("windir") + "\\System32\\mode.com";
		} else {
			return "dmesg";
		}
	}
	
	/**
	 * Attempt to locate all connected serial ports
	 * @return All the located ports
	 */
	public static String[] findSerialPorts() {
		ArrayList<String> ports = new ArrayList<>();
		
		try {
			Process proc = Runtime.getRuntime().exec(getListSerialPortsCommand());
			InputStream stream = proc.getInputStream();
			String input = "";
			
			while(proc.isAlive() || stream.available() > 0) {
				input += (char)stream.read();
			}
			
			input.replace("\n", " ");
			
			for(String token : input.split("\\s+")) {
				for(String prefix : serialPortPrefixes) {
					if(token.startsWith(prefix)) {
						if(token.endsWith(":")) {
							token = token.substring(0, token.length() - 1);
						}
						
						if(token.startsWith("tty")) {
							token = ("/dev/" + token);
						}
						
						if(!ports.contains(token)) {
							ports.add(token);
						}
					}
				}
			}
		} catch(Exception e) {
			System.err.println("Failed to list serial ports: ");
			e.printStackTrace();
		}
		
		return ports.toArray(new String[ports.size()]);
	}
	
	/**
	 * Create the serial connection to the given port
	 * @param portID The device name of the port
	 * @param baud The baud rate
	 * @param parity The parity (0 = none, 1 = odd, 2 = even)
	 * @param flowControl The flow control (see documentation for the {@link SerialPort jSerialComm port class}
	 * @param stopBits The number of stop bits
	 * @param dataBits The number of data bits
	 */
	public SerialConnection(String portID, int baud, int parity, int flowControl, int stopBits, int dataBits) {
		port = SerialPort.getCommPort(portID);
		port.setBaudRate(baud);
		port.setParity(parity);
		port.setFlowControl(flowControl);
		port.setNumStopBits(stopBits);
		port.setNumDataBits(dataBits);
		port.openPort();
		
		// Magic sleep time to make sure we can actually write some data
		Timer.delay(1.75);
	}
	
	/**
	 * Polls the serial port for new data
	 */
	public void update() {
		int bytesAvailable = port.bytesAvailable();
		
		if(bytesAvailable > 0) {
			byte[] buff = new byte[bytesAvailable];
			port.readBytes(buff, bytesAvailable);
			dataBuff += new String(buff);
		}
	}
	
	@Override
	public void initSendable(SendableBuilder builder) {
		builder.addBooleanProperty("serialPortConnected", port::isOpen, null);
		builder.addStringProperty("serialPortID", port::getSystemPortName, null);
	}
	
	/**
	 * This does the same thing as {@link #close()}
	 */
	@Override
	@Deprecated
	public void free() {
		super.free();
		close();
	}
	
	@Override
	public void close() {
		port.closePort();
	}
	
	/**
	 * Writes the given data to the Serial port
	 * @param data The data
	 */
	public void write(byte[] data) {
		port.writeBytes(data, data.length);
	}
	
	/**
	 * Gives back whatever data has been read so far
	 * @return The data, as text
	 */
	public String getReadData() {
		return dataBuff;
	}
	
	/**
	 * Sets the read data
	 * @param dataBuff The data, as text
	 */
	public void setReadData(String dataBuff) {
		this.dataBuff = dataBuff;
	}
	
	/**
	 * Clears the read data
	 */
	public void clearReadData() {
		setReadData("");
	}
}