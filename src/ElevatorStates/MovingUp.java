package ElevatorStates;

import elevatorSystem.*;
import elevatorSystem.ElevatorState;

import java.util.ArrayList;

public class MovingUp implements ElevatorState {

    ElevatorCar elevator;
    CalculateTime ct;
    ArrayList<ElevatorCar> system;
    public MovingUp(ElevatorCar ele, ArrayList<ElevatorCar> system){

        this.elevator = ele;
        this.system = system;

    }

    @Override
    public void Elevator_Loading() {

    }

    @Override
    public void Elevator_Arrived() {

    }

    @Override
    public void Elevator_DoorOpen() {

    }

    @Override
    public void Elevator_DoorClosed() {

    }

    @Override
    public void Elevator_MovingUp(ElevatorCar elevator) {
        Float startLocation = elevator.getPosition();
        Integer endLocation = elevator.getTasks().get(0);

        long time = ct.CalculateTime(startLocation, endLocation);
        elevator.setMotors(true);
        System.out.println("Elevator "+ ele.getElevatorNumber()+" now moving to floor "+endLocation);
        //Wait for calculated time
        try{ wait(time); }
        catch (Exception e){}

    }

    @Override
    public void Elevator_MovingDown(ElevatorCar elevator) {

    }

    @Override
    public void Elevator_NextState() {

    }
}
