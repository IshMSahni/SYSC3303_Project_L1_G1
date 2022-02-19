package elevatorSystem;

/** This class represents the ElevatorState interface that other elevator state classes will implement from*/
public interface ElevatorState {
    void moveElevator();
    void openDoor();
    void closeDoor();
    void loadElevator();
    void elevatorArrived();
}
