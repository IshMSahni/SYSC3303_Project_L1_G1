package tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Test_Floor_System {
	
	/**
	 * Test assumes using file given in submission: 'filename.txt'
	 */
	@Test
	void test_readFile() {
		Person[] allPeople;	
		allPeople = Floor_System.readFile();
		
		assertEquals(1, allPeople[2].getTime()[1]);
		assertEquals(2, allPeople[2].getDestination());
	}
	
	@Test
	void test_buttonEvent() {
		Floor_System x = new Floor_System(3);
		x.buttonEvent(0, 1);
		x.buttonEvent(1, 2);
		
		assertEquals(true, x.getFloors()[0].getUpButton());
		assertEquals(true, x.getFloors()[1].getDownButton());
	}
	
	@Test
	void test_lampEvent() {
		Floor_System x = new Floor_System(3);
		x.lampEvent(1, 0, 2);
		
		assertEquals(2, x.getFloors()[1].getLamp()[0]);
	}

}
