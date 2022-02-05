// imports for scanning text file
import java.util.ArrayList;

/** Elevator_System class, Simulates as a control system for Elevator Car objects */
public class Elevator_System implements Runnable{

	private static boolean isEvent;		// Variable for breaking wait()
	private ArrayList<ElevatorCar> elevators; //List of elevators
	private Scheduler_System scheduler_system;
	private final float elevatorAcceleration = 1; // 1 meter per second square
	private final float elevatorTopSpeed = 5; // 5 meters per second
	private static Integer targetElevatorNumber;

	/** Constructor for Elevator_System */
	public Elevator_System(ArrayList<ElevatorCar> elevators, Scheduler_System scheduler_system){
		this.scheduler_system = scheduler_system;
		this.elevators = elevators;
		isEvent = false;
		targetElevatorNumber = 0;
	}

	/** This method will update elevator scheduled queue*/
	public void updateElevatorQueue(){
		targetElevatorNumber = scheduler_system.getTargetElevatorNumber();
		ArrayList<Integer> tasks = this.scheduler_system.getScheduledQueue(targetElevatorNumber);
		this.elevators.get(targetElevatorNumber).setTasks(tasks);
		isEvent = true;
	}

	/** Method to move elevator */
	public void moveElevator(Integer elevatorNumber){
		//Calculate Time to move elevator
		ElevatorCar elevator = elevators.get(elevatorNumber);
		Float startLocation = elevator.getPosition();
		Integer endLocation = elevator.getTasks().get(0);
		long time = calculateTime(startLocation,endLocation);

		//Wait for calculated time
		try{ wait(time); }
		catch (Exception e){}

		//Set elevator to new position and remove task from Elevator's queue
		elevator.setPosition(endLocation);
		elevator.getTasks().remove(0);
		elevators.set(elevatorNumber,elevator);
	}

	/** Method to calculate time to move Elevator a given amount of distance */
	public long calculateTime(Float startLocation, Integer endLocation){
		double time;
		double netDistance = Math.abs(startLocation - endLocation);
		double inflectionDistance = (elevatorTopSpeed*elevatorTopSpeed)/elevatorAcceleration;

		//If net distance between floors allows elevator to reach top speed, then use 1st formula, else use 2nd formula.
		if(netDistance > inflectionDistance){
			time = ((netDistance - inflectionDistance) / elevatorTopSpeed) + (Math.sqrt(inflectionDistance) * 2);
		}
		else{
			time = Math.sqrt(netDistance) * 2;
		}
		//Return time converted to long type after rounding.
		return Math.round(time);
	}

	public static void setIsEvent(boolean isEvent) {Elevator_System.isEvent = isEvent;}

	/** Run method for Elevator_System */
	 @Override
	    public void run() {
	 		while (true) {
	 			//Wait until event occurs for elevator
	 			while (!isEvent){
					try {
						wait();
					} catch (InterruptedException e) {
						System.out.println("Error occured while waiting for New Elevator event in thread.");
						e.printStackTrace();
					}
				}
				//Send signal to elevator car for which floor to go to
				moveElevator(targetElevatorNumber);
	 			this.scheduler_system.removeElevatorTask(targetElevatorNumber);
			}
	    }
}
