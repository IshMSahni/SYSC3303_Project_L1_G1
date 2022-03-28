package tests;

import elevatorSystem.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class Test_Bugs {

    private Elevator_System elevator_system;
    private ElevatorCar elevator;

    @Test
    public void test_OutOfService(){
        this.elevator_system = new Elevator_System(1,20,false);
        elevator_system.elevatorRunningSupportStartUp();
        this.elevator = elevator_system.getElevators()[0];
        ArrayList<ElevatorAction> tasks = new ArrayList<>();
        tasks.add(new ElevatorAction(5,1));
        elevator.setTasks(tasks);
        Float startLocation = elevator.getPosition();
        Integer endLocation = 5;

        long time = elevator.calculateTime(startLocation,endLocation);
        elevator.moveElevator(time);
        elevator.elevatorArrived();
        elevator.setPosition(1);

        try{wait(2000);}
        catch (Exception e){e.printStackTrace();}

        assert(elevator.getElevatorState().equals(elevator.getOutOfService()));

    }

}
