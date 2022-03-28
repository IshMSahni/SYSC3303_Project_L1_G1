# SYSC3303_Project_L1_G1

## Steps to install:

Import Iteration 4 into IDE

### Steps to run the program:

1. Import repo on eclipse

a) Paste github url (https://github.com/KeithLam101147900/SYSC3303_Project_L1_G1.git)

b) Pull/Clone main branch

c) Type in User and API key

2. If pulled/cloned files are not pulled as source folders in eclipse create new project

a) Make sure .java files are under src (source folder)

b) Make sure to name packages correctly

3. Run the main methods in the following order: Scheduler_System, Elevator_System, Floor_System.

4. To run the test files open any file in the test folder and run all or some of the tests.

### Steps to run the Java Tests:

1. Right click the test package in the project explorer
2. Click Run As -> JUnit Test
3. Run tests in Test_Bugs.java separately.

## Files and Description:

### Elevator_System.java

- System responsible for handling requests made by the Scheduler to then tell which chosen elevator to move where
- Keeps track of the work that each elevator has to do in a queue
- Ensures that the motion and timing of the completion of the elevator is realistic
- Interacts with elevator states depending on action that needs to occur
- Handles each car on a different thread

### ElevatorCar.java

- Object pertaining to requirements of every ElevatorCar such as buttons, lights, motors, doors etc.
- Objects are managed by the Elevator_System to indicate where the elevator should travel
- Contains the state of the elevator and also changes based on calls to the methods within the state
- Contains information about how many people are on and off the elevator as an indicator of task completion
- Each car is now a thread that is notified when it supposed to interact with the system
- The system has a OutOfService mode to ensure safe usage of the elevator and to minimize edge cases that can result in harm

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
- allPeople array has been changed to an ArrayList in order to accommodate for the iteration 2 requirements.
- Note that Floor_System now reads file with bug parameter for iteration 4

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
- Note that person has bug attribute for iteration 4 (Creating bugs for system to test)

### Scheduler_System.java

- Handles all requests in Task objects that delegate the Floor_System and the Elevator_System what to do.
- Manages, sends, and removes interactions with the system from the queue after the interaction is complete
- Manages which elevator to assign the task to based on the location of the elevator and the people.

### Task.java

- Object that is made to represent the task given to the scheduler.
- Can handle requests from both elevator and floor to then be red by the Scheduler_System.

### Task.java

- Object that is made to represent the task given to the scheduler.
- Can handle requests from both elevator and floor to then be red by the Scheduler_System.
- Note that Task has bug attribute for iteration 4 (Creating bugs for system to test)

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

### OutOfService.java

- A class that implements ElevatorState
- This state defines what happens when a bug/fault occurs and the elevater must now remain out of service until the issue is fixed.
- Note that this state has no next state

### ElevatorAction.java

- An instruction object for ElevatorCar to follow
- Holds information about the target floor and number of people travelling in each instruction

### TimingEvent.java

- A Threaded event that runs to measure the time it takes for each interaction to and handling it for bugs

## Team & Contributions for Iteration 3

1. Keith Lam - Bug input readFile changes, Request with bug modifications, JUnit testing, Readme.txt modifications
2. Muhammad Furqan - TimingEvent, JUnit Testing, ElevatorAction, ElevatorAction, OutOFService, Scheduler_System changes
3. Ishanov Sahni - UML Class Diagram, Readme.txt modifications, JUnit testing
