package elevatorSystem;
// imports for scanning text file
import ElevatorStates.DoorClosed;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.*;
import java.net.*;
import java.util.*;

/** Elevator_System class, Simulates as a control system for Elevator Car objects */
public class Elevator_System implements Runnable{

	private boolean isSupport;		// Variable for breaking wait()
	private ElevatorCar[] elevators; //List of elevators
	private final double elevatorAcceleration = 0.9; // 0.9 meter per second square
	private final double elevatorTopSpeed = 2.7; // 2.7 meters per second
	private final long loadTime = 10; // 10 seconds is the average loading time
	private ArrayList<ElevatorState> states;

	private static Integer targetElevatorNumber;
	private DatagramPacket sendPacket, receivePacket; // UDP packets and sockets for send and recieving
	private DatagramSocket sendSocket, receiveSocket, movingSocket;

	/** Constructor for Elevator_System */
	public Elevator_System(int totalNumElevators, int totalNumFloors, boolean isSupport){
		this.isSupport = isSupport;
		targetElevatorNumber = 0;

		//Create elevators
		elevators = new ElevatorCar[totalNumElevators];
		for (int elevatorNumber = 0; elevatorNumber < totalNumElevators; elevatorNumber++) {
			elevators[elevatorNumber] = new ElevatorCar(elevatorNumber, totalNumFloors);

		}
		try {
			//Create and send and recieve socket.
			if(!isSupport) {
				receiveSocket = new DatagramSocket(20); // Elevator_System will recieve data on Port 20
				sendSocket = new DatagramSocket(41); //If data is recieved from Port 41 then it from Elevator_System
			}
			else {
				movingSocket = new DatagramSocket(45);
			}

		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	/** This method will wait until new data is received and then take the necessary actions based on that data*/
	public void elevatorRunning(){
		// Recieve new scheduled task data
		byte data[] = recieveData(receiveSocket);

		// Data is new scheduled Elevator Task
		if(data[0] == (byte) 0){
			System.out.println("New Scheduled Elevator Task recieved.");
			int elevatorNum = data[1];
			updateElevatorQueue(elevatorNum, data);
		}
		// Data is new scheduled Floor Task
		else if(data[0] == (byte) 2){
			System.out.println("New Scheduled Floor Task recieved.");
			int elevatorNum = data[1];
			updateElevatorQueue(elevatorNum, data);
		}
		//Elevator Position Request
		else if(data[0] == (byte) 1){
			this.sendElevatorsData();
		}

		// Elevator has arrived at a floor
		else if(data[0] == (byte) 7){
			this.updateQueue(data);
		}
	}

	/** Removes tasks from queue that are completed by Elevator after arriving at a floor*/
	public void updateQueue(byte[] data) {
		int elevatorNumber = data[1];
		int floorNumber = data[2];
		ArrayList<Integer> tasks = elevators[elevatorNumber].getTasks();
		while((tasks.size() != 0) && tasks.get(0) == floorNumber) {
			tasks.remove(0);
		}
		elevators[elevatorNumber].setTasks(tasks);
		this.sendData(data, 10);
	}

	/** A support method that will handle Elevator moving elevators */
	public void elevatorRunningSupport() {
		byte data[] = recieveData(movingSocket);
		if(data[0] == (byte) 0 || data[0] == (byte) 2) {
			int elevatorNumber = data[1];
			ArrayList<Integer> tasks = new ArrayList<>();
			for (int i = 2; i < data.length; i++) {
				int floorNumber = data[i];
				tasks.add(floorNumber);
			}

			//Update elevator tasks and move elevator.
			this.elevators[elevatorNumber].setTasks(tasks);
			targetElevatorNumber = elevatorNumber;
			moveElevator(elevatorNumber);
			loadElevator(elevatorNumber);
			if(data[0] == (byte) 0) {
				this.elevators[elevatorNumber].decrementPassengerCount();
			}
			else {
				this.elevators[elevatorNumber].incrementPassengerCount();
			}
		}
	}

	public byte[] recieveData(DatagramSocket receiveSocket) {
		// Construct a DatagramPacket for receiving packets for the data reply
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		try {
			// Wait until data reply is received via receiveSocket.
			receiveSocket.receive(receivePacket);
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Process the received datagram.
		int len = receivePacket.getLength();
		byte tempData[] = new byte[len];
		for(int i = 0; i < len; i++) {
			tempData[i] = data[i];
		}
		data = tempData;

		return data;
	}

	/** This method will update elevator scheduled queue*/
	public synchronized void updateElevatorQueue(int elevatorNumber, byte data[]){
		//Reconstruct the scheduled tasks as an ArrayList from the data
		ArrayList<Integer> tasks = new ArrayList<>();
		for (int i = 2; i < data.length; i++) {
			int floorNumber = data[i];
			tasks.add(floorNumber);
		}

		//Update elevator tasks and move elevator.
		this.elevators[elevatorNumber].setTasks(tasks);
		targetElevatorNumber = elevatorNumber;
		this.sendData(data,45);
	}

	/** Method to move elevator */
	public synchronized void moveElevator(Integer elevatorNumber){
		//Calculate Time to move elevator
		ElevatorCar elevator = elevators[elevatorNumber];
		Float startLocation = elevator.getPosition();
		Integer endLocation = elevator.getTasks().get(0);
		long time = calculateTime(startLocation,endLocation);

		//Elevator state methods
		elevators[elevatorNumber].moveElevator(time);
		elevators[elevatorNumber].elevatorArrived();
		elevators[elevatorNumber].openDoor();
	}

	/** Method to calculate time in milliseconds to move Elevator a given amount of distance */
	public long calculateTime(Float startLocation, Integer endLocation){
		double time;
		double netDistance = Math.abs(startLocation - endLocation) * 3.0;
		double inflectionDistance = (elevatorTopSpeed*elevatorTopSpeed)/elevatorAcceleration;

		//If net distance between floors allows elevator  to reach top speed, then use 1st formula, else use 2nd formula.
		if(netDistance > inflectionDistance){
			time = ((netDistance - inflectionDistance) / elevatorTopSpeed) + (Math.sqrt(inflectionDistance) * 2);
		}
		else{
			time = Math.sqrt(netDistance) * 2;
		}
		//Return time converted to long type after rounding.
		//Multiply 1000 because to convert time from seconds to milliseconds
		return (Math.round(time) * 1000);
	}

	/** Method to simulate loading elevator waiting time */
	public synchronized void loadElevator(Integer elevatorNumber){
		elevators[elevatorNumber].loadElevator(loadTime*1000);
		elevators[elevatorNumber].closeDoor();
	}

	public ElevatorCar[] getElevators(){return this.elevators;}

	/** Method to convert all elevator positions to byte[] format and send to Scheduler*/
	public void sendElevatorsData(){
		byte data[] = new byte[elevators.length + 1];
		data[0] = (byte) 4;
		for (int i = 1; i < data.length; i++) {
			data[i] = (byte) Math.round(elevators[i-1].getPosition());
		}
		this.sendData(data, 40);
	}

	/** This method will send scheduled task data to Elevator_System*/
	public void sendData(byte data[], int portNumber){
		//create the datagram packet for the message with Port given
		try {
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), portNumber);
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

	/** Run method for Elevator_System */
	@Override
	public synchronized void run() {
		while (true) {
			//Wait until event occurs for elevator
			if(this.isSupport) {
				this.elevatorRunningSupport();
			}
			else {
				this.elevatorRunning();
			}
		}
	}

	/** Main method*/
	public static void main(String[] args) {
		int totalNumElevators = 3;
		int totalNumFloors = 10;
		Elevator_System elevator_system_main = new Elevator_System(totalNumElevators,totalNumFloors, false);
		Elevator_System elevator_system_support = new Elevator_System(totalNumElevators,totalNumFloors, true);
		Thread elevatorSystemMainThread = new Thread(elevator_system_main, "Scheduler Simulation");
		Thread elevatorSystemSupportThread = new Thread(elevator_system_support, "Scheduler Simulation");
		System.out.println("Waiting for new Task...");

		elevatorSystemMainThread.start();
		elevatorSystemSupportThread.start();
	}
}
