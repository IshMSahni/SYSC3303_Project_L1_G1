package ElevatorStates;

import elevatorSystem.ElevatorCar;
import elevatorSystem.ElevatorState;
import elevatorSystem.Elevator_System;
import elevatorSystem.TimingEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        int endLocation = elevator.getTasks().get(0).getTargetFloor();
        int distance = Math.round(Math.abs(startLocation - endLocation));
        int amount = 0;
        elevator.setMotors(true);
        int passengers = elevator.getNumPassengerCounter();

        if(startLocation < endLocation){
            amount = 1;
            System.out.println("\nElevator "+elevatorNumber+" now Moving Up to floor "+endLocation+". Num Passengers: "+passengers);
            elevator.setElevatorState(elevator.getMovingUp());
        }
        else{
            amount = -1;
            System.out.println("\nElevator "+elevatorNumber+" now Moving Down to floor "+endLocation+". Num Passengers: "+passengers);
            elevator.setElevatorState(elevator.getMovingDown());
        }

        //Create Timer and start it with Estimated moving time + 0.5 second
        TimingEvent timingEvent = new TimingEvent(elevatorNumber,endLocation,(time + 500));
        Thread timerThread = new Thread(timingEvent,"Elevator Timer "+elevatorNumber);
        timerThread.start();

        //Wait for calculated time
        if(time != 0) {
            for (int i = 0; i < distance; i++) {
                try{ wait((time/distance) - 50); }
                catch (Exception e){}
                this.elevator.setPosition(this.elevator.getPosition() + amount);
            }
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
        //Open door, then load elevator
        System.out.println("\nElevator "+elevator.getElevatorNumber() +" door is stuck close, opening door again before loading.\n");
        elevator.setDoors(true);
        this.elevator.setElevatorState(elevator.getDoorOpen());
        this.elevator.loadElevator(time);
    }

    @Override
    public void elevatorArrived() {
        System.out.println("Can Not arrive, Door closed for Elevator "+elevator.getElevatorNumber());

    }

    @Override
    public void elevatorOutOfService() {
        elevator.setDoors(true);
        elevator.setElevatorState(elevator.getOutOfService()); //Set to new state
        System.out.println("Elevator "+elevator.getElevatorNumber()+" is going Out of Service");
    }

}
