/**
 * Floor object
 */
public class Floor {
	private int floorNumber;
	private boolean isLast;
	private boolean upButton;
	private boolean downButton;
	private int[] lamps;
	
	public Floor() {
		upButton = false;
		downButton = false;
		
		//lamps = new int[getNumberOfElevators()];
		lamps = new int[2];
	}

	public void setFloor(int floorNumber) {
		this.floorNumber = floorNumber;
	}
	
    public int getFloorNumber() {
        return floorNumber;
    }
	
	public void setFloor(int floorNumber, boolean last) {
		this.isLast = last;
		this.floorNumber = floorNumber;
	}
	
	public boolean getUpButton() {
		return upButton;
	}
	
	public boolean getDownButton() {
		return downButton;
	}
	
	public void setUpButton(boolean upButton) {
		this.upButton = upButton;
	}
	
	public void setDownButton(boolean downButton) {
		this.downButton = downButton;
	}
	
	public void changeLamp(int elevator, int direction) {
		lamps[elevator] = direction;
	}
	
	public void printFloor() {
		System.out.println();
		System.out.print("Floor Number: " + floorNumber);
		
		if (floorNumber == 0) {
			System.out.println(" (Ground Floor)");
		} else if (isLast) {
			System.out.println(" (Top Floor)");
		} else {
			System.out.println();
		}
		
		if (!isLast) {
			System.out.println("\tUp Button: " + upButton);
		} 
		if (floorNumber != 0) {
			System.out.println("\tDown Button: " + downButton);
		}
		
		System.out.println("\tLamps:");
		for (int i = 0; i < lamps.length; i++) {
			if (lamps[i] == 0) {
				System.out.println("\t\tElevator " + i + ": Stopped");
			} else if (lamps[i] == 1) {
				System.out.println("\t\tElevator " + i + ": Going up");
			} else if (lamps[i] == 2) {
				System.out.println("\t\tElevator " + i + ": Going down");
			}
		}
	}
}
