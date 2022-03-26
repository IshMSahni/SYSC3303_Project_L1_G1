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
        System.out.println("Can Not move Elevator "+elevator.getElevatorNumber()+", Out of Service");
    }

    @Override
    public void openDoor() {
        System.out.println("Can Not open door for Elevator "+elevator.getElevatorNumber()+", Out of Service");
    }

    @Override
    public void closeDoor() {
        System.out.println("Can Not close door for Elevator "+elevator.getElevatorNumber()+", Out of Service");
    }

    @Override
    public void loadElevator(long time) {
        System.out.println("Can Not load Elevator "+elevator.getElevatorNumber()+", Out of Service");
    }

    @Override
    public void elevatorArrived() {
        System.out.println("Can Not arrive for Elevator "+elevator.getElevatorNumber()+", Out of Service");
    }

    @Override
    public void elevatorOutOfService() {
        System.out.println("Elevator "+elevator.getElevatorNumber()+" is already Out of Service");
    }
}
