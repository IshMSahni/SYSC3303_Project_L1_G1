package elevatorSystem;
/**
 * Person Class:
 * 	Class is an object to represent a person
 * Written by: Keith Lam 101147900
 */
public class Person {
	
	private int[] time;			// Time array (hr / min / sec)
	private int floorNumber;	// floor number person is on
	private int direction;	// Button status variable (0 = un-pressed, 1 = up, 2 = down)
	private int destination;	// Destination floor variable
	
	/**
	 * Constructor:
	 * 	Person constructor initializes all person variables
	 * 	Args: (int[]) time, (int floorNumber, (int) buttonStatus, (int) destination)
	 */
	public Person(int[] time, int floorNumber, int direction, int destination) {
		this.time = time;
		this.floorNumber = floorNumber;
		this.direction = direction;
		this.destination = destination;
	}
	
	/**
	 * Method: getTime
	 * 	Method returns person arrival time
	 * 	Args: Null
	 * 	Return: Array of int
	 */
	public int[] getTime() {
		return time;
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
	 * Method: getDirection
	 * 	Method returns direction person wants to go
	 * 	Args: Null
	 * 	Return: int
	 */
	public int getDirection() {
		return direction;
	}
	
	/**
	 * Method: getDestination
	 * 	Method returns floor destination
	 * 	Args: Null
	 * 	Return: int
	 */
	public int getDestination() {
		return destination;
	}
}
