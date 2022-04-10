package elevatorSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.*;

/** This Class will create the GUI for the Elevator_System*/
public class Elevator_System_GUI implements Runnable{
    private int numElevators;
    private int numFloors;
    private int[] elevatorsPositions;
    private JFrame mainFrame;
    private JPanel upperPanel, mainPanel, lowerPanel;
    private JPanel[] elevatorPanels;
    private JLabel[] positionLabels;
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

         this.createMainFrame();
     }

     /** Creates the main JFrame of the GUI which will contain multiple ElevatorCarFrames*/
     public void createMainFrame(){
         this.mainFrame = new JFrame("Group 1 Elevator System");
         Dimension size = Toolkit. getDefaultToolkit(). getScreenSize();
         this.mainFrame.setPreferredSize(size);

         this.mainFrame.setResizable(true);

         this.upperPanel = new JPanel(new BorderLayout());
         this.lowerPanel = new JPanel(new BorderLayout());
         this.mainPanel = new JPanel(new GridLayout(1,numElevators));
         Dimension mainPanelSize = new Dimension(size.width, size.height);
         this.mainPanel.setPreferredSize(mainPanelSize);


         this.elevatorPanels = new JPanel[numElevators];
         this.positionLabels = new JLabel[numElevators];

         this.createElevatorCarFrame();

         this.mainFrame.add(upperPanel,BorderLayout.PAGE_START);
         this.mainFrame.add(mainPanel,BorderLayout.CENTER);
         this.mainFrame.add(lowerPanel,BorderLayout.PAGE_END);
    }

    /** Creates individual JFrames for a single elevatorCar object*/
    public void createElevatorCarFrame(){
        for (int i = 0; i < this.numElevators; i++) {
            this.elevatorPanels[i] = new JPanel(new BorderLayout());
            JLabel elevatorName = new JLabel("Elevator "+i);
            this.positionLabels[i] = new JLabel("Floor "+this.elevatorsPositions[i]);

            this.elevatorPanels[i].add(elevatorName,BorderLayout.PAGE_START);
            this.elevatorPanels[i].add(this.positionLabels[i],BorderLayout.CENTER);
            this.mainPanel.add(this.elevatorPanels[i]);
        }

    }

    /** This method will update the elevator positions on the GUI*/
    public void updateGUI(){
        for (int i = 0; i < this.numElevators; i++) {
            this.positionLabels[i].setText("Floor "+this.elevatorsPositions[i]);
        }
    }

    /**
     * Closes the main frame and prompts the user with a confirmation message beforehand.
     */
    public void closeFrame() {

        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if (JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to close the GUI?")
                        == JOptionPane.OK_OPTION) {
                    mainFrame.setVisible(false);
                    mainFrame.dispose();
                    System.exit(0);
                }
            }
        });
    }

    /**
     * displays the GUI of the game.
     */
    public void displayGUI(){
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setSize(size);
        mainFrame.setVisible(true);
        closeFrame();
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
        this.displayGUI();
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
