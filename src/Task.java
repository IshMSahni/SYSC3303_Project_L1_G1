/** Task class. Represents a task given to scheduler */
public class Task {
    private String[] time;
    private int elevatorNumber;
    private int floorNumber;
    private String direction;
    private Boolean isFloorTask;

    /** Constructor if task is an Elevator Task*/
    public Task(String[] time, int elevatorNumber, int floorNumber){
        this.time = time;
        this.elevatorNumber = elevatorNumber;
        this.floorNumber = floorNumber;
        this.isFloorTask = false;
    }

    /** Constructor if task is a Floor task */
    public Task(String[] time, int floorNumber, String direction){
        this.time = time;
        this.floorNumber = floorNumber;
        this.direction = direction;
        this.isFloorTask = true;
    }

    /** Getter methods for all attributes */
    public String[] getTime() {return time;}
    public Boolean getIsFloorTask() {return this.isFloorTask;}
    public int getFloorNumber() {return floorNumber;}
    public int getElevatorNumber() {return elevatorNumber;}
    public String getDirection() {return direction;}
}
