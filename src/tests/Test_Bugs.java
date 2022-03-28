package tests;

import elevatorSystem.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class Test_Bugs {

    private Elevator_System elevator_system;
    private Scheduler_System scheduler_system;
    private ElevatorCar elevator;

    @Test
    /** Manually set position wrong after moving elevator and check if timing event catches the mistake
     * and sets state to Out of Service. */
    public synchronized void test_OutOfService(){
        this.elevator_system = new Elevator_System(1,20,false);

        Thread elevatorSystemMainThread = new Thread(elevator_system , "Scheduler Simulation");
        System.out.println("Waiting for new Task...");
        elevatorSystemMainThread.start();

        this.scheduler_system = new Scheduler_System(1,20);
        this.elevator = elevator_system.getElevators()[0];
        ArrayList<ElevatorAction> tasks = new ArrayList<>();
        tasks.add(new ElevatorAction(2,1));
        elevator.setTasks(tasks);
        Float startLocation = elevator.getPosition();
        Integer endLocation = 2;

        long time = elevator.calculateTime(startLocation,endLocation);
        elevator.moveElevator(time);
        elevator.elevatorArrived();
        elevator.setPosition(1);

        try{wait(2000);}
        catch (Exception e){e.printStackTrace();}

        assert(elevator.getElevatorState().equals(elevator.getOutOfService()));

    }
}
