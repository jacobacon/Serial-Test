package com.jacobacon.serialsign;

import java.util.Scanner;

import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialSign {

	public static void main(String[] args) {
		String port = "COM3";
		String message = "Hello!";

		SerialPort serialPort = new SerialPort(port);
		Scanner scan = new Scanner(System.in);

		System.out.println("Message: ");
		message = scan.nextLine();

		byte[] bytes = new byte[message.length() + 17];

		writeMessage(message, serialPort, bytes);

	}

	public static void writeMessage(String message, SerialPort serialPort,
			byte[] bytes) {
		try {
			// Write the actual data

			serialPort.openPort(); // Open Port
			serialPort.setParams(2400, 8, 1, 0); // Set Params

			// The Following is NEEDED in order to notify the sign of the
			// incoming message. DO NOT EDIT

			bytes[0] = 0; // 00
			bytes[1] = (byte) 255; // FF
			bytes[2] = (byte) 255; // FF
			bytes[3] = 0; // 00
			bytes[4] = 11; // 0Bmmm
			bytes[5] = 1; // 01
			bytes[6] = (byte) 255; // FF
			bytes[7] = 1; // 01
			bytes[8] = 48; // 30
			bytes[9] = 49; // 31
			bytes[10] = (byte) 239; // EF
			bytes[11] = (byte) 176; // B0
			bytes[12] = (byte) 239; // EF
			bytes[13] = (byte) 162; // A2

			// The Above is NEEDED in order to notify the sign of the incoming
			// message. DO NOT EDIT

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

			// The Following is NEEDED in order to notify the sign of the
			// incoming message. DO NOT EDIT
			bytes[bytes.length - 3] = (byte) 255;
			bytes[bytes.length - 2] = (byte) 255;
			bytes[bytes.length - 1] = 0;
			// The Above is NEEDED in order to notify the sign of the incoming
			// message. DO NOT EDIT

			serialPort.writeBytes(bytes);
			serialPort.closePort();

		} catch (SerialPortException e) {
			System.out.println(e);
		}
	}

}
