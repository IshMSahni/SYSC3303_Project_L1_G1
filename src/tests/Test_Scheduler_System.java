package tests;

import elevatorSystem.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;


public class Test_Scheduler_System  {
    Floor[] floors;
    ElevatorCar[] elevators;
    Scheduler_System scheduler_system;
    Elevator_System elevator_system;

    public void setUp() throws Exception {
        Elevator_System elevator_system = new Elevator_System(1,10);
        elevators = elevator_system.getElevators();
        Scheduler_System ss= new Scheduler_System();
        ss.setElevator_system(elevator_system);
        Floor_System floor_system = new Floor_System(10,1);
        floors = floor_system.getFloors();
    }

    @Test
    /** Test elevator position to be at 3 after adding 1 Task, sorting, send signal to move */
    public void test_addToQueue() throws Exception {
        this.setUp();
        scheduler_system.addToQueue(new Task(0,3));
        assert(elevators[0].getPosition() == 3.0);
    }

    @Test
    /** Test elevator position to be at 2 after adding 3 Task, sorting, send signal to move */
    public void test_addToQueue2() throws Exception {
        this.setUp();
        scheduler_system.addToQueue(new Task(0,1));
        scheduler_system.addToQueue(new Task(0,4));
        scheduler_system.addToQueue(new Task(0,2));
        assert(elevators[0].getPosition() == 2.0);
    }

    @Test
    /** Test to see if getTaskNumber correctly returns number position of task*/
    public void test_getTaskNumber() throws Exception{
        this.setUp();
        ArrayList<Integer> tasks = new ArrayList<>();
        ArrayList<Task> newTask = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> scheduledTasks = new HashMap<>();
        tasks.add(2);tasks.add(4);tasks.add(7);
        elevators[0].setTasks(tasks);
        scheduledTasks.put(0,tasks);
        newTask.add(new Task(0,5));
        scheduler_system.setTasksQueue(newTask);
        scheduler_system.setScheduledQueue(scheduledTasks);

        assert(scheduler_system.getTaskNumber(0) == 2);
    }

    @Test
    /** Test to see if getTaskNumber correctly returns number position of task*/
    public void test_getTaskNumber2() throws Exception{
        this.setUp();
        ArrayList<Integer> tasks = new ArrayList<>();
        ArrayList<Task> newTask = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> scheduledTasks = new HashMap<>();
        tasks.add(5);tasks.add(7);tasks.add(3);tasks.add(0);
        elevators[0].setTasks(tasks);
        elevators[0].setPosition(2);
        scheduledTasks.put(0,tasks);
        newTask.add(new Task(0,1));
        scheduler_system.setTasksQueue(newTask);
        scheduler_system.setScheduledQueue(scheduledTasks);

        assert(scheduler_system.getTaskNumber(0) == 3);
    }

    @Test
    /** Test to see if getTaskNumber correctly returns number position of task*/
    public void test_getTaskNumber3() throws Exception{
        this.setUp();
        ArrayList<Integer> tasks = new ArrayList<>();
        ArrayList<Task> newTask = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> scheduledTasks = new HashMap<>();
        tasks.add(5);tasks.add(7);tasks.add(3);tasks.add(0);
        elevators[0].setTasks(tasks);
        elevators[0].setPosition(0);
        scheduledTasks.put(0,tasks);
        newTask.add(new Task(0,1));
        scheduler_system.setTasksQueue(newTask);
        scheduler_system.setScheduledQueue(scheduledTasks);

        assert(scheduler_system.getTaskNumber(0) == 0);
    }
}
