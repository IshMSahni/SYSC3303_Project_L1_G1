package elevatorSystem;
// imports for scanning text file
import ElevatorStates.DoorClosed;

import java.util.ArrayList;

/** Elevator_System class, Simulates as a control system for Elevator Car objects */
public class Elevator_System implements Runnable{

	private static boolean isEvent;		// Variable for breaking wait()
	private ArrayList<ElevatorCar> elevators; //List of elevators
	private Scheduler_System scheduler_system;
	private final double elevatorAcceleration = 0.9; // 0.9 meter per second square
	private final double elevatorTopSpeed = 2.7; // 2.7 meters per second
	private final long loadTime = 10; // 10 seconds is the average loading time
	private ArrayList<ElevatorState> states;

	private static Integer targetElevatorNumber;

	/** Constructor for Elevator_System */
	public Elevator_System(ArrayList<ElevatorCar> elevators){
		this.elevators = elevators;
		isEvent = false;
		targetElevatorNumber = 0;
		for (int i =0; i < elevators.size(); i++){
			ElevatorState state = new DoorClosed(elevators.get(i));
			states.add(state);
		}
	}

	/** This method will update elevator scheduled queue*/
	public synchronized void updateElevatorQueue(Integer elevatorNumber){
		ArrayList<Integer> tasks = this.scheduler_system.getScheduledQueue(elevatorNumber);

		this.elevators.get(elevatorNumber).setTasks(tasks);
		//isEvent = true;

		moveElevator(elevatorNumber);
		loadElevator(elevatorNumber);
	}

	/** Method to move elevator */
	public synchronized void moveElevator(Integer elevatorNumber){
		ElevatorCar elevator = elevators.get(elevatorNumber);
		Float startLocation = elevator.getPosition();
		Integer endLocation = elevator.getTasks().get(0);
		if (startLocation > endLocation){
			states.get(elevatorNumber).Direction(true);
			states.get(elevatorNumber).Elevator_NextState();
		} else {
			states.get(elevatorNumber).Direction(false);
		}
		states.set(elevatorNumber, states.get(elevatorNumber).Elevator_NextState());
		//Calculate Time to move elevator
		

		elevator.setMotors(false);
		elevator.setStatus("Stopped");
		//Set elevator to new position and remove task from Elevator's queue
		elevator.setPosition(endLocation);
		elevator.setDoors(true);
		elevator.setButton(endLocation, false);
		elevator.getTasks().remove(0);
		elevators.set(elevatorNumber,elevator);
		System.out.println("Elevator "+elevatorNumber+" now at floor "+endLocation+", Door Opening.");
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
		try{
			wait(loadTime*1000);
			ElevatorCar targetElevator = elevators.get(elevatorNumber);
			targetElevator.setDoors(false);
			System.out.println("Loading Elevator "+elevatorNumber+" completed, Door closed.");
		}
		catch (Exception e){
			System.out.println("Error occured while loading Elevator in thread.");
			e.printStackTrace();
		}
	}

	public static void setIsEvent(boolean isEvent) {Elevator_System.isEvent = isEvent;}

	public void setSchedulerSystem(Scheduler_System scheduler_system){this.scheduler_system = scheduler_system;}

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
