package tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import elevatorSystem.Floor_System;
import elevatorSystem.Person;

public class Test_Floor_System {
	
	@Test
	public void test_buttonEvent() {
		Floor_System x = new Floor_System(3,1);
		x.buttonEvent(0, 1);
		x.buttonEvent(1, 2);
		
		assert(x.getFloors()[0].getUpButton());
		assert(x.getFloors()[1].getDownButton());
	}
}