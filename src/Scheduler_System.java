import java.util.ArrayList;
import java.util.HashMap;

/**
 * Scheduler class
 */
public class Scheduler_System implements Runnable{
    private ArrayList<ElevatorCar> elevators;
    private ArrayList<Floor> floors;
    private Elevator_System elevator_system;
    private ArrayList<Task> tasksQueue;
    private HashMap<Integer,ArrayList<Integer>> scheduledQueue;  //Key is Elevator Number, Values are next floor numbers to go to.

    /** Constructor for Scheduler_System */
    public Scheduler_System(ArrayList<ElevatorCar> elevators, ArrayList<Floor> floors, Elevator_System elevator_system){
        this.elevators = elevators;
        this.elevator_system = elevator_system;
        this.floors = floors;
        this.tasksQueue = new ArrayList<>();
        for (int i = 0; i < this.elevators.size(); i++) {
            scheduledQueue.put(i,new ArrayList<>());
        }
    }

    /**Get Scheduled tasks from Scheduler for a given elevator number*/
    public ArrayList<Task> getScheduledQueue(Integer elevatorNumber){return this.getScheduledQueue(elevatorNumber);}

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
            //Now find best position in scheduled queue for Task
            sortScheduleQueue(bestElevator.getElevatorNumber());
        }
        //Elevator Task
        else{
            //Find best position in scheduled queue for Task
            sortScheduleQueue(task.getElevatorNumber());
        }
    }

    /**Method to Sort Scheduled Elevator Tasks */
    public void sortScheduleQueue(Integer elevatorNumber){
        //Target Floor number of latest task added and status of elevator.
        Integer targetFloorNumber = tasksQueue.get(tasksQueue.size() - 1).getFloorNumber();
        Float elevatorPosition = elevators.get(elevatorNumber).getPosition();
        ArrayList<Integer> queue = scheduledQueue.get(elevatorNumber);

        Boolean condition1 = (elevatorPosition > queue.get(0))&&(targetFloorNumber > queue.get(0))&&(elevatorPosition > targetFloorNumber);
        Boolean condition2 = (elevatorPosition < queue.get(0))&&(targetFloorNumber < queue.get(0))&&(elevatorPosition < targetFloorNumber);

        int i = 0;
        while (!condition1 && !condition2 && (i < queue.size())){
            i++;
            condition1 = (elevatorPosition > queue.get(i))&&(targetFloorNumber > queue.get(i))&&(elevatorPosition > targetFloorNumber);
            condition2 = (elevatorPosition < queue.get(i))&&(targetFloorNumber < queue.get(i))&&(elevatorPosition < targetFloorNumber);
        }

        //Add targetFloorNumber to best index "i" found and replace sceduledQueue with New queue
        queue.add(i,targetFloorNumber);
        scheduledQueue.replace(elevatorNumber,queue);
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
