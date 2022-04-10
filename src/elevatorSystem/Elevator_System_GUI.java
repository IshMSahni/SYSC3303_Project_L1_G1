package elevatorSystem;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

/** This Class will create the GUI for the Elevator_System*/
public class Elevator_System_GUI implements Runnable{
    private int numElevators;
    private int numFloors;
    private int[] elevatorsPositions;
    private JFrame mainFrame;
    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket sendReceiveSocket;

    /** Constructor */
     public Elevator_System_GUI(int numElevators, int numFloors){
         this.numElevators = numElevators;
         this.numFloors = numFloors;
         this.elevatorsPositions = new int[numElevators];

         //Create and sendReceive socket. This socket and will receive Elevator real time position data to update GUI with.
         try {
             this.sendReceiveSocket = new DatagramSocket(200);
         } catch (SocketException se) {
             se.printStackTrace();
             System.exit(1);
         }
     }

     /** Creates the main JFrame of the GUI which will contain multiple ElevatorCarFrames*/
     public void createMainFrame(){

    }

    /** Creates individual JFrames for a single elevatorCar object*/
    public void createElevatorCarFrame(){

    }

    /** This method will update the elevator positions on the GUI*/
    public void updateGUI(){

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
            sendReceiveSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /** This method will receive data at the receive Socket given*/
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

    @Override
    public synchronized void run() {
        while(true){
            byte data[] = recieveData(sendReceiveSocket);
            if(data[0] == (byte) 0){
                for (int i = 0; i < this.numElevators; i++) {
                    this.elevatorsPositions[i] = data[i+1];
                }
                this.updateGUI();
            }
        }
    }
}
