package elevatorSystem;

/** This class represents the ElevatorState interface that other elevator state classes will implement from*/
public interface ElevatorState {
    ElevatorCar ele = null;
    void ElevatorState(ElevatorCar ele);
    void Elevator_Arrived();
    void Elevator_DoorOpen();
    void Elevator_DoorClosed();
    void Elevator_MovingUp(ElevatorCar elevator);
    void Elevator_MovingDown(ElevatorCar elevator);
    void Elevator_NextState();
}
