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
    public synchronized void test_OutOfServiceBug(){
        this.elevator_system = new Elevator_System(1,20,false);
        Thread elevatorSystemMainThread = new Thread(elevator_system , "Elevator Simulation");
        System.out.println("Waiting for new Task...");
        elevatorSystemMainThread.start();

        ElevatorCar elevatorTest = elevator_system.getElevators()[0];
        ArrayList<ElevatorAction> tasks = new ArrayList<>();
        tasks.add(new ElevatorAction(2,1));
        elevatorTest.setTasks(tasks);
        Float startLocation = elevatorTest.getPosition();
        Integer endLocation = 2;

        long time = elevatorTest.calculateTime(startLocation,endLocation);
        elevatorTest.moveElevator(time);
        elevatorTest.elevatorArrived();

        //Setting wrong position manually
        elevatorTest.setPosition(1);

        try{wait(2000);}
        catch (Exception e){e.printStackTrace();}

        assert(elevator_system.getElevators()[0].getElevatorState().equals(elevatorTest.getOutOfService()));
    }

    @Test
    /** Manually set elevator to door closed and call loading and move*/
    public synchronized void test_doorStuckCloseBug(){
        this.elevator = new ElevatorCar(1,20);
        ArrayList<ElevatorAction> tasks = new ArrayList<>();
        tasks.add(new ElevatorAction(2,1));
        elevator.setTasks(tasks);
        Float startLocation = elevator.getPosition();
        Integer endLocation = 2;

        //Manually set Elevator state to door closed
        elevator.setElevatorState(elevator.getDoorClosed());
        //call loading (which normally expects door open before loading)
        elevator.loadElevator(2000);
        long time = elevator.calculateTime(startLocation,endLocation);
        //Move elevator and then assert if elevator did function correctly despite wrong state before loading
        elevator.closeDoor();
        elevator.moveElevator(time);
        elevator.elevatorArrived();

        assert(Math.round(elevator.getPosition()) == 2);
    }

    @Test
    /** Manually set elevator to arrived (which also has door closed) and call loading and move */
    public synchronized void test_doorStuckCloseBug2(){
        this.elevator = new ElevatorCar(2,20);
        ArrayList<ElevatorAction> tasks = new ArrayList<>();
        tasks.add(new ElevatorAction(3,1));
        elevator.setTasks(tasks);
        Float startLocation = elevator.getPosition();
        Integer endLocation = 3;

        //Manually set Elevator state to Arrived
        elevator.setElevatorState(elevator.getArrived());
        //call loading (which normally expects door open before loading)
        elevator.loadElevator(2000);
        long time = elevator.calculateTime(startLocation,endLocation);
        //Move elevator and then assert if elevator did function correctly despite wrong state before loading
        elevator.closeDoor();
        elevator.moveElevator(time);
        elevator.elevatorArrived();

        assert(Math.round(elevator.getPosition()) == 3);
    }

    @Test
    /** Manually set elevator state to door open call move */
    public synchronized void test_doorStuckOpenBug(){
        this.elevator = new ElevatorCar(3,20);
        ArrayList<ElevatorAction> tasks = new ArrayList<>();
        tasks.add(new ElevatorAction(1,1));
        elevator.setTasks(tasks);
        Float startLocation = elevator.getPosition();
        Integer endLocation = 1;
        long time = elevator.calculateTime(startLocation,endLocation);

        //Manually set Elevator state to door open and call move
        elevator.setElevatorState(elevator.getDoorOpen());
        //Move elevator and then assert if elevator did function correctly despite wrong state before moving
        elevator.moveElevator(time);
        elevator.elevatorArrived();

        assert(Math.round(elevator.getPosition()) == 1);
    }

}
