/**
 * Floor_System Class:
 * 	Class manages and initializes floors and people
 * 	Note Class implements Runnable for floor managing thread
 * Written by: Keith Lam 101147900
 */

// imports for scanning text file
import java.io.File;
import java.io.FileNotFoundException;
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
	
	static Person[] allPeople;				// Array of Person objects
	static Floor[] floors = new Floor[3];	// Array of Floor objects
	
	/**
	 * Method: readFile
	 * 	Method reads txt file, processes the person data and passes formalized list.
	 * 	Args: Null
	 * 	Return: Array of Person objects
	 */
	private static Person[] readFile() {
		String[] timeSTR;			// Time array as String variables
		String buttonStatustemp;	// temp variable to process button status
		int index = 0;				// temp index variable for time calculation
		String[] txt;				// Array of strings to organize person data
		int numberOfLines = 0;		// Variable to count number of Lines
		String temp;				// temp variable for programming logic
		
		time = new int[4];	// Initialize array with empty elements
		Person[] allPeople = new Person[numberOfLines];	// Initialize array with empty elements
		
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
				allPeople[i] = myPerson;	// Add Person to array of people
		    }
			myReader.close();
			return allPeople;
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
	public Person[] getAllPeople() {
		return allPeople;
	}
	
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
	public void run() {
		
		// while loop loops thread until program is terminated
		while (true) {
			
			// if statement displays all initial floor data
			if (eventFloor == -1) {
				for (int i = 0; i < getFloors().length; i++) {
					getFloors()[i].printFloor();
				}
			} else {	// else statement displays changed floor data
				getFloors()[eventFloor].printFloor();
			}
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
		eventFloor = -1;	// Initializing status variable

		// for loop initializes every floor
		for (int i = 0; i < (floors.length - 1); i++) {
			floors[i] = new Floor();
			floors[i].setFloor(i);
		}
		
		// Separate logic to initialize top floor
		// Note: # of floors - 1 (replace '2')
		floors[2] = new Floor();
		floors[2].setFloor((floors.length - 1), true);	//Separate floor initialization for last floor (Override)
		
		allPeople = readFile();	// Initialize people array using readFile method
		
		// for loop adds all people to queue
		for (int i = 0; i < allPeople.length; i++) {
		// Scheduler_System.addToQueue(allPeople[i]);
		}
		
		// Initializes floor manager thread and starts it
		Thread floorSystemThread = new Thread(new Floor_System(), "Floor Simulation");
		floorSystemThread.start();
	}
}
