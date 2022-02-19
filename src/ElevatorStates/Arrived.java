package ElevatorStates;

import elevatorSystem.ElevatorCar;
import elevatorSystem.ElevatorState;

public class Arrived implements ElevatorState {
    ElevatorCar elevatorCar;
    public Arrived(ElevatorCar elevator){

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
    public void Elevator_NextState() {

    }
}
