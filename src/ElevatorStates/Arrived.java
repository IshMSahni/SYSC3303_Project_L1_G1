package ElevatorStates;

import elevatorSystem.ElevatorCar;
import elevatorSystem.ElevatorState;

public class Arrived implements ElevatorState {
    ElevatorCar elevatorCar;
    public Arrived(ElevatorCar elevator){
        this.elevatorCar = elevator;
    }

    public Arrived(elevatorSystem.ElevatorCar elevatorCar) {
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
    public void Elevator_MovingUp() {

    }

    @Override
    public void Elevator_MovingDown() {

    }
}
