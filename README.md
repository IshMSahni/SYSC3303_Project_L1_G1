# SYSC3303_Project_L1_G1

## Steps to install:

Import the Iteration 1 into IDE

Steps to run the program:
1. Run the main method from Floor_System.java file

Steps to run the Java Tests:
1. Right click the test package in the project explorer
2. Click Run As -> JUnit Test


## Files and Description:

### Elevator_System.java
- System responsible for handling requests made by the Scheduler to then tell which chosen elevator to move where
- Keeps track of the work that each elevator has to do in a queue
- Ensures that the motion and timing of the completion of the elevator is realistic

### ElevatorCar.java
- Object pertaining to requirements of every ElevatorCar such as buttons, lights, motors, doors etc.
- Objects are managed by the Elevator_System to indicate where the elevator should travel

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
-- 0 = off
-- 1 = up
-- 2 = down
- Note that Button int values are assigned as:
-- 0 = off
-- 1 = up
-- 2 = down
-- 3 = both up and down

### Person.java
- Object for holding the interactions that a Person would be having with the elevator
- Object is used to call elevator and is handled by the Floor_System and Scheduler_System

### Scheduler_System.java
- Handles all requests in Task objects that delegate the Floor_System and the Elevator_System what to do.
- Manages, sends, and removes interactions with the system from the queue after the interaction is complete

### Task.java
- Object that is made to represent the task given to the scheduler.
- Can handle requests from both elevator and floor to then be red by the Scheduler_System.

## Team & Contributions
1. Keith Lam - Floor.java, Floor_System.java, Person.java & their test cases
2. Muhammad Furqan - Scheduler_System.java, Elevator_System.java, Task.java, UML Sequence Diagram
3. Ishanov Sahni - ElevatorCar.java, Elevator_System.java & their test cases, UML Class Diagram