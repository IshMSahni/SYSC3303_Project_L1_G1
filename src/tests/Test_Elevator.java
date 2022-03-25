package tests;

import elevatorSystem.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class Test_Elevator {

	
	
	@Test
	public void testElevatorCreation() {
		Elevator_System elevator_system = new Elevator_System(1,2,false);
		ElevatorCar elevator = elevator_system.getElevators()[0];
		assertNotNull(elevator);
		assert(elevator.getPosition() == 0);	
	}
	
	@Test
	public void testGetFloor(){
		Elevator_System elevator_system = new Elevator_System(1,2,false);
		ElevatorCar elevator = elevator_system.getElevators()[0];
		elevator.setPosition(2);
		assert(elevator.getPosition() == 2);
	}
	
	@Test
	public void testButtonPressed(){
		Elevator_System elevator_system = new Elevator_System(1,2,false);
		ElevatorCar elevator = elevator_system.getElevators()[0];
		elevator.testbuttonPressed(0);
		
		assert(elevator.getLights(0));
		assert(elevator.getButton(0));
	}
	
	@Test
	public void testSchedulerSystem(){
		Elevator_System elevator_system = new Elevator_System(1,2,false);
		ElevatorCar elevator = elevator_system.getElevators()[0];
	}

}
