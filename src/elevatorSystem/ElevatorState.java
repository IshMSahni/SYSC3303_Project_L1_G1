package elevatorSystem;

/** This class represents the ElevatorState interface that other elevator state classes will implement from*/
public interface ElevatorState {
    void moveElevator(long time);
    void openDoor();
    void closeDoor();
    void loadElevator(long time);
    void elevatorArrived();
}
