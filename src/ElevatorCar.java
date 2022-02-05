import java.util.ArrayList;

/**
 * Elevator car objects.
 */
public class ElevatorCar {
    
	private int elevatorNumber;
    private float position;
    // lights buttons doors and motors
    private ArrayList<Boolean> lights = new ArrayList<Boolean>();
    private ArrayList<Boolean> buttons = new ArrayList<Boolean>();
    private boolean doorsOpen = false; //to open the doors
    private boolean motors =  false; //for motion
    private ArrayList<Integer> tasks;
    private Scheduler_System scheduler_system;
    
    private String status; //Possible statuses:"Stopped","Moving Up","Moving Down"
    
    /** Constructor for Elevator Car */
    public ElevatorCar(int elevatorNumber, int totalFloorNumber, Scheduler_System scheduler_system){
        this.elevatorNumber = elevatorNumber; //
        this.position = 0; //
        this.status = "Stopped"; //
        this.lights = new ArrayList<>();
        this.buttons = new ArrayList<>();
        this.doorsOpen = false;
        this.motors = false;
        this.tasks = new ArrayList<>();
        this.scheduler_system = scheduler_system;

        for (int i = 0; i < totalFloorNumber; i++) {
            this.lights.add(false);
            this.buttons.add(false);
        }
    }
    
    /** Getter and setters for position and status */
    public float getPosition() {return position; }
    public void setPosition(float position) {this.position = position; }
    
    public String getStatus() {return status; }
    public void setStatus(String status) {this.status = status; }
    
    public int getElevatorNumber() {return elevatorNumber;}
    
    public void reachedFloor(float position) {
    	doorsOpen = true;
    	motors = false;
    	
    	buttons.set((int)position, false);
    	status = "Stopped";
    }

    /** Method for when a button is pressed inside Elevator */
    public void buttonPressed(int buttonLocation){
        this.scheduler_system.addToQueue(new Task(elevatorNumber,buttonLocation));
        this.setLights(buttonLocation,true);
        this.setButton(buttonLocation, true);
    }

    /**Getter and Setter method for Lights */
    public void setLights(int location, boolean value) {lights.set(location, value);}
    public boolean getLights(int location) {return lights.get(location);}

    /**Getter and Setter method for buttons */
    public void setButton(int location, boolean value) {buttons.set(location, value);}
    public boolean getButton(int location) {return buttons.get(location);}

    /**Getter and Setter method for doorsOpens */
    public boolean getDoors() {return this.doorsOpen;}
    public void setDoors(boolean value) {this.doorsOpen = value;}

    /**Getter and Setter method for Motors */
    public boolean getMotors() {return this.motors;}
    public void setMotors(boolean value) {this.motors = value;}

    /**Getter and Setter method for Tasks */
    public ArrayList<Integer> getTasks() {return tasks;}
    public void setTasks(ArrayList<Integer> tasks) {this.tasks = tasks;}

    public void setScheduler_system(Scheduler_System scheduler_system){this.scheduler_system = scheduler_system;}
}
