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
    private int[] elevatorsStatus; // 0: Door closed, 1: moving up, 2: moving down, 3: Loading Passengers, Door Open, 4: Out Of Service, 5: idle
    private JFrame mainFrame;
    private JPanel upperPanel, mainPanel, lowerPanel;
    private JPanel[] elevatorPanels;
    private JLabel[] positionLabels, statusLabels;
    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket sendReceiveSocket;

    /** Constructor */
     public Elevator_System_GUI(int numElevators, int numFloors){
         this.numElevators = numElevators;
         this.numFloors = numFloors;
         this.elevatorsPositions = new int[numElevators];
         this.elevatorsStatus = new int[numElevators];

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
         this.statusLabels = new JLabel[numElevators];

         this.createElevatorCarFrame();

         this.mainFrame.add(upperPanel,BorderLayout.PAGE_START);
         this.mainFrame.add(mainPanel,BorderLayout.CENTER);
         this.mainFrame.add(lowerPanel,BorderLayout.PAGE_END);
    }

    /** Creates individual JFrames for a single elevatorCar object*/
    public void createElevatorCarFrame(){
        for (int i = 0; i < this.numElevators; i++) {
            this.elevatorPanels[i] = new JPanel(new GridLayout(3,1));
            JLabel elevatorName = new JLabel("Elevator "+i);
            this.positionLabels[i] = new JLabel("Floor "+this.elevatorsPositions[i]);
            this.statusLabels[i] = new JLabel("Idle");

            //Center Align labels
            elevatorName.setHorizontalAlignment(SwingConstants.CENTER);
            elevatorName.setVerticalAlignment(SwingConstants.CENTER);
            positionLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            positionLabels[i].setVerticalAlignment(SwingConstants.CENTER);
            statusLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            statusLabels[i].setVerticalAlignment(SwingConstants.CENTER);

            this.elevatorPanels[i].add(elevatorName);
            this.elevatorPanels[i].add(this.positionLabels[i]);
            this.elevatorPanels[i].add(this.statusLabels[i]);
            this.elevatorPanels[i].setBackground(new Color(150,150,150));
            this.mainPanel.add(this.elevatorPanels[i]);
        }

    }

    /** This method will update the elevator positions on the GUI*/
    public void updateGUI(){
        for (int i = 0; i < this.numElevators; i++) {
            this.positionLabels[i].setText("Floor "+this.elevatorsPositions[i]);
            this.statusLabels[i].setText(this.getStatusString(this.elevatorsStatus[i]));
            if(this.elevatorsStatus[i] == 4){this.elevatorPanels[i].setBackground(new Color(200,0,0));}
        }
    }

    /** This method will return the equivalent Status String to given number */
    public String getStatusString(int statusNumber){
        String returnString = "";

        if(statusNumber == 0){returnString = "Door is closed.";}
        else if(statusNumber == 1){returnString = "^^ Moving Up ^^";}
        else if(statusNumber == 2){returnString = "vv Moving Down vv";}
        else if(statusNumber == 3){returnString = "Loading Passengers...";}
        else if(statusNumber == 4){returnString = "OUT OF SERVICE";}
        else if(statusNumber == 5){returnString = "Idle";}

        return returnString;
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
                int i = 1;
                for (int j = 0; j < this.numElevators; j++) {
                    this.elevatorsPositions[j] = data[i];
                    this.elevatorsStatus[j] = data[i+1];
                    i += 2;
                }
                this.updateGUI();
            }
        }
    }
}
