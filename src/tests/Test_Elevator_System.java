package tests;

import elevatorSystem.Elevator_System;
import elevatorSystem.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class Test_Elevator_System {

	
	@Test
	public void testMovingElevator() throws InterruptedException {
		Elevator_System elevator_system = new Elevator_System(1,4, false);
		ElevatorCar elevator = elevator_system.getElevators()[0];
		elevator_system.elevatorRunningSupportStartUp();
		Thread elevatorSystemMainThread = new Thread(elevator_system, "Scheduler Simulation");
		Floor floor= new Floor(4,1);
			Task task = new Task(4);
		Scheduler_System scheduler_SubSystem = new Scheduler_System(1, 4);
		Thread schedulerSystemThread = new Thread(scheduler_SubSystem, "Scheduler Simulation");
		schedulerSystemThread.start();
		byte data[] = new byte[3];
		data[0] = (byte) 0;
		data[1] = (byte) 0;
		data[2] = (byte) 4;
		data[2] = (byte) 5;
		elevatorSystemMainThread.start();
		scheduler_SubSystem.sendData(data, 20);
		ArrayList <Integer> check = new ArrayList<>();
		check.add(5);
		System.out.println();
		assert(check == check);
	}


}
