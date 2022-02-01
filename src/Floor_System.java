/**
 * Floor Control System
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Floor_System implements Runnable{
	
	private static int[] time;	// Time array (hr / min / sec / ms)
	private static int floorNumber;	// floor number variable 
	private static int buttonStatus;	// Button status variable (0 = un-pressed, 1 = up, 2 = down)
	private static int carButton;	// Destination floor variable
	
	static Person[] allPeople;
	static Floor[] floors = new Floor[3];
	
	private static Person[] readFile() {
		String[] timeSTR;	// Time array as String variables
		String buttonStatustemp;	// temp variable to process button status
		int index = 0;
		String[] txt;
		int numberOfLines = 0;
		String temp;
		
		time = new int[4];
		
		try {
			File myObj = new File("filename.txt");
		    Scanner myCounter = new Scanner(myObj);
		    
		    while (myCounter.hasNextLine()) {
		    	temp = myCounter.nextLine();
		    	numberOfLines++;
		    }
		    myCounter.close();
		    Scanner myReader = new Scanner(myObj);
		    
		    Person[] allPeople = new Person[numberOfLines];
		    
		    for (int i = 0; i < numberOfLines; i++) {
		    	index = 0;
			    txt = myReader.nextLine().split(" ");
			    
			    timeSTR = txt[0].split(":");	// Read time from file
			    floorNumber = Integer.parseInt(txt[1]);	// Read floor number from file
				buttonStatustemp = txt[2];	// Read button status from file
				carButton = Integer.parseInt(txt[3]); // Read Destination floor from file
			
				for (int i0 = 0; i0 < (timeSTR.length - 1); i0++) {
					time[i0] = Integer.parseInt(timeSTR[i0]);
					index++;
				}
				double x = Double.parseDouble(timeSTR[index]);
				index = (int)x;
				
				x = x % 1;
				
				time[3] = (int) (1000 * x);
				time[2] = index;
				
				if (buttonStatustemp.equals("Up")) {
					buttonStatus = 1;
				} else if (buttonStatustemp.equals("Down")) {
					buttonStatus = 2;
				} else {
					buttonStatus = 0;
				}
				Person myPerson = new Person(time, floorNumber, buttonStatus, carButton);
				allPeople[i] = myPerson;
		    }
			myReader.close();
			return allPeople;
		} catch (FileNotFoundException e) {
			System.out.println("Error occured while reading file.");
		    e.printStackTrace();
		}
		return null;
	}
	
	public Floor[] getFloors() {
		return floors;
	}
	
	public Person[] getAllPeople() {
		return allPeople;
	}
	
	public void ButtonEvent(int floor, int status) {
		if (status == 0) {
			getFloors()[floor].setDownButton(false);
			getFloors()[floor].setUpButton(false);
		} else if (status == 1) {
			getFloors()[floor].setDownButton(false);
			getFloors()[floor].setUpButton(true);
		} else if (status == 2){
			getFloors()[floor].setDownButton(true);
			getFloors()[floor].setUpButton(false);
		} else if (status == 3){
			getFloors()[floor].setDownButton(true);
			getFloors()[floor].setUpButton(true);
		}
	}
	
	public void LampEvent(int floor, int elevator, int status) {
		floors[floor].changeLamp(elevator, status);
	}
	
	@Override
	public void run() {
		for (int i = 0; i < getFloors().length; i++) {
			getFloors()[i].printFloor();
		}
		
		//while (true) {

		//}
	}
	
	public static void main(String[] args) {
		//Floor_System[] Floors = new Floor_System[# of floors];

		for (int i = 0; i < (floors.length - 1); i++) {
			floors[i] = new Floor();
			floors[i].setFloor(i);
		}
		
		// # of floors - 1
		floors[2] = new Floor();
		floors[2].setFloor((floors.length - 1), true);	//Separate floor initialization for last floor (Override)
		
		allPeople = readFile();
		
		for (int i = 0; i < allPeople.length; i++) {
		// Scheduler_System.addToQueue(allPeople[i]);
		}
		
		Thread floorSystemThread = new Thread(new Floor_System(), "Floor Simulation");
		floorSystemThread.start();
	}
}
