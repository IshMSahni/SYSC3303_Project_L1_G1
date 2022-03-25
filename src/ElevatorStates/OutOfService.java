package ElevatorStates;

import elevatorSystem.ElevatorCar;
import elevatorSystem.ElevatorState;

public class OutOfService implements ElevatorState {
    private ElevatorCar elevator;

    public OutOfService(ElevatorCar elevatorCar) {
        elevator = elevatorCar;
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

    @Override
    public void elevatorOutOfService() {
        if ()
    }
}
