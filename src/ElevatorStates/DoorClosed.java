package ElevatorStates;

import elevatorSystem.ElevatorCar;
import elevatorSystem.ElevatorState;

public class DoorClosed implements ElevatorState {

    private ElevatorCar elevator;

    public DoorClosed(ElevatorCar elevator){
        this.elevator = elevator;
    }

    @Override
    public synchronized void moveElevator(long time) {
        int elevatorNumber = elevator.getElevatorNumber();
        int endLocation = elevator.getTasks().get(0);
        elevator.setMotors(true);
        System.out.println("Elevator "+elevatorNumber+" now moving to floor "+endLocation);
        //Wait for calculated time
        try{ wait(time); }
        catch (Exception e){}

        elevator.setMotors(false);
        elevator.setStatus("Stopped");
        //Set elevator to new position and remove task from Elevator's queue
        elevator.setPosition(endLocation);
        elevator.setDoors(true);
        elevator.setButton(endLocation, false);
        elevator.getTasks().remove(0);
        System.out.println("Elevator "+elevatorNumber+" now at floor "+endLocation+", Door Opening.");
    }

    @Override
    public void openDoor() {
        System.out.println("Door opening");
        elevator.setElevatorState(elevator.getDoorOpen());
    }

    @Override
    public void closeDoor() {
        System.out.println("Door already Closed");
    }

    @Override
    public synchronized void loadElevator(long time) {

    }

    @Override
    public void elevatorArrived() {

    }
}
