package tests;

import elevatorSystem.Elevator_System;
import elevatorSystem.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class Test_Elevator_System {

	
	@Test
	public void testMovingElevator() {
		Floor_System floor_system = new Floor_System(3,1);
		Floor[] floors = floor_system.getFloors();
		Task task = new Task(0, 1);
		Elevator_System elevator_system = new Elevator_System(1,2);
		ElevatorCar elevator = elevator_system.getElevators()[0];
		elevator.setPosition(3);
		Scheduler_System ss = new Scheduler_System();
		ss.setElevator_system(elevator_system);
		ss.addToQueue(task);
		assert(elevator.getPosition() == 1);
	}
	

}
