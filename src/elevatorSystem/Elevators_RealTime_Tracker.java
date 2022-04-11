package elevatorSystem;

import java.io.IOException;
import java.net.*;

/** This class will mainly be used to send real time elevators positions to GUI*/
public class Elevators_RealTime_Tracker implements Runnable {

    private ElevatorCar[] elevators;
    private DatagramPacket sendPacket, receivePacket; // UDP packets and sockets for send and recieving
    private DatagramSocket sendSocket;

    public Elevators_RealTime_Tracker(ElevatorCar[] elevators){
        this.elevators = elevators;
        //Create and send Socket. This socket and will send Elevator real time position data to update GUI with.
        try {
            this.sendSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /** This method will send data to given portNumber*/
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

    public int elevatorStateNumber(ElevatorState elevatorState, int elevatorNum){
        int number = 0;

        if(elevatorState.equals(elevators[elevatorNum].getMovingUp())){ number = 1; }
        else if(elevatorState.equals(elevators[elevatorNum].getMovingDown())){ number = 2; }
        else if(elevatorState.equals(elevators[elevatorNum].getLoading()) || elevatorState.equals(elevators[elevatorNum].getArrived()) || elevatorState.equals(elevators[elevatorNum].getDoorOpen())){
            number = 3;
        }
        else if(elevatorState.equals(elevators[elevatorNum].getOutOfService())){ number = 4; }

        return number;
    }

    @Override
    public synchronized void run() {
        long updateCycleTime = 1000; // 1 second
        int numElevators = elevators.length;

        while(true){
            try{
                //Wait cycle time, Then send Elevators positions data to GUI
                wait(updateCycleTime);

                byte[] data = new byte[(3*numElevators)+1];
                data[0] = (byte) 0;
                int i = 1;
                for (int j = 0; j < numElevators;j++) {
                    data[i] = (byte) Math.round(elevators[j].getPosition());

                    if(elevators[j].getElevatorState().equals(elevators[j].getOutOfService())){ data[i+1] = (byte) 4; }
                    else if(elevators[j].getTasks().size() == 0){ data[i+1] = (byte) 5; }
                    else {data[i+1] = (byte) Math.round(this.elevatorStateNumber(elevators[j].getElevatorState(),j));}

                    data[i+2] = (byte) elevators[j].getNumPassengerCounter();
                    i += 3;
                }
                this.sendData(data,200);
            }
            catch(Exception e){e.printStackTrace();}
        }
    }
}
