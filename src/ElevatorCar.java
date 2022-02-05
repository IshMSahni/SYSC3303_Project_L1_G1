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
    
    private String status; //Possible statuses:"Stopped","Door Open","Moving Up","Moving Down"
    
    /** Constructor for Elevator Car */
    public ElevatorCar(int elevatorNumber, ArrayList<Boolean> lights, ArrayList<Boolean> buttons){
        this.elevatorNumber = elevatorNumber; //
        this.position = 0; //
        this.status = "Stopped"; //
        this.lights = lights;
        this.buttons = buttons;
        this.doorsOpen = false;
        this.motors = false;
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
    
    public void setLights(int location, boolean value) {
    	lights.set(location, value);
    }
    
    public boolean getLights(int location) {
    	return lights.get(location);
    }
    
    public void setButton(int location, boolean value) {
    	lights.set(location, value);
    }
    
    public boolean getButton(int location) {
    	return lights.get(location);
    }
    
    public boolean getDoors() {
    	return this.doorsOpen;
    }
    
    public void setDoors(boolean value) {
    	this.doorsOpen = value;
    }
    
    public boolean getMotors() {
    	return this.doorsOpen;
    }
    
    public void setMotors(boolean value) {
    	this.doorsOpen = value;
    }
    
    
    
    

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
    
    
   
}
