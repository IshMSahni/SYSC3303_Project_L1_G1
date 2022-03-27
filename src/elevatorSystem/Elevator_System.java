package elevatorSystem;
// imports for scanning text file
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
	private DatagramSocket sendSocket, receiveSocket;

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
		// Data is new scheduled Bug Task
		else if(data[0] == (byte) 3){
			System.out.println("New Scheduled Bug Task recieved.");
			int bugNumber = data[1];
			int elevatorNum = data[2];
			updateElevatorQueue(elevatorNum, data);

			//Elevator Delayed bug
			if(bugNumber == 1){
				//TODO
			}

			//Elevator door stuck closed bug
			else if (bugNumber == 2){
				//TODO
			}

			//Elevator door stuck open bug
			else if (bugNumber == 3){
				//TODO
			}
		}
		//All Elevators Position Request
		else if(data[0] == (byte) 1){
			this.sendElevatorsData();
		}
		//Specific Elevator position request
		else if(data[0] == (byte) 4){
			int elevatorNumber = data[1];
			float position = elevators[elevatorNumber].getPosition();
			data = new byte[1];
			data[0] = (byte) Math.round(position);
			sendData(data,(100+elevatorNumber));
		}

		//Elevator Out_Of_Service data
		else if(data[0] == (byte) 5){
			int elevatorNumber = data[1];
			//set elevator state to Out_Of_Service
			elevators[elevatorNumber].setElevatorState(elevators[elevatorNumber].getOutOfService());

			//Send out of service elevator data update to Scheduler_system
			data = new byte[2];
			data[0] = (byte) 8; data[1] = (byte) elevatorNumber;
			sendData(data,10);
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
	public void elevatorRunningSupportStartUp() {
		int numElevators = this.elevators.length;
		Thread[] elevatorThreads = new Thread[numElevators];

		//Create elevator threads
		for (int i = 0; i < numElevators; i++) {
			elevatorThreads[i] = new Thread(this.elevators[i], "Scheduler Simulation");
		}

		//Run the elevator threads
		for (int i = 0; i < numElevators; i++) {
			elevatorThreads[i].start();
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

	/** This method will send data to given portNumber*/
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
		if(this.isSupport) {
			this.elevatorRunningSupportStartUp();
		}
		else {
			while (true) {
				//Wait until event occurs for elevator
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
