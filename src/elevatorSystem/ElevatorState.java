package elevatorSystem;

import java.util.ArrayList;

/** This class represents the ElevatorState interface that other elevator state classes will implement from*/
public interface ElevatorState {
    void setState(ElevatorCar elevator, ArrayList<ElevatorCar> system);
    ElevatorState Elevator_NextState();

    void Direction(boolean check);
}
