package elevatorSystem;
import ElevatorStates.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * ElevatorCar class. Simulates an elevator object.
 * elevatorNumber for number for this Elevator
 * ArrayList of buttons and lights to simulates elevator buttons inside
 * Boolean attributes for doorsOpen (true if open), and motors (true if moving)
 * String status for status of elevator, Possible statuses:"Stopped","Moving Up","Moving Down"
 */
public class ElevatorCar implements Runnable{

    private int elevatorNumber;
    private float position;
    // lights buttons doors and motors
    private ArrayList<Boolean> lights = new ArrayList<Boolean>();
    private ArrayList<Boolean> buttons = new ArrayList<Boolean>();
    private boolean doorsOpen = false; //to open the doors
    private boolean motors =  false; //for motion
    private ArrayList<Integer> tasks;
    private String status;
    private ElevatorState doorOpen, doorClosed, arrived, loading, movingUp, movingDown, outOfService;
    private ElevatorState elevatorState;
    private int numPassengerCounter;
    private DatagramPacket sendPacket;
    private DatagramSocket sendSocket;
    private final double elevatorAcceleration = 0.9; // 0.9 meter per second square
    private final double elevatorTopSpeed = 2.7; // 2.7 meters per second
    private final long loadTime = 10; // 10 seconds is the average loading time
    private long time;

    /** Constructor for Elevator Car */
    public ElevatorCar(int elevatorNumber, int totalFloorNumber){
        this.elevatorNumber = elevatorNumber; //
        this.position = 0; //
        this.numPassengerCounter = 0;
        this.status = "Stopped"; //
        this.lights = new ArrayList<>();
        this.buttons = new ArrayList<>();
        this.doorsOpen = false;
        this.motors = false;
        this.tasks = new ArrayList<>();
        for (int i = 0; i < totalFloorNumber; i++) {
            this.lights.add(false);
            this.buttons.add(false);
        }

        //Initialize elevator states
        this.doorOpen = new DoorOpen(this);
        this.doorClosed = new DoorClosed(this);
        this.arrived = new Arrived(this);
        this.loading = new Loading(this);
        this.movingUp = new MovingUp(this);
        this.movingDown = new MovingDown(this);
        this.outOfService = new OutOfService(this);
        this.elevatorState = doorClosed;

        try {
            this.sendSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /** Getter and setters for position and status */
    public float getPosition() {return position; }
    public void setPosition(float position) {this.position = position; }

    public String getStatus() {return status; }
    public void setStatus(String status) {this.status = status; }

    public int getElevatorNumber() {return elevatorNumber;}

    /** Passenger Counter methods*/
    public int getNumPassengerCounter(){return this.numPassengerCounter;}
    public void setNumPassengerCounter(int newCount){this.numPassengerCounter = newCount;}
    public void incrementPassengerCount(){this.numPassengerCounter = this.numPassengerCounter + 1;}
    public void decrementPassengerCount(){this.numPassengerCounter = this.numPassengerCounter - 1;}

    /** Method for when a button is pressed inside Elevator */
    public void buttonPressed(int buttonLocation){
        byte data[] = new byte[3];
        data[0] = (byte) 0; data[1] = (byte) elevatorNumber; data[2] = (byte) buttonLocation;
        //create the datagram packet for the message with Port 10 (Scheduler)
        try {
            this.sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 10);
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
        this.setLights(buttonLocation,true);
        this.setButton(buttonLocation, true);
    }

    public void testbuttonPressed(int buttonLocation){
        this.setLights(buttonLocation,true);
        this.setButton(buttonLocation, true);
    }


    /**Getter and Setter method for Lights */
    public void setLights(int location, boolean value) {lights.set(location, value);}
    public boolean getLights(int location) {return lights.get(location);}

    /**Getter and Setter method for buttons */
    public void setButton(int location, boolean value) {buttons.set(location, value);}
    public boolean getButton(int location) {return buttons.get(location);}

    /**Getter and Setter method for doorsOpens */
    public boolean getDoors() {return this.doorsOpen;}
    public void setDoors(boolean value) {this.doorsOpen = value;}

    /**Getter and Setter method for Motors */
    public boolean getMotors() {return this.motors;}
    public void setMotors(boolean value) {this.motors = value;}

    /**Getter and Setter method for Tasks */
    public ArrayList<Integer> getTasks() {return tasks;}
    public void setTasks(ArrayList<Integer> tasks) {this.tasks = tasks;}

    /** Method to move elevator */
    public synchronized void movingElevator(){
        //Calculate Time to move elevator
        Float startLocation = this.position;
        Integer endLocation = this.tasks.get(0);
        long time = calculateTime(startLocation,endLocation);

        //Elevator state methods
        this.moveElevator(time);
        this.elevatorArrived();
        this.openDoor();
    }

    /** Method to calculate time in milliseconds to move Elevator a given amount of distance */
    public long calculateTime(Float startLocation, Integer endLocation){
        double time;
        double netDistance = Math.abs(startLocation - endLocation) * 3.0;
        double inflectionDistance = (elevatorTopSpeed*elevatorTopSpeed)/elevatorAcceleration;

        //If net distance between floors allows elevator  to reach top speed, then use 1st formula, else use 2nd formula.
        if(netDistance > inflectionDistance){
            time = ((netDistance - inflectionDistance) / elevatorTopSpeed) + (Math.sqrt(inflectionDistance) * 2);
        }
        else{
            time = Math.sqrt(netDistance) * 2;
        }
        //Return time converted to long type after rounding.
        //Multiply 1000 because to convert time from seconds to milliseconds
        return (Math.round(time) * 1000);
    }

    /** Elevator State methods*/
    public synchronized void moveElevator(long time){
        this.time = time;
        this.elevatorState.moveElevator(time);
    }
    public void openDoor(){this.elevatorState.openDoor();}
    public void closeDoor(){this.elevatorState.closeDoor();}
    public synchronized void loadElevator(long time){this.elevatorState.loadElevator(time);}

    public void elevatorArrived(){
        this.elevatorState.elevatorArrived();
        byte data[] = new byte[3];
        int currentLocation = Math.round(this.position);
        data[0] = (byte) 7; data[1] = (byte) elevatorNumber; data[2] = (byte) currentLocation ;
        //create the datagram packet for the message with Port 10 (Scheduler)
        try {
            this.sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 10);
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

    public ElevatorState getDoorOpen(){return this.doorOpen;}
    public ElevatorState getDoorClosed(){return this.doorClosed;}
    public ElevatorState getMovingUp(){return this.movingUp;}
    public ElevatorState getMovingDown(){return this.movingDown;}
    public ElevatorState getLoading(){return this.loading;}
    public ElevatorState getArrived(){return this.arrived;}
    public ElevatorState getElevatorState(){return this.elevatorState;}
    public ElevatorState getOutOfService(){return this.outOfService;}
    public void setElevatorState(ElevatorState elevatorState){this.elevatorState = elevatorState;}

    @Override
    public void run() {
        while(this.elevatorState != this.getOutOfService()) {
            while (this.tasks.size() != 0) {
                this.movingElevator();
                this.loadElevator(loadTime * 1000);
                this.closeDoor();
            }
        }
    }
}