package ElevatorStates;

public class CalculateTime {
    private final double elevatorAcceleration = 0.9; // 0.9 meter per second square
    private final double elevatorTopSpeed = 2.7; // 2.7 meters per second
    private final long loadTime = 10; // 10 seconds is the average loading time

    /** Method to calculate time in milliseconds to move Elevator a given amount of distance */
    public long CalculateTime(Float startLocation, Integer endLocation){
        double time;
        double netDistance = Math.abs(startLocation - endLocation);
        double inflectionDistance = (elevatorTopSpeed*elevatorTopSpeed)/elevatorAcceleration;

        //If net distance between floors allows elevator to reach top speed, then use 1st formula, else use 2nd formula.
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
}
