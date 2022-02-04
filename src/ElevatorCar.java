/**
 * Elevator car objects.
 */
public class ElevatorCar {
    private int elevatorNumber;
    private float position;
    private String status; //Possible statuses:"Stopped","Door Open","Moving Up","Moving Down"

    /** Constructor for Elevator Car */
    public ElevatorCar(int elevatorNumber){
        this.elevatorNumber = elevatorNumber;
        this.position = 0;
        this.status = "Stopped";
    }

    /** Getter and setters for position and status */
    public float getPosition() {return position; }
    public void setPosition(float position) {this.position = position; }

    public String getStatus() {return status; }
    public void setStatus(String status) {this.status = status; }

    public int getElevatorNumber() {return elevatorNumber;}
}
