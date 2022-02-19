package ElevatorStates;

import elevatorSystem.ElevatorCar;
import elevatorSystem.ElevatorState;

public class MovingUp implements ElevatorState {

    private ElevatorCar elevator;

    public MovingUp(ElevatorCar elevator){
        this.elevator = elevator;
    }

    @Override
    public void moveElevator(long time) {

    }

    @Override
    public void openDoor() {

    }

    @Override
    public void closeDoor() {

    }

    @Override
    public void loadElevator(long time) {

    }

    @Override
    public void elevatorArrived() {

    }
}
