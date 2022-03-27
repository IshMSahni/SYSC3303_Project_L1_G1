package elevatorSystem;

/** This class will represent an instruction object for an ElevatorCar to follow*/
public class ElevatorAction {
    private int targetFloor;
    private int numPeople;

    public ElevatorAction(int targetFloor, int numPeople){
        this.numPeople = numPeople;
        this.targetFloor = targetFloor;
    }

    public int getNumPeople() { return numPeople; }
    public void setNumPeople(int numPeople) { this.numPeople = numPeople; }

    public int getTargetFloor() { return targetFloor; }
    public void setTargetFloor(int targetFloor) { this.targetFloor = targetFloor; }
}
