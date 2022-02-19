package ElevatorStates;

import elevatorSystem.ElevatorCar;
import elevatorSystem.ElevatorState;

public class MovingDown implements ElevatorState {

    private ElevatorCar elevator;

    public MovingDown(ElevatorCar elevator){
        this.elevator = elevator;
    }

    @Override
    public void moveElevator(long time) {
        System.out.println("Elevator already Moving Down for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void openDoor() {
        System.out.println("Can Not open door, Elevator Moving Down for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void closeDoor() {
        System.out.println("Door already closed, Elevator Moving Down for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void loadElevator(long time) {
        System.out.println("Can Not load, Elevator Moving Down for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void elevatorArrived() {
        int endLocation = elevator.getTasks().get(0);
        elevator.setMotors(false);
        elevator.setStatus("Stopped");
        //Set elevator to new position and remove task from Elevator's queue
        elevator.setPosition(endLocation);
        elevator.setButton(endLocation, false);
        elevator.getTasks().remove(0);
        System.out.println("Elevator "+elevator.getElevatorNumber()+" now arrived floor "+endLocation);
        //Set to new state
        elevator.setElevatorState(elevator.getArrived());
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
    public void Elevator_Moving(ElevatorCar elevator) {

    }

}
