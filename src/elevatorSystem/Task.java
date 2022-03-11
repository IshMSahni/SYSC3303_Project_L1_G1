package elevatorSystem;
/** Task class. Represents a task given to scheduler */
public class Task {
    private int[] time;
    private int elevatorNumber;
    private int floorNumber;
    private String direction;
    private Boolean isFloorTask;
    private byte[] data; //This first byte of data will refer to the type of data
                         // 0 = Elevator Task, No Time given.  1 = Elevator Task, Time given
                         // 2 = Floor Task, No Time given.     3 = Floor Task, Time given

    /** Constructor if task is an Elevator Task (No time given)*/
    public Task(int elevatorNumber, int floorNumber){
        this.elevatorNumber = elevatorNumber;
        this.floorNumber = floorNumber;
        this.isFloorTask = false;

        this.data = new byte[3];
        this.data[0] = (byte) 0;
        this.data[1] = (byte) elevatorNumber;
        this.data[2] = (byte) floorNumber;
    }

    /** Constructor if task is an Elevator Task*/
    public Task(int[] time, int elevatorNumber, int floorNumber){
        this.time = time;
        this.elevatorNumber = elevatorNumber;
        this.floorNumber = floorNumber;
        this.isFloorTask = false;

        this.data = new byte[7];
        this.data[0] = (byte) 1;
        this.data[1] = (byte) elevatorNumber;
        this.data[2] = (byte) floorNumber;
        this.data[3] = (byte) time[0];
        this.data[4] = (byte) time[1];
        this.data[5] = (byte) time[2];
        this.data[6] = (byte) (time[3]/10);
    }

    /** Constructor if task is a Floor task (No time given)*/
    public Task(String direction, int floorNumber){
        this.floorNumber = floorNumber;
        this.direction = direction;
        this.isFloorTask = true;

        int buttonDirection = 0;
        //Assign buttonStatustemp
        if (direction == "Up") {
            buttonDirection  = 1;
        } else if (direction == "Down") {
            buttonDirection  = 2;
        }

        this.data = new byte[3];
        this.data[0] = (byte) 0;
        this.data[1] = (byte) buttonDirection;
        this.data[2] = (byte) floorNumber;
    }

    /** Constructor if task is a Floor task */
    public Task(int[] time, String direction, int floorNumber){
        this.time = time;
        this.floorNumber = floorNumber;
        this.direction = direction;
        this.isFloorTask = true;

        int buttonDirection = 0;
        //Assign buttonStatustemp
        if (direction == "Up") {
            buttonDirection  = 1;
        } else if (direction == "Down") {
            buttonDirection  = 2;
        }

        this.data = new byte[7];
        this.data[0] = (byte) 3;
        this.data[1] = (byte) buttonDirection;
        this.data[2] = (byte) floorNumber;
        this.data[3] = (byte) time[0];
        this.data[4] = (byte) time[1];
        this.data[5] = (byte) time[2];
        this.data[6] = (byte) (time[3]/10);
    }

    /** Getter methods for all attributes */
    public int[] getTime() {return time;}
    public Boolean getIsFloorTask() {return this.isFloorTask;}
    public int getFloorNumber() {return floorNumber;}
    public int getElevatorNumber() {return elevatorNumber;}
    public String getDirection() {return direction;}
    public byte[] getData() { return data; }
}
