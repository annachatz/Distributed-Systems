import java.util.List;
public class UserStatistics {
    private int id;
    private double totalDistance;
    private double totalVelocity;
    private double totalElevation;
    private long totalTime;

    public UserStatistics() {
        this.totalDistance = 0.0;
        this.totalElevation = 0.0;
        this.totalVelocity = 0.0;
        this.totalTime = (long) 0.0;
    }
    public UserStatistics(double Distance, double Velocity, double Elevation, long Time){
        this.totalDistance = Distance;
        this.totalElevation = Elevation;
        this.totalVelocity = Velocity;
        this.totalTime = Time;
    }
    public synchronized double getDistance(){
        return totalDistance;

    }
    public synchronized double getVelocity(){
        return totalVelocity;

    }
    public synchronized double getElevation(){
        return totalElevation;

    }
    public synchronized long getTime(){
        return totalTime;

    }

    public synchronized UserStatistics reduce(List<Double> list) {
        totalDistance += list.get(0);
        totalVelocity += list.get(1);
        totalElevation += list.get(2);
        totalTime += list.get(3);
        return new UserStatistics(totalDistance,totalVelocity,totalElevation,totalTime);
    }

    @Override
    public String toString() {
        return "UserStatistics{" +
                "id=" + id +
                ", totalDistance=" + totalDistance +
                ", totalVelocity=" + totalVelocity +
                ", totalElevation=" + totalElevation +
                ", totalTime=" + totalTime +
                '}';
    }
}
