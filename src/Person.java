/**
 * Person object
 */
public class Person {
	
	private int[] time;	// Time array (hr / min / sec)
	private int floorNumber;	// floor number person is on
	private int buttonStatus;	// Button status variable (0 = un-pressed, 1 = up, 2 = down)
	private int destination;	// Destination floor variable
	
	public Person(int[] time, int floorNumber, int buttonStatus, int destination) {
		this.time = time;
		this.floorNumber = floorNumber;
		this.buttonStatus = buttonStatus;
		this.destination = destination;
	}
	
	public int[] getTime() {
		return time;
	}
	
	public int getFloorNumber() {
		return floorNumber;
	}
	
	public int getButtonStatus() {
		return buttonStatus;
	}
	
	public int getDestination() {
		return destination;
	}
}
