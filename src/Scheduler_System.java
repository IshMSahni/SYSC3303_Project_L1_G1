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

    /** Constructor for Scheduler_System */
    public Scheduler_System(ArrayList<ElevatorCar> elevators, ArrayList<Floor> floors, Elevator_System elevator_system){
        this.elevators = elevators;
        this.elevator_system = elevator_system;
        this.floors = floors;
        this.tasksQueue = new ArrayList<>();
        this.scheduledQueue = new ArrayList<>();
    }

    /**Get Scheduled tasks from Scheduler*/
    public ArrayList<String> getScheduledQueue(){return this.scheduledQueue;}

    /**Add task to queue for Scheduler*/
    public void addToQueue(String task){tasksQueue.add(task);}

    /**Remove task from all queues after finsihing a task*/
    public void removeFromQueue(String task){
        tasksQueue.remove(task);
        scheduledQueue.remove(task);
    }

    /** Schedule tasks based off elevators & floors data */
    public void scheduleTasks(){
        //Get All Elevators position and status
        ArrayList<Float> elevatorPositions = new ArrayList<>();
        ArrayList<String> elevatorStatuses = new ArrayList<>();
        for (int i = 0; i < elevators.size(); i++) {
            elevatorPositions.add(elevators.get(i).getPosition());
            elevatorStatuses.add(elevators.get(i).getStatus());
        }

        //Do some algorithm based off the data to schedule tasks
    }

    @Override
    public void run() {
        while (true){
            //Wait until new task is added or task is complete
            //Then schedule tasks
            //Notify Elevator system
        }
    }
}
