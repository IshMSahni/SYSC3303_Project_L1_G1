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
        System.out.println("Can Not move elevator");
    }

    @Override
    public void openDoor() {
        System.out.println("Door opening now for Elevator "+elevator.getElevatorNumber());
        elevator.setDoors(true);
        elevator.setElevatorState(elevator.getDoorOpen()); //Set to new state
    }

    @Override
    public void closeDoor() {
        System.out.println("Door already closed for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void loadElevator(long time) {
        //Open door, then load elevator
        System.out.println("Opening Door for Elevator "+elevator.getElevatorNumber());
        elevator.setDoors(true);
        this.elevator.setElevatorState(elevator.getDoorOpen());
        this.elevator.loadElevator(time);
    }

    @Override
    public void elevatorArrived() {
        System.out.println("Already arrived for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void elevatorOutOfService() {
        elevator.setDoors(true);
        elevator.setElevatorState(elevator.getOutOfService()); //Set to new state
        System.out.println("Elevator "+elevator.getElevatorNumber()+" is going Out of Service");
    }

}
