package tests;

import elevatorSystem.Elevator_System;
import elevatorSystem.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class Elevator_System_Tests {

	
	public void setUp() throws Exception {
		ArrayList<Floor> floors = new ArrayList<Floor>();
		Floor floor1 = new Floor(1);
		Floor floor2 = new Floor(1);
		floors.add(floor2);
		floors.add(floor1);
		ArrayList<ElevatorCar> arrEle = new ArrayList <ElevatorCar>();
		ElevatorCar elevator = new ElevatorCar(0, 2);
		arrEle.add(elevator);
		final Scheduler_System scheduler_system = new Scheduler_System(arrEle, floors);
		arrEle.get(0).setScheduler_system(scheduler_system);
		Elevator_System elevator_system = new Elevator_System(arrEle);
	}

	
	public void tearDown() throws Exception {
	}

	@Test
	public void testElevatorCreation() {
		ElevatorCar elevator = new ElevatorCar(0, 2);
		assertNotNull(elevator);
		assert(elevator.getPosition() == 0);	
	}
	
	public void testMovingElevator() {
		ArrayList<Floor> floors = new ArrayList<Floor>();
		Floor floor1 = new Floor(1);
		floor1.setFloor(0);
		Floor floor2 = new Floor(1);
		floor2.setFloor(1);
		floors.add(floor2);
		floors.add(floor1);
		Task task = new Task(0, 1);
		ArrayList<ElevatorCar> arrEle = new ArrayList <ElevatorCar>();
		ElevatorCar elevator = new ElevatorCar(0, 1);
		arrEle.add(elevator);
		Scheduler_System scheduler_system = new Scheduler_System(arrEle, floors);
		scheduler_system.addToQueue(task);
		Elevator_System elevator_system = new Elevator_System(arrEle);
		elevator_system.updateElevatorQueue(0);
		elevator_system.moveElevator(0);
		assert(elevator.getPosition() == 1);
		
		
	}

	void testGetFloor(){
		ElevatorCar elevator = new ElevatorCar(0, 2);
		assert(elevator.getPosition() == 0);
	}

}
