package tests;
import static org.junit.jupiter.api.Assertions.*;

import elevatorSystem.Floor;
import elevatorSystem.Floor_System;
import org.junit.jupiter.api.Test;

class Test_Floor {

	Floor_System floor_system = new Floor_System(1,1);
	Floor x = floor_system.getFloors()[0];
	
	@Test
	void test_Floor_Constructor() {
		Floor y = new Floor(1,1,floor_system);
		assertEquals(false, y.getUpButton());	
		assertEquals(false, y.getDownButton());	
	}
	
	@Test
	void test_changeLamp() {
		x.changeLamp(0, 1);
		
		assertEquals(1, x.getLamp()[0]);	
	}

}
