# SYSC3303_Project_L1_G1

## Steps to install:

Import the Iteration 1 into IDE

### Steps to run the program:
1. Import repo on eclipse

a. Paste github url (https://github.com/KeithLam101147900/SYSC3303_Project_L1_G1.git)

b. Type in User and API key (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)

c. Create new project
	
2. Make sure .java files are under src (source folder)
3. Run the main method from Floor_System.java file

### Steps to run the Java Tests:
1. Right click the test package in the project explorer
2. Click Run As -> JUnit Test


## Files and Description:

### Elevator_System.java
- System responsible for handling requests made by the Scheduler to then tell which chosen elevator to move where
- Keeps track of the work that each elevator has to do in a queue
- Ensures that the motion and timing of the completion of the elevator is realistic
- Interacts with elevator states depending on action that needs to occur

### ElevatorCar.java
- Object pertaining to requirements of every ElevatorCar such as buttons, lights, motors, doors etc.
- Objects are managed by the Elevator_System to indicate where the elevator should travel
- Contains the state of the elevator and also changes based on calls to the methods within the state

### Floor_System.java
- Reads input from filename.txt
- Note that document called "filename" follows the following format:


	00:00:23.101 2 Down 1
	00:00:53.101 3 Up 5
	00:01:53.101 0 Up 5
hr:min:sec.mm | Current_Floor | Direction_of_Destination | Destination Floor

- Parses each line into People objects and adds them to queue for scheduler
- Once it reaches the end of the file it will return a list of People objects
- Manages the outputs/inputs on each floor relative to the position of the elevator and people waiting on the floor
- Initializes the program (main method located in Floor_System)
- Note that Floor_System constructor has been created for JUnit Testing only.

### Floor.java
- Creates floors of the building for a Person to be located on/have a destination to
- Outputs the interactions with the floor
- Note that Lamps array first element is for the elevator number and the second element int values are assigned as:


	 0 = off
	 1 = up
	 2 = down

- Note that Button int values are assigned as:

	 0 = off
	 1 = up
	 2 = down
	 3 = both up and down

### Person.java
- Object for holding the interactions that a Person would be having with the elevator
- Object is used to call elevator and is handled by the Floor_System and Scheduler_System

### Scheduler_System.java
- Handles all requests in Task objects that delegate the Floor_System and the Elevator_System what to do.
- Manages, sends, and removes interactions with the system from the queue after the interaction is complete

### Task.java
- Object that is made to represent the task given to the scheduler.
- Can handle requests from both elevator and floor to then be red by the Scheduler_System.

### Task.java
- Object that is made to represent the task given to the scheduler.
- Can handle requests from both elevator and floor to then be red by the Scheduler_System.

### ElevatorState.java
- An interface that outlines the methods that will be used by all the states that implement this interface.

### Arrived.java
- A class that implements ElevatorState
- This state defines what happens when the elevator arrives on the correct floor and different methods that you can use to change the state of the elevator
- The next state for this is DoorOpen

### DoorClosed.java
- A class that implements ElevatorState
- This state defines what happens when the elevator door is closed and its implementation on how different methods interact with the elevator in this state
- The next state for this is either DoorOpen, MovingUp, or MovingDown

### DoorOpen.java
- A class that implements ElevatorState
- This state defines what happens when the elevator door is open and its implementation on how different methods interact with the elevator in this state
- The next state for this is either DoorClosed or Loading
- As per design specifications, this is the default state of the elevator to be at when there are no other tasks for the elevator to handle

### Loading.java
- A class that implements ElevatorState
- This state defines what happens when the elevator is loading people and its separate implementation on how different methods interact with the elevator in this state
- The next state for this is DoorClosed

### MovingDown.java
- A class that implements ElevatorState
- This state defines what happens when the elevator is in motion while going down floors and its separate implementation on how different methods interact with the elevator in this state
- The next state for this is Arrived

### MovingUp.java
- A class that implements ElevatorState
- This state defines what happens when the elevator is in motion while going up floors and its separate implementation on how different methods interact with the elevator in this state
- The next state for this is Arrived

## Team & Contributions
1. Keith Lam - 
2. Muhammad Furqan - 
3. Ishanov Sahni - 