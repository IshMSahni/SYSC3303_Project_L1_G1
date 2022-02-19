package elevatorSystem;
/**
 * Floor_System Class:
 * 	Class manages and initializes floors and people
 * 	Note Class implements Runnable for floor managing thread
 * Written by: Keith Lam 101147900
 */

// imports for scanning text file
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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
	private static Scheduler_System scheduler_system;
	private static Elevator_System elevator_system; //Elevator system
	
	//static Person[] allPeople;				// Array of Person objects
	static ArrayList<Person> allPeople;
	static Floor[] floors;	// Array of Floor objects
	
	/**
	 * Constructor: Floor_System
	 * 	Constructor for JUnit testing
	 * 	Args: Null
	 */
	public Floor_System(int totalNumFloors, int totalNumElevators) {
		floors = new Floor[totalNumFloors];
		int lastFloorNumber = totalNumFloors - 1;
		for (int i = 0; i < lastFloorNumber; i++) {
			floors[i] = new Floor(i, totalNumElevators, this);
		}
		floors[lastFloorNumber] = new Floor(lastFloorNumber, totalNumElevators, this);
		floors[lastFloorNumber].setFloor(lastFloorNumber,true);
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
		
		// Try Catch statement for file processing
		try {
			File myObj = new File("filename.txt");
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
				Person myPerson = new Person(time, floorNumber, buttonStatus, carButton);	// Create Person object
				filePeople[i] = myPerson;	// Add Person to array of people
		    }
			myReader.close();
			return filePeople;
		} catch (FileNotFoundException e) {	// Cover case of interrupt
			System.out.println("Error occured while reading file.");
		    e.printStackTrace();
		}
		return null;	// Redundancy for syntax error coverage
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
	
	/**
	 * Method: getAllPeople
	 * 	Method returns People array
	 * 	Args: Null
	 * 	Return: Array of Person objects
	 */
	public ArrayList<Person> getAllPeople() {
		return allPeople;
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
		
		// while loop loops thread until program is terminated
		while (true) {
			/*
			// if statement displays all initial floor data
			if (eventFloor == -1) {
				for (int i = 0; i < getFloors().length; i++) {
					//getFloors()[i].printFloor();
				}
			} else {	// else statement displays changed floor data
				//getFloors()[eventFloor].printFloor();
			} */
			
			//Schedule task
			Integer elevatorNumber = scheduler_system.getTargetElevatorNumber();
			String buttonStatusTemp = "";
			int direction = allPeople.get(0).getDirection();
			//Assign buttonStatustemp
			if(direction == 1){
				buttonStatusTemp = "Up";
			}
			else if(direction == 2){
				buttonStatusTemp = "Down";
			}
			System.out.println("New Task added to queue, time: " + time[1] + ":" + time[2] + ", Target Floor: "
					+ floorNumber + ", Elevator Number: " + elevatorNumber);
					
			scheduler_system.addToQueue(new Task(allPeople.get(0).getTime(), buttonStatusTemp, allPeople.get(0).getFloorNumber()));
			// scheduler_system.addToQueue(new Task(time, buttonStatustemp, floorNumber));
			
			time[2] = time[2] + 1; //Add 1 second buffer between tasks
			System.out.println("New Task added to queue, time: " + time[1] + ":" + time[2] +", Target Floor: " 
					+ carButton + ", Elevator Number: " + elevatorNumber);
			
			scheduler_system.addToQueue(new Task(allPeople.get(0).getTime(), elevatorNumber, allPeople.get(0).getDestination()));
			// scheduler_system.addToQueue(new Task(time, elevatorNumber, carButton));
			
			allPeople.remove(0);
			isEvent = false;
			
			// while loop loops until thread is notified
			while (!isEvent) {
				try {
					wait();
				} catch (InterruptedException e) {	// Try catch statement to catch thread logic interrupts
					System.out.println("Error occured while waiting in thread.");
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Method: main
	 * 	Method hosts Starting/main sequential logic and initialized objects
	 * 	Args: (String[]) Array of Strings
	 * 	Return: Void
	 */
	public static void main(String[] args) {
		int totalFloorNumber = 10; //total number of floors
		int totalElevatorNumber = 1; //total number of elevators

		Floor_System floor_SubSystem = new Floor_System(totalFloorNumber,totalElevatorNumber);
		Elevator_System elevator_SubSystem = new Elevator_System(totalElevatorNumber,totalFloorNumber);
		Scheduler_System scheduler_SubSystem = new Scheduler_System();

		scheduler_SubSystem.setElevator_system(elevator_SubSystem);
		floor_SubSystem.setScheduler_system(scheduler_SubSystem);
		floor_SubSystem.setEventFloor(-1);

		Person[] people = readFile();	// Initialize people array using readFile method
		for (int i = 0; i < people.length; i++) {
			floor_SubSystem.allPeople.add(people[i]);
		}
		
		// Initializes floor manager thread and starts it
		Thread floorSystemThread = new Thread(floor_SubSystem, "Floor Simulation");
		Thread elevatorSystemThread = new Thread(elevator_SubSystem, "Elevator Simulation");
		Thread schedulerSystemThread = new Thread(scheduler_SubSystem, "Scheduler Simulation");

		floorSystemThread.start();
		elevatorSystemThread.start();
		schedulerSystemThread.start();
		System.exit(0); // This is to end program Only for Iteration 1 after reading data from file.
	}
}
