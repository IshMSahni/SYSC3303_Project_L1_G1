
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
		ElevatorCar elevator = new ElevatorCar(1,10);
		elevator.setPosition(2);
		assert(elevator.getPosition() == 2);
	}
	
	@Test
	public void testButtonPressed(){
		ElevatorCar elevator = new ElevatorCar(2,10);
		elevator.testbuttonPressed(0);
		
		assert(elevator.getLights(0));
		assert(elevator.getButton(0));
	}
	
	@Test
	public void testMovingElevator(){
		ElevatorCar elevator = new ElevatorCar(3,10);
		ArrayList <ElevatorAction> tasks = new ArrayList<>();
		tasks.add(new ElevatorAction(2,1));
		elevator.setTasks(tasks);
		elevator.movingElevator();
		assert(elevator.getPosition() == 2);
	}

	@Test
	public void changeElevatorState(){
		ElevatorCar elevator = new ElevatorCar(4,10);
		ArrayList <ElevatorAction> tasks = new ArrayList<>();
		tasks.add(new ElevatorAction(2,1));
		elevator.setTasks(tasks);
		elevator.movingElevator();
		elevator.setElevatorState(elevator.getOutOfService());
		assert (elevator.getElevatorState() == elevator.getOutOfService());
	}

}
