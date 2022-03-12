package ElevatorStates;

import elevatorSystem.ElevatorCar;
import elevatorSystem.ElevatorState;
import elevatorSystem.Elevator_System;

import java.util.ArrayList;

public class DoorClosed implements ElevatorState {

    private ElevatorCar elevator;

    public DoorClosed(ElevatorCar elevator){

        this.elevator = elevator;
    }

    @Override
    public synchronized void moveElevator(long time) {
        int elevatorNumber = elevator.getElevatorNumber();
        float startLocation = elevator.getPosition();
        int endLocation = elevator.getTasks().get(0);
        elevator.setMotors(true);

        if(startLocation < endLocation){
            System.out.println("Elevator "+elevatorNumber+" now Moving Up to floor "+endLocation);
            System.out.println("Elevator "+elevatorNumber+" moving time "+ (time/1000) + " seconds.");
            elevator.setElevatorState(elevator.getMovingUp());
        }
        else{
            System.out.println("Elevator "+elevatorNumber+" now Moving Down to floor "+endLocation);
            System.out.println("Elevator "+elevatorNumber+" moving time "+ (time/1000) + " seconds.");
            elevator.setElevatorState(elevator.getMovingDown());
        }

        //Wait for calculated time
        if(time != 0) {
            try{ wait(time); }
            catch (Exception e){}
        }

    }

    @Override
    public void openDoor() {
        System.out.println("Door opening");
        elevator.setDoors(true);
        elevator.setElevatorState(elevator.getDoorOpen()); //Set to new state
    }

    @Override
    public void closeDoor() {
        System.out.println("Door already closed for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public synchronized void loadElevator(long time) {
        System.out.println("Can Not load, Door closed for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void elevatorArrived() {
        System.out.println("Can Not arrive, Door closed for Elevator "+elevator.getElevatorNumber());

    }

}
