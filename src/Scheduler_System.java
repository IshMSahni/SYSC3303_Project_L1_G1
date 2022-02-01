import java.util.ArrayList;

/**
 * Scheduler class
 */
public class Scheduler_System implements Runnable{
    private ArrayList<ElevatorCar> elevators;
    private ArrayList<Floor> floors;
    private Elevator_System elevator_system;
    private ArrayList<String> tasksQueue;
    private ArrayList<String> scheduledQueue;

    /**
     * Constructor for Scheduler_System
     */
    public Scheduler_System(ArrayList<ElevatorCar> elevators, ArrayList<Floor> floors, Elevator_System elevator_system){
        this.elevators = elevators;
        this.elevator_system = elevator_system;
        this.floors = floors;
        this.tasksQueue = new ArrayList<>();
        this.scheduledQueue = new ArrayList<>();
    }

    public ArrayList<String> getScheduledQueue(){return this.scheduledQueue;}

    @Override
    public void run() {
        while (true){
            //Wait until new task is added or task is complete
            //Then schedule tasks
            //Notify Elevator system
        }
    }
}
