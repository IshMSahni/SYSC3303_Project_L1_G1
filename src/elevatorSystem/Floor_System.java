package elevatorSystem;
/**
 * Floor_System Class:
 * 	Class manages and initializes floors and people
 * 	Note Class implements Runnable for floor managing thread
 * Written by: Keith Lam 101147900
 */

// imports for scanning text file
import java.io.*;
import java.net.*;
import java.util.*;

// Class declaration
public class Floor_System implements Runnable{

	/**
	 * Initial Class Variables
	 */
	private static int[] time;			// Time array (hr / min / sec / ms)
	private static int floorNumber;		// floor number variable 
	private static int buttonStatus;	// Button status variable (0 = un-pressed, 1 = up, 2 = down)
	private static int carButton;		// Destination floor variable
	private static boolean isEvent;		// Variable for breaking wait()
	private static int eventFloor;		// Temp Target floor variable
	private static int bug;				// Bug type variable for person definition
	
	private static Scheduler_System scheduler_system;
	private static Elevator_System elevator_system; //Elevator system
	private DatagramPacket sendPacket, receivePacket; // UDP packets and sockets for send and recieving
	private DatagramSocket sendSocket, receiveSocket;
	private long previousTime;

	//static Person[] allPeople;				// Array of Person objects
	private ArrayList<Person> allPeople;
	private Floor[] floors;	// Array of Floor objects

	/**
	 * Constructor: Floor_System
	 * 	Constructor for JUnit testing
	 * 	Args: Null
	 */
	public Floor_System(int totalNumFloors, int totalNumElevators) {
		this.floors = new Floor[totalNumFloors];
		int lastFloorNumber = totalNumFloors - 1;
		for (int i = 0; i < lastFloorNumber; i++) {
			floors[i] = new Floor(i, totalNumElevators);
		}
		this.floors[lastFloorNumber] = new Floor(lastFloorNumber, totalNumElevators);
		this.floors[lastFloorNumber].setFloor(lastFloorNumber,true);
		this.allPeople = new ArrayList<>();
		this.previousTime = 0;

		try {
			//Create and send and recieve socket.
			receiveSocket = new DatagramSocket(30); // Floor_System will recieve data on Port 30
			sendSocket = new DatagramSocket(42); //If data is recieved from Port 42 then it Floor_System

		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void setScheduler_system(Scheduler_System system){
		scheduler_system = system;
	}

	/**
	 * Method: readFile
	 * 	Method reads txt file, processes the person data and passes formalized list.
	 * 	Args: Null
	 * 	Return: Array of Person objects
	 */
	public static Person[] readFile() {
		String[] timeSTR;			// Time array as String variables
		String buttonStatustemp;	// temp variable to process button status
		int index = 0;				// temp index variable for time calculation
		String[] txt;				// Array of strings to organize person data
		int numberOfLines = 0;		// Variable to count number of Lines
		String temp;				// temp variable for programming logic

		time = new int[4];	// Initialize array with empty elements
		Person myPerson;

		// Try Catch statement for file processing
		try {
			File myObj = new File("wrongFilename.txt"); //Currently doing just default list of people testing, so wrong filename
			Scanner myCounter = new Scanner(myObj);

			// while loop counts number of lines
			while (myCounter.hasNextLine()) {
				temp = myCounter.nextLine();
				numberOfLines++;
			}

			// Close and re-open scanner
			myCounter.close();
			Scanner myReader = new Scanner(myObj);
			Person[] filePeople = new Person[numberOfLines];	// Initialize array with empty elements

			// for loop processes each line individually
			for (int i = 0; i < numberOfLines; i++) {
				index = 0;
				txt = myReader.nextLine().split(" ");	// Split data

				timeSTR = txt[0].split(":");			// Read time from file
				floorNumber = Integer.parseInt(txt[1]);	// Read floor number from file
				buttonStatustemp = txt[2];				// Read button status from file
				carButton = Integer.parseInt(txt[3]);	// Read Destination floor from file
				bug = Integer.parseInt(txt[4]);

				// for loop assigns hours and minutes to time array
				for (int i0 = 0; i0 < (timeSTR.length - 1); i0++) {
					time[i0] = Integer.parseInt(timeSTR[i0]);
					index++;
				}
				double x = Double.parseDouble(timeSTR[index]);	// Assign seconds/milliseconds to temp variable

				index = (int)x;	// Assigns seconds by casting int
				x = x % 1;		// Finds milliseconds using modulus

				// Assigns seconds and milliseconds to time array
				time[3] = (int) (1000 * x);
				time[2] = index;

				// Assign direction based on text input
				if (buttonStatustemp.equals("Up")) {
					buttonStatus = 1;
				} else if (buttonStatustemp.equals("Down")) {
					buttonStatus = 2;
				} else {
					buttonStatus = 0;
				}
				if (bug != 0 && bug <= 3) {
					myPerson = new Person(time, floorNumber, buttonStatus, carButton, bug);	// Create Person object with bug
				} else {
					myPerson = new Person(time, floorNumber, buttonStatus, carButton);	// Create Person object
				}
				filePeople[i] = myPerson;	// Add Person to array of people
			}
			myReader.close();
			return filePeople;
		} catch (FileNotFoundException e) {	// Cover case of interrupt
			System.out.println("Adding default people list");
			//Create default people objects and return a list of them.
			int time[] = new int[4];

			int totalNumPeoples = 12;
			Person defaultPeoples[] = new Person[totalNumPeoples];

			time[0]=00; time[1]=00; time[2]=03; time[3]=101;
			defaultPeoples[0] = new Person(time, 1, 1, 21, 0);

			time = new int[4]; time[0]=00; time[1]=00; time[2]=13; time[3]=101;
			defaultPeoples[1]= new Person(time, 0, 1, 5, 0);

			time = new int[4]; time[0]=00; time[1]=00; time[2]=23; time[3]=101;
			defaultPeoples[2] = new Person(time, 1, 1, 7, 0);

			time = new int[4]; time[0]=00; time[1]=00; time[2]=33; time[3]=101;
			defaultPeoples[3]= new Person(time, 0, 1, 12, 0);

			time = new int[4]; time[0]=00; time[1]=00; time[2]=43; time[3]=101;
			defaultPeoples[4]= new Person(time, 1, 1, 2, 0);
			time[0]=00; time[1]=00; time[2]=48; time[3]=101;
			defaultPeoples[5] = new Person(time, 21, 2, 1, 0);

			time = new int[4]; time[0]=00; time[1]=00; time[2]=53; time[3]=101;
			defaultPeoples[6]= new Person(time, 5, 1, 6, 0);

			time = new int[4]; time[0]=00; time[1]=01; time[2]=03; time[3]=101;
			defaultPeoples[7] = new Person(time, 6, 2, 1,3);

			time = new int[4]; time[0]=00; time[1]=01; time[2]=13; time[3]=101;
			defaultPeoples[8]= new Person(time, 7, 2, 4, 0);

			time = new int[4]; time[0]=00; time[1]=01; time[2]=23; time[3]=101;
			defaultPeoples[9]= new Person(time, 12, 2, 1);
			time[0]=00; time[1]=01; time[2]=33; time[3]=101;
			defaultPeoples[10] = new Person(time, 4, 2, 0, 1);

			time = new int[4]; time[0]=00; time[1]=01; time[2]=43; time[3]=101;
			defaultPeoples[11]= new Person(time, 2, 2, 1, 0);

			return defaultPeoples;
		}
	}

	/**
	 * Method: getFloors
	 * 	Method returns floors array
	 * 	Args: Null
	 * 	Return: Array of Floor objects
	 */
	public Floor[] getFloors() {
		return floors;
	}

	public void setFloors(Floor[] floors) {
		this.floors = floors;
	}

	/**
	 * Method: getAllPeople
	 * 	Method returns People array
	 * 	Args: Null
	 * 	Return: Array of Person objects
	 */
	public ArrayList<Person> getAllPeople() {
		return allPeople;
	}

	public void setAllPeople(ArrayList<Person> people) {
		this.allPeople = people;
	}

	public void setEventFloor(int eventFloorNum){eventFloor = eventFloorNum;}

	/**
	 * Method: buttonEvent
	 * 	Method handles any button event. Direction instructions in README
	 * 	Args: (int) floor, (int) direction
	 * 	Return: Void
	 */
	public void buttonEvent(int floor, int direction) {

		// if statement logic changes data based on args
		if (direction == 0) {
			getFloors()[floor].setDownButton(false);
			getFloors()[floor].setUpButton(false);
		} else if (direction == 1) {
			getFloors()[floor].setDownButton(false);
			getFloors()[floor].setUpButton(true);
		} else if (direction == 2){
			getFloors()[floor].setDownButton(true);
			getFloors()[floor].setUpButton(false);
		} else if (direction == 3){
			getFloors()[floor].setDownButton(true);
			getFloors()[floor].setUpButton(true);
		} else {	// else statement covers case of incorrect arg values
			System.out.println("Error occurred when buttonEvent() was called");
			System.exit(0);
		}

		// Variables used to communicate to thread logic
		isEvent = true;
		eventFloor = floor;
	}

	/**
	 * Method: lampEvent
	 * 	Method handles any lamp event. Direction instructions in README
	 * 	Args: (int) floor, (int) elevator, (int) direction
	 * 	Return: Void
	 */
	public void lampEvent(int floor, int elevator, int direction) {

		// if statement checks for correct arg values
		if (floor < floors.length && direction < 3) {
			floors[floor].changeLamp(elevator, direction);
		} else {	// else statement covers case of incorrect arg values
			System.out.println("Error occurred when lampEvent() was called");
			System.exit(0);
		}

		// Variables used to communicate to thread logic
		isEvent = true;
		eventFloor = floor;
	}

	/**
	 * Method: run
	 * 	@Override
	 * 	Covers thread logic. Displays event data
	 * 	Args: Null
	 * 	Return: Void
	 */
	public synchronized void run() {

		Task task;
		
		// while loop loops thread until program is terminated
		while (true) {
			//Schedule task
			if(this.allPeople.size() != 0) {

				byte data[] = new byte[7];
				int time[] = allPeople.get(0).getTime();

				//Find total time in miliseconds
				long totalTime = (time[0] *3600000) + (time[1] *60000) + (time[2] * 1000) + time[3];

				try{ wait(totalTime - previousTime);}
				catch (Exception e){
					System.out.println("Error while waiting between sending Peoples data");
					e.printStackTrace();
				}
				previousTime = totalTime;

				String buttonDirection = "";
				int direction = this.allPeople.get(0).getDirection();

				if(direction == 1) { buttonDirection = "Up"; }
				else if(direction == 2) { buttonDirection = "Down"; }

				//Floor Task data
				if (this.allPeople.get(0).getBug() != 0 && this.allPeople.get(0).getBug() <= 3) {
					task = new Task(time, buttonDirection ,this.allPeople.get(0).getFloorNumber(), this.allPeople.get(0).getBug());
				} else {
					task = new Task(time, buttonDirection ,this.allPeople.get(0).getFloorNumber());
				}
				this.sendData(task.getData(),10);

				//Wait 2 seconds
				try{ wait(2000);}
				catch (Exception e){
					System.out.println("Error while waiting 2 seconds sending Peoples data");
					e.printStackTrace();
				}

				//Elevator Task data
				task = new Task(this.allPeople.get(0).getDestination());
				this.sendData(task.getData(),10);

				this.allPeople.remove(0);
			}

			if(this.allPeople.size() == 0) {
				isEvent = false;
				System.exit(0);//Only because we don't have an GUI yet for Iteration 2
				// while loop loops until thread is notified
				while (!isEvent) {
					try {
						wait();
					} catch (InterruptedException e) {    // Try catch statement to catch thread logic interrupts
						System.out.println("Error occured while waiting in thread.");
						e.printStackTrace();
					}
				}
			}
		}
	}

	/** This method will send scheduled task data to Elevator_System*/
	public void sendData(byte data[], int portNumber){
		System.out.println("Sending a task data to Scheduler_System.");
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

	/**
	 * Method: main
	 * 	Method hosts Starting/main sequential logic and initialized objects
	 * 	Args: (String[]) Array of Strings
	 * 	Return: Void
	 */
	public static void main(String[] args) {
		int totalFloorNumber = 22; //total number of floors
		int totalElevatorNumber = 4; //total number  of elevators

		Floor_System floor_SubSystem = new Floor_System(totalFloorNumber,totalElevatorNumber);
		floor_SubSystem.setEventFloor(-1);

		Person[] people = readFile();	// Initialize people array using readFile method
		ArrayList<Person> peoples = new ArrayList<>();
		for (int i = 0; i < people.length; i++) {
			peoples.add(people[i]);
		}
		floor_SubSystem.setAllPeople(peoples);

		// Initializes floor manager thread and starts it
		Thread floorSystemThread = new Thread(floor_SubSystem, "Floor Simulation");
		floorSystemThread.start();
	}
}