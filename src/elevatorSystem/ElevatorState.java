package elevatorSystem;

/** This class represents the ElevatorState interface that other elevator state classes will implement from*/
public interface ElevatorState {
    ElevatorCar ele = null;
    void Elevator_Loading();
    void Elevator_Arrived();
    void Elevator_DoorOpen();
    void Elevator_DoorClosed();
    void Elevator_MovingUp();
    void Elevator_MovingDown();
}
