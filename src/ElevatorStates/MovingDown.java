package ElevatorStates;

import elevatorSystem.ElevatorCar;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
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
        int endLocation = elevator.getTasks().get(0).getTargetFloor();
        elevator.setMotors(false);
        elevator.setStatus("Stopped");
        //Set elevator to new position and remove task from Elevator's queue
        elevator.setButton(endLocation, false);
        elevator.getTasks().remove(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Elevator "+elevator.getElevatorNumber()+" now arrived floor "+Math.round(this.elevator.getPosition())+" at Time: "+dtf.format(now));
        //Set to new state
        elevator.setElevatorState(elevator.getArrived());
    }

    @Override
    public void elevatorOutOfService() {
        elevator.setPosition(0);
        elevator.setDoors(true);
        elevator.setElevatorState(elevator.getOutOfService()); //Set to new state
        System.out.println("Elevator "+elevator.getElevatorNumber()+" is going Out of Service");
    }
}
