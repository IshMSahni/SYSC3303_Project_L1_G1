package ElevatorStates;

import elevatorSystem.ElevatorCar;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import elevatorSystem.ElevatorState;

public class DoorOpen implements ElevatorState {

    private ElevatorCar elevator;

    public DoorOpen(ElevatorCar elevator){
        this.elevator = elevator;
    }

    @Override
    public void moveElevator(long time) {
        System.out.println("Can Not move, Door is open for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void openDoor() {
        System.out.println("Door already open for Elevator "+elevator.getElevatorNumber());
    }

    @Override
    public void closeDoor() {
        System.out.println("Closing door for Elevator "+elevator.getElevatorNumber());
        elevator.setDoors(false);
        elevator.setElevatorState(elevator.getDoorClosed()); // set to new state
    }

    @Override

    public synchronized void loadElevator(long time) {
        int elevatorNumber = elevator.getElevatorNumber();
        try{
            wait(time);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println("Loading Elevator "+elevatorNumber+" completed at Time: "+dtf.format(now));
            elevator.setElevatorState(elevator.getLoading()); //Set to new state
        }
        catch (Exception e){
            System.out.println("Error occured while loading Elevator in thread.");
            e.printStackTrace();
        }


    }

    @Override
    public void elevatorArrived() {
        System.out.println("Can Not arrive for Elevator "+elevator.getElevatorNumber());

    }

    @Override
    public void elevatorOutOfService() {

    }
}