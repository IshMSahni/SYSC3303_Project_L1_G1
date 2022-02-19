package ElevatorStates;

import elevatorSystem.*;
import elevatorSystem.ElevatorState;

import java.util.ArrayList;

public class MovingUp implements ElevatorState {

    private ElevatorCar elevator;

    public MovingUp(ElevatorCar elevator){
        this.elevator = elevator;
    }

    @Override
    public void moveElevator(long time) {
        System.out.println("Elevator already Moving Up for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void openDoor() {
        System.out.println("Can Not open door, Elevator Moving Up for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void closeDoor() {
        System.out.println("Door already closed and Moving Up for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void loadElevator(long time) {
        System.out.println("Can Not load and Moving Up for Elevator "+elevator.getElevatorNumber());
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
    public ElevatorState Elevator_NextState() {
        ElevatorState arr = new Arrived(this.ele);
        return arr;
    }

    @Override
    public void Direction(boolean check) {

    }

    /*
        elevator.setMotors(false);
		elevator.setStatus("Stopped");
		//Set elevator to new position and remove task from Elevator's queue
		elevator.setPosition(endLocation);
		elevator.setDoors(true);
		elevator.setButton(endLocation, false);
		elevator.getTasks().remove(0);
		elevators.set(elevatorNumber,elevator);
		System.out.println("Elevator "+elevatorNumber+" now at floor "+endLocation+", Door Opening.");
     */
}
