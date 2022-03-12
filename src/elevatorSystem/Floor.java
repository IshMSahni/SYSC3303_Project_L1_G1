package elevatorSystem;
import java.io.*;
import java.net.*;
import java.util.*;
/**
 * Floor Class:
 * 	Class is an object to represent a floor
 */
public class Floor {
	
	/**
	 * Initial Class Variables
	 */
	private int floorNumber;	// Variable for floor's level
	private boolean isLast;		// Variable to ID top floor
	private boolean upButton;	// Variable for up button status
	private boolean downButton;	// Variable for down button status
	private int buttonStatus; // 0 = No button pressed, 1 = Up button pressed, 2 = Down button pressed
	private int[] lamps;		// Array for all lamps statuses
	private DatagramPacket sendPacket;
	private DatagramSocket sendSocket;
	
	/**
	 * Constructor:
	 * 	Floor constructor initializes all floor variables
	 * 	Note floor Number is not initialized due to how floor objects are created
	 */
	public Floor(int floorNumber, int totalElevatorNumber) {
		upButton = false;
		downButton = false;
		lamps = new int[totalElevatorNumber];
		this.floorNumber = floorNumber;
		this.isLast = false;
		this.buttonStatus = 0;

		try {
			this.sendSocket = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Method: setFloor
	 * 	Method sets floor number
	 * 	Args: (int) floorNumber
	 * 	Return: Void
	 */
	public void setFloor(int floorNumber) {
		this.floorNumber = floorNumber;
	}
	
	/**
	 * Method: getFloorNumber
	 * 	Method returns floor number
	 * 	Args: Null
	 * 	Return: int
	 */
    public int getFloorNumber() {
        return floorNumber;
    }
	
	/**
	 * Method: setFloor
	 * 	@Override
	 * 	Method sets floor number of top floor
	 * 	Args: (int) floorNumber, (boolean) is top floor
	 * 	Return: Void
	 */
	public void setFloor(int floorNumber, boolean last) {
		this.isLast = last;
		this.floorNumber = floorNumber;
	}
	
	/**
	 * Method: getUpButton
	 * 	Method returns up button status
	 * 	Args: Null
	 * 	Return: boolean
	 */
	public boolean getUpButton() {
		return upButton;
	}
	
	/**
	 * Method: getDownButton
	 * 	Method returns down button status
	 * 	Args: Null
	 * 	Return: boolean
	 */
	public boolean getDownButton() {
		return downButton;
	}
	
	/**
	 * Method: setUpButton
	 * 	Method sets up button status
	 * 	Args: (boolean) upButton
	 * 	Return: Void
	 */
	public void setUpButton(boolean upButton) {
		this.upButton = upButton;
	}
	
	/**
	 * Method: setDownButton
	 * 	Method sets down button status
	 * 	Args: (boolean) downButton
	 * 	Return: Void
	 */
	public void setDownButton(boolean downButton) {
		this.downButton = downButton;
	}
	
	/**
	 * Method: changeLamp
	 * 	Method sets a lamp's status
	 * 	Args: (int) elevator, (int) direction
	 * 	Return: Void
	 */
	public void changeLamp(int elevator, int direction) {
		lamps[elevator] = direction;
	}
	
	/**
	 * Method: printFloor
	 * 	Method displays a floor's data
	 * 	Args: Null
	 * 	Return: Void
	 */
	public void printFloor() {
		System.out.println();
		System.out.print("Floor Number: " + floorNumber);
		
		// if statements in case of ground/top floors
		if (floorNumber == 0) {
			System.out.println(" (Ground Floor)");
		} else if (isLast) {
			System.out.println(" (Top Floor)");
		} else {
			System.out.println();
		}
		
		// if statements in case of ground/top floors
		if (!isLast) {
			System.out.println("\tUp Button: " + upButton);
		} 
		if (floorNumber != 0) {
			System.out.println("\tDown Button: " + downButton);
		}
		
		// Logic displays all lamp data
		System.out.println("\tLamps:");
		for (int i = 0; i < lamps.length; i++) {
			if (lamps[i] == 0) {
				System.out.println("\t\tElevator " + i + ": Stopped");
			} else if (lamps[i] == 1) {
				System.out.println("\t\tElevator " + i + ": Going up");
			} else if (lamps[i] == 2) {
				System.out.println("\t\tElevator " + i + ": Going down");
			}
		}
	}

	/** Getter method for lamps */
	public int[] getLamp() {return lamps;}

	/** Method for when a button is pressed on a Floor */
	public void buttonPressed(int direction){

		if (direction == 1) {
			this.setUpButton(true);
			this.setDownButton(false);
		}
		else if (direction == 2) {
			this.setUpButton(false);
			this.setDownButton(true);
		}
		else{
			this.setUpButton(false);
			this.setDownButton(false);
		}

		byte data[] = new byte[3];
		data[0] = (byte) 2; data[1] = (byte) direction; data[2] = (byte) floorNumber;
		//create the datagram packet for the message with Port 10 (Scheduler)
		try {
			this.sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 10);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Send the data.
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
