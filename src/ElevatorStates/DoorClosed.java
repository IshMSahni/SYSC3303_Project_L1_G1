package ElevatorStates;

import elevatorSystem.ElevatorCar;
import elevatorSystem.ElevatorState;
import elevatorSystem.Elevator_System;

import java.util.ArrayList;

public class DoorClosed implements ElevatorState {
        ElevatorCar e;
        Boolean check;

    public DoorClosed(ElevatorCar elevator){
        this.e = elevator;
        e.setDoors(false);
        e.setMotors(false);
    }

    public void Direction(boolean onHigherFloor){
        this.check = onHigherFloor;
    }


    @Override
    public void setState(ElevatorCar elevator, ArrayList<ElevatorCar> system) {

    }

    @Override
    public ElevatorState Elevator_NextState() {
        if(!check) {
            ElevatorState state = new MovingUp(e);
            return state;
        } else{
            ElevatorState state = new MovingDown(e);
            return state;
        }
    }

}
