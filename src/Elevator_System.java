// imports for scanning text file
import java.util.ArrayList;

/** Elevator_System class, Simulates as a control system for Elevator Car objects */
public class Elevator_System implements Runnable{

	private static boolean isEvent;		// Variable for breaking wait()
	private ArrayList<ElevatorCar> elevators; //List of elevators
	private Scheduler_System scheduler_system;

	/** Constructor for Elevator_System */
	public Elevator_System(ArrayList<ElevatorCar> elevators, Scheduler_System scheduler_system){
		this.scheduler_system = scheduler_system;
		this.elevators = elevators;
	}

	/** This method will update all the queues in all elevators */
	public void updateAllElevatorQueues(){
		for (int i = 0; i < this.elevators.size(); i++) {
			ArrayList<Integer> tasks = this.scheduler_system.getScheduledQueue(i);
			this.elevators.get(i).setTasks(tasks);
		}
	}

	/** Run method for Elevator_System */
	 @Override
	    public void run() {
	 		while (true) {

				//Wait until button is pressed inside elevator or elevator reaches floor
				//Notify Scheduler of button pressed in elevator
				//Wait until Scheduler has scheduled queue
				//Send signal to elevator cars on which floor to go to
			}
	    }
	


}
