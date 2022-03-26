package tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import elevatorSystem.Floor_System;
import elevatorSystem.Person;

public class Test_Floor_System {
	
	/**
	 * Test assumes using file given in submission: 'filename.txt'
	 */
	@Test
	public void test_readFile() {
		Person[] allPeople;
		allPeople = Floor_System.readFile();
		
		assertEquals(1, allPeople[2].getTime()[1]);
		assertEquals(5, allPeople[2].getDestination());
	}
	
	@Test
	public void test_buttonEvent() {
		Floor_System x = new Floor_System(3,1);
		x.buttonEvent(0, 1);
		x.buttonEvent(1, 2);
		
		assertEquals(true, x.getFloors()[0].getUpButton());
		assertEquals(true, x.getFloors()[1].getDownButton());
	}
}
