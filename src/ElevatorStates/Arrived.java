package ElevatorStates;

import elevatorSystem.ElevatorCar;
import elevatorSystem.ElevatorState;

public class Arrived implements ElevatorState {

    private ElevatorCar elevator;

    public Arrived(ElevatorCar elevator){
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
