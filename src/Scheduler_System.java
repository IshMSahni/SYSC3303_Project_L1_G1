import java.util.ArrayList;

/**
 * Scheduler class
 */
public class Scheduler_System implements Runnable{
    private ArrayList<ElevatorCar> elevators;
    private ArrayList<Floor> floors;
    private Elevator_System elevator_system;
    private ArrayList<Task> tasksQueue;
    private ArrayList<Task> scheduledQueue;

    /** Constructor for Scheduler_System */
    public Scheduler_System(ArrayList<ElevatorCar> elevators, ArrayList<Floor> floors, Elevator_System elevator_system){
        this.elevators = elevators;
        this.elevator_system = elevator_system;
        this.floors = floors;
        this.tasksQueue = new ArrayList<>();
        this.scheduledQueue = new ArrayList<>();
    }

    /**Get Scheduled tasks from Scheduler*/
    public ArrayList<Task> getScheduledQueue(){return this.scheduledQueue;}

    /**Add task to queue for Scheduler and schedule it*/
    public void addToQueue(Task task){
        tasksQueue.add(task);
        scheduleTask(task);
    }

    /**Remove task from all queues after finsihing a task*/
    public void removeFromQueue(Task task){
        tasksQueue.remove(task);
        scheduledQueue.remove(task);
    }

    /** Schedule tasks based off elevators & floors data */
    public void scheduleTask(Task task){
        //Get All Elevators position and status
        ArrayList<Float> elevatorPositions = new ArrayList<>();
        ArrayList<ElevatorCar> possibleElevators = new ArrayList<>();

        //Floor task
        if(task.getIsFloorTask()){
            if(elevators.size() > 1) {
                for (int i = 0; i < elevators.size(); i++) {
                    float elevatorPosition = elevators.get(i).getPosition();
                    elevatorPositions.add(elevatorPosition);
                    String elevatorStatus = elevators.get(i).getStatus();
                    Boolean condition1 = elevatorStatus.equals("Moving Up") && (elevatorPosition > task.getFloorNumber());
                    Boolean condition2 = elevatorStatus.equals("Moving Down") && (elevatorPosition < task.getFloorNumber());
                    if (!condition1 && !condition2) {
                        possibleElevators.add(elevators.get(i));
                    }
                }
            }
            else{possibleElevators.add(elevators.get(0));}

            ElevatorCar bestElevator = possibleElevators.get(0);

            //If 0 or more than 1 elevator is a possibleElevator, then pick best one based on net distance to target.
            if(possibleElevators.size() != 1){
                if(possibleElevators.size() == 0){
                    possibleElevators = elevators;
                }
                for (int i = 0; i < possibleElevators.size(); i++) {
                    float bestDiffernce = Math.abs(bestElevator.getPosition() - task.getFloorNumber());
                    float differnce = Math.abs(possibleElevators.get(i).getPosition() - task.getFloorNumber());
                    if(differnce < bestDiffernce){ //if Closer to target change bestElevator
                        bestElevator = possibleElevators.get(i);
                    }
                }
            }

            //Best Elevator now selected until here
            //Now find best position in scheduled queue for Task (Another method)

        }
        //Elevator Task
        else{
            //Find best position in scheduled queue for Task (Another method)
        }
    }

    /**Method to Sort Scheduled Elevator Tasks */
    public void sortScheduleQueue(){

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
