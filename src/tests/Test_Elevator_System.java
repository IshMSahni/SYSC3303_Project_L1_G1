package tests;

import elevatorSystem.Elevator_System;
import elevatorSystem.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class Test_Elevator_System {

	
	@Test
	public void testMovingElevator() {
		ArrayList<Floor> floors = new ArrayList<Floor>();
		Floor floor1 = new Floor(1);
		floor1.setFloor(0);
		Floor floor2 = new Floor(1);
		floor2.setFloor(1, true);
		floors.add(floor2);
		floors.add(floor1);
		Task task = new Task(0, 1);
		ArrayList<ElevatorCar> arrEle = new ArrayList <ElevatorCar>();
		ElevatorCar elevator = new ElevatorCar(0, 3);
		Elevator_System elevator_system = new Elevator_System(arrEle);
		arrEle.add(elevator);
		Scheduler_System scheduler_system = new Scheduler_System(arrEle, floors);
		scheduler_system.setElevator_system(elevator_system);
		elevator_system.setSchedulerSystem(scheduler_system);
		scheduler_system.addToQueue(task);
		assert(elevator.getPosition() == 1);
	}
	

}
