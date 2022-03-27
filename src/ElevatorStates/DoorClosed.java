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
        elevator.setMotors(true);
        int passengers = elevator.getNumPassengerCounter();

        if(startLocation < endLocation){
            System.out.println("\nElevator "+elevatorNumber+" now Moving Up to floor "+endLocation+". Num Passengers: "+passengers);
            elevator.setElevatorState(elevator.getMovingUp());
        }
        else{
            System.out.println("\nElevator "+elevatorNumber+" now Moving Down to floor "+endLocation+". Num Passengers: "+passengers);
            elevator.setElevatorState(elevator.getMovingDown());
        }

        //Create Timer and start it with Estimated moving time + 2 seconds
        TimingEvent timingEvent = new TimingEvent(elevatorNumber,endLocation,(time + 2000));
        Thread timerThread = new Thread(timingEvent,"Elevator Timer "+elevatorNumber);
        timerThread.start();

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
        //Open door, then load elevator
        System.out.println("Opening Door for Elevator "+elevator.getElevatorNumber());
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
