package com.jacobacon.serialsign;

import java.math.BigInteger;
import java.nio.charset.Charset;

import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialSign {

	public static void main(String[] args) {
		String port = "COM3";
		SerialPort serialPort = new SerialPort(port);

		String message = "Dad is a dork!";

		byte[] bytes = new byte[message.length() + 17];

		try {
			// Write the actual data
			System.out.println("Port Opened: " + serialPort.openPort()); // Open
																			// Port
																			// Again
			System.out.println("Params setted: "
					+ serialPort.setParams(2400, 8, 1, 0)); // Set Params
			System.out.println("Purge Ports: "
					+ serialPort.purgePort(SerialPort.PURGE_TXABORT)); // Purge
																		// old
																		// data.
			System.out.println("Purge Ports: "
					+ serialPort.purgePort(SerialPort.PURGE_RXABORT));
			System.out.println("Purge Ports: "
					+ serialPort.purgePort(SerialPort.PURGE_RXCLEAR));

			bytes[0] = 0; // 00
			bytes[1] = (byte) 255; // FF
			bytes[2] = (byte) 255; // FF
			bytes[3] = 0; // 00
			bytes[4] = 11; // 0B
			bytes[5] = 1; // 01
			bytes[6] = (byte) 255; // FF
			bytes[7] = 1; // 01
			bytes[8] = 48; // 30
			bytes[9] = 49; // 31
			bytes[10] = (byte) 239; // EF
			bytes[11] = (byte) 176; // B0
			bytes[12] = (byte) 239; // EF
			bytes[13] = (byte) 162; // A2

			byte[] messageArray = new byte[message.length()];

			for (int i = 0; i < messageArray.length; i++) {
				messageArray[i] = (byte) message.charAt(i);
			}

			int value = 0;
			for (int i = 14; i < bytes.length - 3; i++) {
				bytes[i] = messageArray[value];
				value++;
			}

			// End Message
			bytes[bytes.length - 3] = (byte) 255;
			bytes[bytes.length - 2] = (byte) 255;
			bytes[bytes.length - 1] = 0;

			System.out
					.println("\"Hello World!!!\" successfully writen to port: "
							+ serialPort.writeBytes(bytes));
			System.out.println("Port closed: " + serialPort.closePort());

			System.out.println("Bytes: " + bytes.length);
			System.out.println("Message Length: " + message.length());

			for (int i = 0; i < bytes.length; i++) {
				System.out.println(bytes[i]);
			}

		} catch (SerialPortException e) {
			System.out.println(e);
		}
	}

	public static String toHex(String word) {
		return String.format("%x",
				new BigInteger(1, word.getBytes(Charset.defaultCharset())));
	}

	public static int toDigit(char c) {
		int value = c;
		return value;
	}

}
