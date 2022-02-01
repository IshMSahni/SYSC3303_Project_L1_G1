import java.util.ArrayList;

/**
 * Elevator control system
 */
public class Elevator_System implements Runnable{
    private ArrayList<ElevatorCar> elevators;

    @Override
    public void run() {
        //Wait until button is pressed inside elevator or elevator reaches floor
        //Notify Scheduler of button pressed in elevator
        //Wait until Schduler has scheduled queue
        //Send signal to elevator cars on which floor to go to
    }
}
