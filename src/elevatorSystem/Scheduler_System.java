package elevatorSystem;
import java.io.*;
import java.net.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Scheduler_System class. Simulates a scheduler system for an Elevator system.
 * elevators attribute to store the list of Elevators
 * floors attribute to store the list of Floors
 * elevator_system attribute to store a reference to the Elevator_System
 * tasksQueue attribute to store a list Tasks added to schedule
 * scheduledQueue attribute to store a list of scheduled Floor numbers to visit (Value) for each Elevator (Key)
 * isNewTaskScheduled attribute to store a boolean value of true if a new task is scheduled, else false
 * targetElevatorNumber attribute to store an integer number of the elevator selected to be scheduled
 */
public class Scheduler_System implements Runnable{
    private ElevatorCar[] elevators;
    private ArrayList<Task> tasksQueue;
    private HashMap<Integer,ArrayList<ElevatorAction>> scheduledQueue;
    private int targetElevatorNumber;
    private DatagramPacket sendPacket, receivePacket; // UDP packets and sockets for send and recieving
    private DatagramSocket sendSocket, receiveSocket;

    /** Constructor for Scheduler_System */
    public Scheduler_System(int totalElevatorNumber, int totalFloorNumber){
        this.tasksQueue = new ArrayList<>();
        this.scheduledQueue = new HashMap<>();
        targetElevatorNumber = 0;
        this.elevators = new ElevatorCar[totalElevatorNumber];

        for (int i = 0; i < totalElevatorNumber; i++) {
            this.scheduledQueue.put(i,new ArrayList<>());
            this.elevators[i] = new ElevatorCar(i,totalFloorNumber);
        }

        try {
            //Create and send and recieve socket.
            receiveSocket = new DatagramSocket(10); // Scheduler_System will recieve data on Port 10
            sendSocket = new DatagramSocket(40); //If data is recieved from Port 40 then it from Scheduler_System

        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    public synchronized void scheduling(){
        // Receive Data
        System.out.println("Scheduler_System: Waiting for New Task......");
        byte data[] = recieveData(receiveSocket);

        //Re-construct Task object from recieved data
        Task task;

        //Data is elevator arrival
        if(data[0] == (byte) 7){
            System.out.println("Scheduler_System: Elevator "+data[1]+" has arrived at floor "+data[2]);
            System.out.println("Updating queues....");
            this.removeElevatorTask(data[1], data[2]); //removeElevatorTask request
        }
        //Data is Out of service elevator status update
        else if(data[0] == (byte) 8){
            int elevatorNum = data[1];
            System.out.println("\n ELEVATOR "+elevatorNum+" IS OUT OF SERVICE \n");
            elevators[elevatorNum].setElevatorState(elevators[elevatorNum].getOutOfService());
        }
        //Data is scheduling task data
        else {
            byte taskType;
            //Print time
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println("Scheduler_System: Task received at Time: "+dtf.format(now));
            if(data[0] == (byte) 0){
                taskType = (byte) 0;
                this.targetElevatorNumber = data[1];
                task = new Task(data[1],data[2]);
            }
            else if(data[0] == (byte) 1){
                taskType = (byte) 0;
                int time[] = new int[4];
                time[0] = data[3]; time[1] = data[4]; time[2] = data[5]; time[3] = data[6];
                this.targetElevatorNumber = data[1];
                task = new Task(time, data[1],data[2]);
            }
            else if(data[0] == (byte) 4){
                taskType = (byte) 0;
                task = new Task(targetElevatorNumber, data[1]);
            }
            //Bug Task
            else if (data[0] == (byte) 5){
                taskType = (byte) 3;
                String buttonStatusTemp = "";
                int direction = data[1];
                //Assign buttonStatustemp
                if (direction == 1) { buttonStatusTemp = "Up";}
                else if (direction == 2) { buttonStatusTemp = "Down"; }
                int time[] = new int[4];
                time[0] = data[4]; time[1] = data[5]; time[2] = data[6]; time[3] = data[7];

                task = new Task(time,buttonStatusTemp,data[2],data[3]);
            }
            //Bug Task
            else if (data[0] == (byte) 6){
                taskType = (byte) 3;
                String buttonStatusTemp = "";
                int direction = data[1];
                //Assign buttonStatustemp
                if (direction == 1) { buttonStatusTemp = "Up";}
                else if (direction == 2) { buttonStatusTemp = "Down"; }

                task = new Task(buttonStatusTemp,data[2],data[3]);
            }
            //Floor Task
            else {
                taskType = (byte) 2;
                String buttonStatusTemp = "";
                int direction = data[1];
                //Assign buttonStatustemp
                if (direction == 1) { buttonStatusTemp = "Up";}
                else if (direction == 2) { buttonStatusTemp = "Down"; }

                if(data[0] == (byte) 2){ task = new Task(buttonStatusTemp,data[2]); }
                else{
                    int time[] = new int[4];
                    time[0] = data[3]; time[1] = data[4]; time[2] = data[5]; time[3] = data[6];
                    task = new Task(time, buttonStatusTemp,data[2]);
                }
            }

            //Update elevator position dataset being used by Scheduler_System
            this.sendElevatorPositionsRequest();
            //Add reconstructed Task to queue and schedule it
            this.addToQueue(task, taskType);
        }
    }

    /**Get Scheduled tasks from Scheduler for a given elevator number*/
    public synchronized ArrayList<ElevatorAction> getScheduledQueue(Integer elevatorNumber){return this.scheduledQueue.get(elevatorNumber);}

    /**Add task to queue for Scheduler and schedule it*/
    public void addToQueue(Task task, byte taskType){
        if(task.getIsFloorTask()){
            System.out.println("New Task added to queue" + ", Target Floor: "
                    + task.getFloorNumber());
        }
        else{System.out.println("New Task added to queue"+ ", Target Floor: "
                + task.getFloorNumber());
        }
        tasksQueue.add(task);
        scheduleTask(task,taskType);
    }

    /**Remove First task from queue of a given elevator number*/
    public void removeElevatorTask(int elevatorNumber, int floorNumber){
        while((scheduledQueue.get(elevatorNumber).size() != 0) && scheduledQueue.get(elevatorNumber).get(0).getTargetFloor() == floorNumber) {
            scheduledQueue.get(elevatorNumber).remove(0);
        }
    }

    /** Schedule a given Task based off elevators & floors data */
    public void scheduleTask(Task task, byte taskType){
        //Get All Elevators position and status
        Integer bestElevatorNumber = 0;
        Integer bestTaskNumber;

        //Floor task
        if(task.getIsFloorTask()){
            if(elevators.length > 1) {
                bestElevatorNumber = bestElevatorNumber();
                if(bestElevatorNumber == -1){
                    bestTaskNumber = 0;
                }
                else{bestTaskNumber = getTaskNumber(bestElevatorNumber);}
            }
            else {
                bestTaskNumber = getTaskNumber(0);
            }
        }
        //Elevator Task
        else{
            bestElevatorNumber = task.getElevatorNumber();
            bestTaskNumber = getTaskNumber(bestElevatorNumber);
        }

        //Check bestElevatorNumber
        if(bestElevatorNumber == -1){
            System.out.println("Scheduler: No elevator found, Task did not schedule");
        }
        else {
            //Add task to scheduled Task list for best elevator number
            bestTaskNumber = Math.abs(bestTaskNumber);
            boolean taskIsBug = task.getIsDelayed() || task.getisDoorStuckClosed() || task.getIsDoorStuckOpen();
            int bugNumber = 0;
            if(task.getIsDelayed() ){bugNumber = -30;}
            else if(task.getisDoorStuckClosed() ){bugNumber = -40;}
            else if(task.getIsDoorStuckOpen() ){bugNumber = -50;}

            ArrayList<ElevatorAction> queue = this.scheduledQueue.get(bestElevatorNumber);

            int numPeople;
            if(taskIsBug){ numPeople = bugNumber;}
            else if(task.getIsFloorTask()){ numPeople = 1; }
            else{ numPeople = -1; }

            if (bestTaskNumber < (queue.size() - 1)) {
                queue.add(bestTaskNumber, new ElevatorAction(task.getFloorNumber(),numPeople));
            } else {
                queue.add(new ElevatorAction(task.getFloorNumber(),numPeople));
            }
            
            byte data[];
            //Convert new to byte[] format
            data = new byte[(queue.size()*2) + 2];
            data[0] = taskType; // This will tell Elevator_System that this data is a scheduled task.
            data[1] = (byte) bestElevatorNumber.intValue();
            int i = 0;
            for (int j = 0; j < queue.size();j++) {
                data[i + 2] = (byte) queue.get(j).getTargetFloor();
                data[i + 3] = (byte) queue.get(j).getNumPeople();
                i += 2;
            }
            this.scheduledQueue.replace(bestElevatorNumber, queue);
            targetElevatorNumber = bestElevatorNumber;

            System.out.println("Scheduler: sending Scheduled Task data to Elevator_System.");
            System.out.println("Containing Bytes: " + Arrays.toString(data) + "\n");
            this.sendData(data, 20);
        }
    }

    /** This method will send scheduled task data to Elevator_System*/
    public void sendData(byte data[], int portNumber){
        //create the datagram packet for the message with Port given
        try {
            sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), portNumber);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Send the data.
        try {
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /** Method to receieveData at the receiveSocket given*/
    public byte[] recieveData(DatagramSocket receiveSocket) {
        // Construct a DatagramPacket for receiving packets for the data reply
        byte data[] = new byte[100];
        receivePacket = new DatagramPacket(data, data.length);
        try {
            // Wait until data reply is received via receiveSocket.
            receiveSocket.receive(receivePacket);
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Process the received datagram.
        int len = receivePacket.getLength();
        byte tempData[] = new byte[len];
        for(int i = 0; i < len; i++) {
            tempData[i] = data[i];
        }
        data = tempData;

        return data;
    }

    /** Method to send elevators current positions request and update*/
    public void sendElevatorPositionsRequest(){
        byte data[] = new byte[1];
        data[0] = (byte) 1;
        this.sendData(data,20);

        // Receive Elevator data
        data = recieveData(sendSocket);
        int len = data.length;

        //If Correct data type received
        if(data[0] == (byte) 4){
            for (int i = 1; i < len; i++) {
                int position = data[i];
                Integer floatPosition = position;
                elevators[i-1].setPosition(floatPosition.floatValue());
            }
        }
    }

    /**
     * Method to determine best Elevator Number to use.
     * Prefers Elevators that has empty task queue (when currentWeightFactor = TaskNumber = 0)
     * Or Elevators that already have that task in their queue (when currentTaskNumber < 0).
     * Then it prefers Elevators that have lowest WeightFactor which is based on number of passengers and taskNumber
     */
    public int bestElevatorNumber() {
        int bestElevatorNumber = -1;
        int bestWeightFactor = -1;

        for (int i = 0; i < elevators.length; i++) {
            if(!elevators[i].getElevatorState().equals(elevators[i].getOutOfService())) {
                int currentTaskNumber = getTaskNumber(i);
                int passengerNumber = elevators[i].getNumPassengerCounter();
                int currentWeightFactor = currentTaskNumber * (passengerNumber + 1);

                if ((currentTaskNumber < 0) || (currentWeightFactor == 0)) {
                    return i;
                }

                if(bestElevatorNumber == -1){
                    bestElevatorNumber = i;
                    bestWeightFactor = currentWeightFactor;
                }
                else if (bestWeightFactor > currentWeightFactor) {
                    bestElevatorNumber = i;
                }
            }
        }
        return bestElevatorNumber;
    }

    /** Method to get position of task if added to a given elevator number to schedule most the recent task
     * Returns a negative number if target floor is already in queue */
    public int getTaskNumber(Integer elevatorNumber){
        //Target Floor number of latest task added and status of elevator.
        Integer targetFloorNumber = tasksQueue.get(tasksQueue.size() - 1).getFloorNumber();
        Float elevatorPosition = elevators[elevatorNumber].getPosition();
        ArrayList<ElevatorAction> queue = this.scheduledQueue.get(elevatorNumber);
        Boolean condition1 = false;
        Boolean condition2 = false;
        Integer bestTaskNumber = 0;

        //Iterate task number until condition1 or condition2 becomes true, Or until queue.size reaches end.
        if(queue.size() != 0) {
            //Return -bestTaskNumber if target floor already in queue (to make sure it choosen)
            for (int i = 0; i < queue.size(); i++) {
                if (targetFloorNumber == queue.get(i).getTargetFloor()) {
                    return -i;
                }
            }
            // Else loop again to find best position
            for (int j = 0; !condition1 && !condition2 && (j < queue.size()); j++) {
                Integer queueFloor = queue.get(j).getTargetFloor();
                condition1 = (elevatorPosition > queueFloor) && (targetFloorNumber > queueFloor) && (elevatorPosition > targetFloorNumber);
                condition2 = (elevatorPosition < queueFloor) && (targetFloorNumber < queueFloor) && (elevatorPosition < targetFloorNumber);
                bestTaskNumber = j;
            }
        }
        return bestTaskNumber;
    }

    /** Getter method for targert Elevator Number */
    public Integer getTargetElevatorNumber(){return targetElevatorNumber;}

    public void setTasksQueue(ArrayList<Task> tasks){this.tasksQueue = tasks;}
    public void setScheduledQueue(HashMap<Integer,ArrayList<ElevatorAction>> tasks){this.scheduledQueue = tasks;}

    /** run method for Scheduler Thread */
    @Override
    public synchronized void run() {
        while (true){
            //Wait until new task is added, schedule it and send data to Elevator_System
            this.scheduling();
        }
    }

    /** Main method*/
    public static void main(String[] args) {
        int totalElevatorNumber = 4;
        int totalFloorNumber = 22;
        Scheduler_System scheduler_SubSystem = new Scheduler_System(totalElevatorNumber, totalFloorNumber);
        Thread schedulerSystemThread = new Thread(scheduler_SubSystem, "Scheduler Simulation");
        schedulerSystemThread.start();
    }
}
