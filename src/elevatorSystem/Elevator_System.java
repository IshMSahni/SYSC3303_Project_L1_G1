package elevatorSystem;
// imports for scanning text file
import ElevatorStates.DoorClosed;

import java.util.ArrayList;

/** Elevator_System class, Simulates as a control system for Elevator Car objects */
public class Elevator_System implements Runnable{

	private static boolean isEvent;		// Variable for breaking wait()
	private ElevatorCar[] elevators; //List of elevators
	private Scheduler_System scheduler_system;
	private final double elevatorAcceleration = 0.9; // 0.9 meter per second square
	private final double elevatorTopSpeed = 2.7; // 2.7 meters per second
	private final long loadTime = 10; // 10 seconds is the average loading time
	private ArrayList<ElevatorState> states;

	private static Integer targetElevatorNumber;

	/** Constructor for Elevator_System */
	public Elevator_System(int totalNumElevators, int totalNumFloors){
		isEvent = false;
		targetElevatorNumber = 0;

		//Create elevators
		elevators = new ElevatorCar[totalNumElevators];
		for (int elevatorNumber = 0; elevatorNumber < totalNumElevators; elevatorNumber++) {
			elevators[elevatorNumber] = new ElevatorCar(elevatorNumber, totalNumFloors, this);

		}
	}

	/** This method will update elevator scheduled queue*/
	public synchronized void updateElevatorQueue(Integer elevatorNumber){
		ArrayList<Integer> tasks = this.scheduler_system.getScheduledQueue(elevatorNumber);

		this.elevators[elevatorNumber].setTasks(tasks);
		isEvent = true;
		targetElevatorNumber = elevatorNumber;

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

		//If net distance between floors allows elevator to reach top speed, then use 1st formula, else use 2nd formula.
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

	public void setSchedulerSystem(Scheduler_System scheduler_system){this.scheduler_system = scheduler_system;}

	public ElevatorCar[] getElevators(){return this.elevators;}

	/** Run method for Elevator_System */
	 @Override
	 public synchronized void run() {
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
				//Send signal to elevator car for which floor to go to and load/unload elevator
				if(isEvent){
					moveElevator(targetElevatorNumber);
					this.scheduler_system.removeElevatorTask(targetElevatorNumber);
					loadElevator(targetElevatorNumber);
					isEvent = false;
				}
			}
	 }
}
