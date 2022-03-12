package elevatorSystem;
// imports for scanning text file
import ElevatorStates.DoorClosed;
import java.io.*;
import java.net.*;
import java.util.*;

/** Elevator_System class, Simulates as a control system for Elevator Car objects */
public class Elevator_System implements Runnable{

	private static boolean isEvent;		// Variable for breaking wait()
	private ElevatorCar[] elevators; //List of elevators
	private final double elevatorAcceleration = 0.9; // 0.9 meter per second square
	private final double elevatorTopSpeed = 2.7; // 2.7 meters per second
	private final long loadTime = 10; // 10 seconds is the average loading time
	private ArrayList<ElevatorState> states;

	private static Integer targetElevatorNumber;
	private DatagramPacket sendPacket, receivePacket; // UDP packets and sockets for send and recieving
	private DatagramSocket sendSocket, receiveSocket;

	/** Constructor for Elevator_System */
	public Elevator_System(int totalNumElevators, int totalNumFloors){
		isEvent = false;
		targetElevatorNumber = 0;

		//Create elevators
		elevators = new ElevatorCar[totalNumElevators];
		for (int elevatorNumber = 0; elevatorNumber < totalNumElevators; elevatorNumber++) {
			elevators[elevatorNumber] = new ElevatorCar(elevatorNumber, totalNumFloors);
		}

		try {
			//Create and send and recieve socket.
			receiveSocket = new DatagramSocket(20); // Elevator_System will recieve data on Port 20
			sendSocket = new DatagramSocket(41); //If data is recieved from Port 41 then it from Elevator_System

		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	/** This method will wait until new data is received and then take the necessary actions based on that data*/
	public void elevatorRunning(){
		// Construct a DatagramPacket for receiving packets for the data reply
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);

		try {
			// Wait until data reply is received via receiveSocket.
			System.out.println("Elevator_System: Waiting for New Task......");
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
		int portRecievedFrom = receivePacket.getPort();

		System.out.println("Elevator_System: Task received:");
		System.out.println("From: " + receivePacket.getAddress());
		System.out.println("Port: " + portRecievedFrom);
		System.out.println("Length: " + len);
		System.out.print("Containing: ");

		// Form a String from the byte array.
		String received = new String(data,0,len);
		System.out.println("String: " + received);
		System.out.println("Bytes: " + Arrays.toString(data) + "\n");

		// Data is new scheduled Task
		if(data[0] == (byte) 0){
			int elevatorNum = data[1];
			updateElevatorQueue(elevatorNum, data);
		}
		// Data is elevator positions request
		else if(data[0] == (byte) 1){
			this.sendElevatorsData();
		}
		//TODO Data is elevator button is pressed
		else if(data[0] == (byte) 2){

		}
		//TODO Data is elevator has arrived at a floor
		else{

		}
	}

	/** This method will update elevator scheduled queue*/
	public synchronized void updateElevatorQueue(int elevatorNumber, byte data[]){
		//Reconstruct the scheduled tasks as an ArrayList from the data
		ArrayList<Integer> tasks = new ArrayList<>();
		for (int i = 1; i < data.length; i++) {
			int floorNumber = data[i];
			tasks.add(floorNumber);
		}

		//Update elevator tasks and move elevator.
		this.elevators[elevatorNumber].setTasks(tasks);
		isEvent = true;
		targetElevatorNumber = elevatorNumber;
		moveElevator(elevatorNumber);
		loadElevator(elevatorNumber);
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
		double netDistance = Math.abs(startLocation - endLocation);
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

	public static void setIsEvent(boolean isEvent) {Elevator_System.isEvent = isEvent;}

	public ElevatorCar[] getElevators(){return this.elevators;}

	/** Method to convert all elevator positions to byte[] format and send to Scheduler*/
	public void sendElevatorsData(){
		byte data[] = new byte[elevators.length + 1];
		data[0] = (byte) 4;
		for (int i = 1; i < data.length; i++) {
			data[i] = (byte) Math.round(elevators[i-1].getPosition());
		}

		System.out.println("Elevator_System: sending a elevator position data to Scheduler_System.");
		//create the datagram packet for the message with Port 20
		try {
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 40);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Send the scheduled task data to Elevator_System.
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
	 			this.elevatorRunning();
			}
	 }

	/** Main method*/
	public static void main(String[] args) {
		int totalNumElevators = 1;
		int totalNumFloors = 10;
		Elevator_System elevator_system = new Elevator_System(totalNumElevators,totalNumFloors);
		Thread elevatorSystemThread = new Thread(elevator_system, "Scheduler Simulation");
		elevatorSystemThread.start();
	}
}
