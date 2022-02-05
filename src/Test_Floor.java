import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Test_Floor {

	Floor x = new Floor();
	
	@Test
	void test_Floor_Constructor() {
		Floor y = new Floor();	
		assertEquals(false, y.getUpButton());	
		assertEquals(false, y.getDownButton());	
	}
	
	@Test
	void test_changeLamp() {
		x.changeLamp(0, 1);
		
		assertEquals(1, x.getLamp()[0]);	
	}

}
