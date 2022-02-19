package ElevatorStates;

import elevatorSystem.ElevatorCar;
import elevatorSystem.ElevatorState;

public class DoorClosed implements ElevatorState {
        ElevatorCar e;
    public DoorClosed(ElevatorCar elevator){
        this.e = elevator;
    }

    @Override
    public void Elevator_Loading() {

    }

    @Override
    public void Elevator_Arrived() {

    }

    @Override
    public void Elevator_DoorOpen() {

    }

    @Override
    public void Elevator_DoorClosed() {

    }

    @Override
    public void Elevator_MovingUp(ElevatorCar elevator) {

    }

    @Override
    public void Elevator_MovingDown(ElevatorCar elevator) {

    }
}
