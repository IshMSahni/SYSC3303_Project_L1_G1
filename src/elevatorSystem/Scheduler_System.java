package elevatorSystem;
import java.io.*;
import java.net.*;
import java.util.*;

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
    private Elevator_System elevator_system;
    private ArrayList<Task> tasksQueue;
    private HashMap<Integer,ArrayList<Integer>> scheduledQueue;
    private static Boolean isNewTaskScheduled;
    private static Integer targetElevatorNumber;
    private DatagramPacket sendPacket, receivePacket; // UDP packets and sockets for send and recieving
    private DatagramSocket sendSocket, receiveSocket;

    /** Constructor for Scheduler_System */
    public Scheduler_System(){
        isNewTaskScheduled = false;
        this.tasksQueue = new ArrayList<>();
        this.scheduledQueue = new HashMap<>();
        targetElevatorNumber = 0;
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
        // Construct a DatagramPacket for receiving packets for the data reply
        byte data[] = new byte[100];
        receivePacket = new DatagramPacket(data, data.length);

        try {
            // Wait until data reply is received via receiveSocket.
            System.out.println("Scheduler_System: Waiting for New Task......");
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
        int portRecievedFrom = receivePacket.getPort();

        System.out.println("Scheduler_System: Task received:");
        System.out.println("From: " + receivePacket.getAddress());
        System.out.println("Port: " + portRecievedFrom);
        System.out.println("Length: " + len);
        System.out.print("Containing: ");

        // Form a String from the byte array.
        String received = new String(data,0,len);
        System.out.println("String: " + received);
        System.out.println("Bytes: " + Arrays.toString(data) + "\n");

        //Re-construct Task object from recieved data
        Task task;
        if(data[0] == (byte) 0){
            task = new Task(data[1],data[2]);
        }
        else if(data[0] == (byte) 1){
            int time[] = new int[4];
            time[0] = data[3]; time[1] = data[4]; time[2] = data[5]; time[3] = data[6];
            task = new Task(time, data[1],data[2]);
        }
        //Elevator Task
        else {
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
        this.addToQueue(task);
    }

    /**Get Scheduled tasks from Scheduler for a given elevator number*/
    public synchronized ArrayList<Integer> getScheduledQueue(Integer elevatorNumber){return this.scheduledQueue.get(elevatorNumber);}

    /**Add task to queue for Scheduler and schedule it*/
    public void addToQueue(Task task){
        if(task.getIsFloorTask()){
            System.out.println("New Task added to queue" + ", Target Floor: "
                    + task.getFloorNumber() + ", Direction: "+ task.getDirection());
        }
        else{System.out.println("New Task added to queue"+ ", Target Floor: "
                + task.getFloorNumber() + ", Elevator Number: " + task.getElevatorNumber());
        }
        tasksQueue.add(task);
        scheduleTask(task);
    }

    /**Remove First task from queue of a given elevator number*/
    public void removeElevatorTask(Integer elevatorNumber){
        scheduledQueue.get(elevatorNumber).remove(0);
    }

    /** Schedule a given Task based off elevators & floors data */
    public void scheduleTask(Task task){
        //Get All Elevators position and status
        Integer bestElevatorNumber = 0;
        Integer bestTaskNumber = getTaskNumber(bestElevatorNumber);

        //Floor task
        if(task.getIsFloorTask()){
            if(elevators.length > 1) {

                for (int i = 0; i < elevators.length; i++) {
                    Integer currentTaskNumber = getTaskNumber(i);
                    if(bestTaskNumber > currentTaskNumber){
                        bestTaskNumber = currentTaskNumber;
                        bestElevatorNumber = i;
                    }
                }
            }
        }
        //Elevator Task
        else{
            bestElevatorNumber = task.getElevatorNumber();
            bestTaskNumber = getTaskNumber(bestElevatorNumber);
        }
        //Add task to scheduled Task list for best elevator number if task is Not already queue.
        if(bestTaskNumber != -1) {
            ArrayList<Integer> queue = this.scheduledQueue.get(bestElevatorNumber);

            if(bestTaskNumber < queue.size()) { queue.add(bestTaskNumber, task.getFloorNumber());}
            else {queue.add(task.getFloorNumber());}

            //Convert new to byte[] format
            byte data[] = new byte[queue.size() + 2];
            data[0] = (byte) 0; // This will tell Elevator_System that this data is a scheduled task.
            data[1] = (byte) bestElevatorNumber.intValue();
            for (int i = 0; i < queue.size(); i++) {
                data[i+2] = (byte) queue.get(i).intValue();
            }

            this.scheduledQueue.replace(bestElevatorNumber, queue);
            targetElevatorNumber = bestElevatorNumber;
            isNewTaskScheduled = true;
            this.sendData(data,20);
        }
    }

    /** This method will send scheduled task data to Elevator_System*/
    public void sendData(byte data[], int portNumber){
        System.out.println("Scheduler: sending a scheduled task data to Elevator_System.");
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

    /** Method to send elevators current positions request and update*/
    public void sendElevatorPositionsRequest(){
        byte data[] = new byte[1];
        data[0] = (byte) 1;
        this.sendData(data,20);
        // Construct a DatagramPacket for receiving packets for the data reply
        data = new byte[100];
        receivePacket = new DatagramPacket(data, data.length);

        try {
            // Wait until data reply is received via sendSocket.
            sendSocket.receive(receivePacket);
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
        int portRecievedFrom = receivePacket.getPort();

        //Correct data recieved
        if(data[0] == (byte) 4){
            for (int i = 1; i < len; i++) {
                int position = data[i];
                Integer floatPosition = position;
                elevators[i-1].setPosition(floatPosition.floatValue());
            }
        }
    }

    /** Method to get position of task if added to a given elevator number to schedule most the recent task
     * Return -1 if target floor is already in queue  */
    public int getTaskNumber(Integer elevatorNumber){
        //Target Floor number of latest task added and status of elevator.
        Integer targetFloorNumber = tasksQueue.get(tasksQueue.size() - 1).getFloorNumber();
        Float elevatorPosition = elevators[elevatorNumber].getPosition();
        ArrayList<Integer> queue = this.scheduledQueue.get(elevatorNumber);
        Boolean condition1 = false;
        Boolean condition2 = false;
        Integer bestTaskNumber = 0;

        //Iterate task number until condition1 or condition2 becomes true, Or until queue.size reaches end.
        if(queue.size() != 0) {
            for (int i = 0; !condition1 && !condition2 && (i < queue.size()); i++) {
                Integer queueFloor = queue.get(i);
                if (targetFloorNumber == queueFloor) {
                    return -1; //Return -1 if target floor already in queue
                }
                condition1 = (elevatorPosition > queueFloor) && (targetFloorNumber > queueFloor) && (elevatorPosition > targetFloorNumber);
                condition2 = (elevatorPosition < queueFloor) && (targetFloorNumber < queueFloor) && (elevatorPosition < targetFloorNumber);
                bestTaskNumber = i;
            }
        }
        return bestTaskNumber;
    }

    /** Getter method for targert Elevator Number */
    public Integer getTargetElevatorNumber(){return targetElevatorNumber;}

    /** Setter methods for a group of attributes */
    public void setElevator_system(Elevator_System elevator_system){
        this.elevator_system = elevator_system;
        elevators = elevator_system.getElevators();
        for (int i = 0; i < this.elevators.length; i++) {
            this.scheduledQueue.put(i,new ArrayList<>());
        }
    }
    public void setIsNewTaskScheduled(Boolean condition){isNewTaskScheduled = condition;}
    public void setTasksQueue(ArrayList<Task> tasks){this.tasksQueue = tasks;}
    public void setScheduledQueue(HashMap<Integer,ArrayList<Integer>> tasks){this.scheduledQueue = tasks;}

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
        Scheduler_System scheduler_SubSystem = new Scheduler_System();
        Thread schedulerSystemThread = new Thread(scheduler_SubSystem, "Scheduler Simulation");
        schedulerSystemThread.start();
    }
}
