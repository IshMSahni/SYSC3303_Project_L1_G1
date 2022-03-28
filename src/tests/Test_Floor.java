package tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import elevatorSystem.Floor;
import elevatorSystem.Floor_System;

public class Test_Floor {

	Floor_System floor_system = new Floor_System(1,1);
	Floor x = floor_system.getFloors()[0];
	
	@Test
	public void test_changeLamp() {
		x.changeLamp(0, 1);
		
		assertEquals(1, x.getLamp()[0]);	
	}
}