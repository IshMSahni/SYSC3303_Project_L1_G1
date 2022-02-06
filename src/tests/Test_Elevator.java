package tests;

import elevatorSystem.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class Test_Elevator {

	
	
	@Test
	public void testElevatorCreation() {
		ElevatorCar elevator = new ElevatorCar(0, 2);
		assertNotNull(elevator);
		assert(elevator.getPosition() == 0);	
	}
	
	@Test
	public void testGetFloor(){
		ElevatorCar elevator = new ElevatorCar(0, 2);
		elevator.setPosition(2);
		assert(elevator.getPosition() == 2);
	}
	
	@Test
	public void testButtonPressed(){
		ElevatorCar elevator = new ElevatorCar(0, 2);
		elevator.testbuttonPressed(0);
		
		assert(elevator.getLights(0));
		assert(elevator.getButton(0));
	}
	
	@Test
	public void testSchedulerSystem(){
		ArrayList<Floor> floors = new ArrayList<Floor>();
		Floor floor1 = new Floor(1);
		floor1.setFloor(0);
		Floor floor2 = new Floor(1);
		floor2.setFloor(1, true);
		floors.add(floor2);
		floors.add(floor1);
		ArrayList<ElevatorCar> arrEle = new ArrayList <ElevatorCar>();
		ElevatorCar elevator = new ElevatorCar(0, 2);
		arrEle.add(elevator);
		Scheduler_System ss= new Scheduler_System(arrEle,floors);
		elevator.setScheduler_system(ss);
		assert(elevator.getScheduler_system());
	}

}
