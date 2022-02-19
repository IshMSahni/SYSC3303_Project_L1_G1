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
        System.out.println("Can Not load elevator, Door not open for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void elevatorArrived() {
        System.out.println("Already arrived for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void Elevator_Loading() {
        
    }

    @Override
    public void ElevatorState(ElevatorCar ele) {
        this.elevatorCar = ele;
    }

    @Override
    public void Elevator_Arrived() {
        elevator.setMotors(false);
        elevator.setStatus("Stopped");
        //Set elevator to new position and remove task from Elevator's queue
        elevator.setPosition(endLocation);
        elevator.setDoors(true);
        elevator.setButton(endLocation, false);
        elevator.getTasks().remove(0);
        system.set(ele.getElevatorNumber(),elevator);
        System.out.println("Elevator "+ ele.getElevatorNumber()+" now at floor "+endLocation+", Door Opening.");
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

    @Override
    public ElevatorState Elevator_NextState() {

        return null;
    }
}
