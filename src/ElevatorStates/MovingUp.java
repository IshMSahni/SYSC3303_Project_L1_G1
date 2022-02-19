package ElevatorStates;

import elevatorSystem.*;
import elevatorSystem.ElevatorState;

import java.util.ArrayList;

public class MovingUp implements ElevatorState {

    ElevatorCar ele;
    CalculateTime ct;
    ArrayList<ElevatorCar> system;
    public MovingUp(ElevatorCar ele){
        this.ele = ele;
    }


    @Override
    public void setState(ElevatorCar elevator, ArrayList<ElevatorCar> system) {
        Float startLocation = elevator.getPosition();
        Integer endLocation = elevator.getTasks().get(0);

        long time = ct.CalculateTime(startLocation, endLocation);
        elevator.setMotors(true);
        System.out.println("Elevator "+ ele.getElevatorNumber()+" now moving to floor "+endLocation);
        //Wait for calculated time
        try{ wait(time); }
        catch (Exception e){}
    }

    public void Elevator_MovingUp(ElevatorCar elevator, ArrayList<ElevatorCar> system) {

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
