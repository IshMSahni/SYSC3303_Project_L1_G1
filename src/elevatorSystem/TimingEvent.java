package elevatorSystem;

import java.io.IOException;
import java.net.*;

public class TimingEvent implements Runnable{
    private int elevatorNumber;
    private int targetFloor;
    private long time;
    private DatagramSocket sendReceiveSocket;
    private DatagramPacket sendPacket, receivePacket;

    public TimingEvent(int elevatorNumber, int targetFloor, long time){
        this.elevatorNumber = elevatorNumber;
        this.time = time;
        this.targetFloor = targetFloor;
    }

    /** This method will send data to the portNumber given*/
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
        try{ sendReceiveSocket = new DatagramSocket(100+elevatorNumber); }
        catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        //Wait specific time
        try{ wait(time); }
        catch (Exception e){ e.printStackTrace(); }

        //Send Elevator Position request
        byte[] data = new byte[2];
        data[0] = (byte) 4; data[1] = (byte) elevatorNumber;
        sendData(data, 20);

        data = recieveData(sendReceiveSocket);

        //If elevator did not reach floor, send Elevator Out Of Service data to Elevator_System
        if(data[0] != targetFloor){
            System.out.println("TimingEvent: Elevator "+elevatorNumber+" going Out Of Service, Timing Error.");
            data = new byte[2];
            data[0] = (byte) 5; data[1] = (byte) elevatorNumber;
            sendData(data, 20);
        }
        sendReceiveSocket.close();
    }
}
