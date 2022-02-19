package ElevatorStates;

import elevatorSystem.ElevatorCar;
import elevatorSystem.ElevatorState;

public class DoorOpen implements ElevatorState {

    private ElevatorCar elevator;

    public DoorOpen(ElevatorCar elevator){
        this.elevator = elevator;
    }

    @Override
    public void moveElevator(long time) {

    }

    @Override
    public void openDoor() {

    }

    @Override
    public void closeDoor() {

    }

    @Override
    public synchronized void loadElevator(long time) {
        int elevatorNumber = elevator.getElevatorNumber();
         try{
             wait(time);
             elevator.setDoors(false);
             System.out.println("Loading Elevator "+elevatorNumber+" completed, Door closed.");
         }
         catch (Exception e){
             System.out.println("Error occured while loading Elevator in thread.");
             e.printStackTrace();
         }
    }

    @Override
    public void elevatorArrived() {

    }
}
