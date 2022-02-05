
// imports for scanning text file


// Class declaration
public class Elevator_System implements Runnable{
	
	/**
	 * Initial Class Variables
	 */
	private static float position;		// floor number variable 
	private static int buttonStatus;	// Button status variable
	private static int carButton;		// Destination floor variable
	private static boolean isEvent;		// Variable for breaking wait()
	private static int eventFloor;		// Temp Target floor variable
	
	static ElevatorCar[] elevator = new ElevatorCar[1];	
	
	public ElevatorCar[] getElevator() {
		return elevator;
	}
	
	 @Override
	    public void run() {
	        //Wait until button is pressed inside elevator or elevator reaches floor
	        //Notify Scheduler of button pressed in elevator
	        //Wait until Schduler has scheduled queue
	        //Send signal to elevator cars on which floor to go to
	    }
	


}
