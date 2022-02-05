package tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Test_Person {

	@Test
	void test_Person_Constructor() {
		int[] i = new int[4];
		i[0] = 0;
		i[1] = 1;
		i[2] = 2;
		i[3] = 3;
		
		Person x = new Person(i, 1, 2, 3);	
		assertEquals(i, x.getTime());	
		assertEquals(1, x.getFloorNumber());
		assertEquals(2, x.getDirection());
		assertEquals(3, x.getDestination());
	}

}
