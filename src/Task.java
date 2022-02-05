/** Task class. Represents a task given to scheduler */
public class Task {
    private int[] time;
    private int elevatorNumber;
    private int floorNumber;
    private String direction;
    private Boolean isFloorTask;

    /** Constructor if task is an Elevator Task (No time given)*/
    public Task(int elevatorNumber, int floorNumber){
        this.elevatorNumber = elevatorNumber;
        this.floorNumber = floorNumber;
        this.isFloorTask = false;
    }

    /** Constructor if task is an Elevator Task*/
    public Task(int[] time, int elevatorNumber, int floorNumber){
        this.time = time;
        this.elevatorNumber = elevatorNumber;
        this.floorNumber = floorNumber;
        this.isFloorTask = false;
    }

    /** Constructor if task is a Floor task (No time given)*/
    public Task(String direction, int floorNumber){
        this.floorNumber = floorNumber;
        this.direction = direction;
        this.isFloorTask = true;
    }

    /** Constructor if task is a Floor task */
    public Task(int[] time, String direction, int floorNumber){
        this.time = time;
        this.floorNumber = floorNumber;
        this.direction = direction;
        this.isFloorTask = true;
    }

    /** Getter methods for all attributes */
    public int[] getTime() {return time;}
    public Boolean getIsFloorTask() {return this.isFloorTask;}
    public int getFloorNumber() {return floorNumber;}
    public int getElevatorNumber() {return elevatorNumber;}
    public String getDirection() {return direction;}
}
