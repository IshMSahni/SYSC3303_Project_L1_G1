package elevatorSystem;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Scheduler_System class. Simulates a scheduler system for an Elevator system.
 * elevators attribute to store the list of Elevators
 * floors attribute to store the list of Floors
 * elevator_system attribute to store a reference to the Elevator_System
 * tasksQueue attribute to store a list Tasks added to schedule
 * scheduledQueue attribute to store a list of scheduled Floor numbers to visit (Value) for each Elevator (Key)
 * isNewTaskScheduled attribute to store a boolean value of true if a new task is scheduled, else false
 * targetElevatorNumber attribute to store an integer number of the elevator selected to be scheduled
 */
public class Scheduler_System implements Runnable{
    private ElevatorCar[] elevators;
    private Elevator_System elevator_system;
    private ArrayList<Task> tasksQueue;
    private HashMap<Integer,ArrayList<Integer>> scheduledQueue;
    private static Boolean isNewTaskScheduled;
    private static Integer targetElevatorNumber;

    /** Constructor for Scheduler_System */
    public Scheduler_System(){
        isNewTaskScheduled = false;
        this.tasksQueue = new ArrayList<>();
        this.scheduledQueue = new HashMap<>();
        /**
        for (int i = 0; i < this.elevators.size(); i++) {
            this.scheduledQueue.put(i,new ArrayList<>());
        }*/
        targetElevatorNumber = 0;
    }

    /**Get Scheduled tasks from Scheduler for a given elevator number*/
    public synchronized ArrayList<Integer> getScheduledQueue(Integer elevatorNumber){return this.scheduledQueue.get(elevatorNumber);}

    /**Add task to queue for Scheduler and schedule it*/
    public void addToQueue(Task task){
        tasksQueue.add(task);
        scheduleTask(task);
    }

    /**Remove First task from queue of a given elevator number*/
    public void removeElevatorTask(Integer elevatorNumber){
        scheduledQueue.get(elevatorNumber).remove(0);
    }

    /** Schedule a given Task based off elevators & floors data */
    public void scheduleTask(Task task){
        //Get All Elevators position and status
        Integer bestElevatorNumber = 0;
        Integer bestTaskNumber = getTaskNumber(bestElevatorNumber);

        //Floor task
        if(task.getIsFloorTask()){
            if(elevators.length > 1) {
                for (int i = 0; i < elevators.length; i++) {
                    Integer currentTaskNumber = getTaskNumber(i);
                    if(bestTaskNumber > currentTaskNumber){
                        bestTaskNumber = currentTaskNumber;
                        bestElevatorNumber = i;
                    }
                }
            }
        }
        //Elevator Task
        else{
            bestElevatorNumber = task.getElevatorNumber();
            bestTaskNumber = getTaskNumber(bestElevatorNumber);
        }
        //Add task to scheduled Task list for best elevator number if task is Not already queue.
        if(bestTaskNumber != -1) {
            ArrayList<Integer> queue = this.scheduledQueue.get(bestElevatorNumber);
            queue.add(bestTaskNumber, task.getFloorNumber());
            this.scheduledQueue.replace(task.getElevatorNumber(), queue);
            targetElevatorNumber = bestElevatorNumber;
            //isNewTaskScheduled = true;
            this.elevator_system.updateElevatorQueue(bestElevatorNumber);
        }
    }

    /** Method to get position of task if added to a given elevator number to schedule most the recent task
     * Return -1 if target floor is already in queue */
    public int getTaskNumber(Integer elevatorNumber){
        //Target Floor number of latest task added and status of elevator.
        Integer targetFloorNumber = tasksQueue.get(tasksQueue.size() - 1).getFloorNumber();
        Float elevatorPosition = elevators[elevatorNumber].getPosition();
        ArrayList<Integer> queue = this.scheduledQueue.get(elevatorNumber);
        Boolean condition1 = false;
        Boolean condition2 = false;
        Integer bestTaskNumber = 0;

        //Iterate task number until condition1 or condition2 becomes true, Or until queue.size reaches end.
        for (int i = 0; !condition1 && !condition2 && (i < queue.size()); i++){
            Integer queueFloor = queue.get(i);
            if(targetFloorNumber == queueFloor){
                return -1; //Return -1 if target floor already in queue
            }
            condition1 = (elevatorPosition > queueFloor)&&(targetFloorNumber > queueFloor)&&(elevatorPosition > targetFloorNumber);
            condition2 = (elevatorPosition < queueFloor)&&(targetFloorNumber < queueFloor)&&(elevatorPosition < targetFloorNumber);
            bestTaskNumber = i;
        }
        return bestTaskNumber;
    }

    /** Getter method for targert Elevator Number */
    public Integer getTargetElevatorNumber(){return targetElevatorNumber;}

    /** Setter methods for a group of attributes */
    public void setElevator_system(Elevator_System elevator_system){
        this.elevator_system = elevator_system;
        elevator_system.setSchedulerSystem(this);
        elevators = elevator_system.getElevators();
    }
    public void setIsNewTaskScheduled(Boolean condition){isNewTaskScheduled = condition;}
    public void setTasksQueue(ArrayList<Task> tasks){this.tasksQueue = tasks;}
    public void setScheduledQueue(HashMap<Integer,ArrayList<Integer>> tasks){this.scheduledQueue = tasks;}

    /** run method for Scheduler Thread */
    @Override
    public synchronized void run() {
        while (true){
            //Wait until new task is added and scheduled
            while (!isNewTaskScheduled) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("Error occured while waiting for New Scheduled Task in thread.");
                    e.printStackTrace();
                }
            }
            //Notify Elevator system
            if(isNewTaskScheduled) {
                this.elevator_system.updateElevatorQueue(targetElevatorNumber);
                isNewTaskScheduled = false;
            }
        }
    }
}
