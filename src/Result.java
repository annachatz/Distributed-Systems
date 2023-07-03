import java.io.Serializable;
import java.util.List;
public class Result implements Serializable{
    private List<Waypoint> waypoints;
    private List<Double> doubles;
    private int id;
    Result(List<Waypoint> w,List<Double> d,int i){
        this.waypoints = w;
        this.doubles = d;
        this.id = i;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public List<Double> getDoubles() {
        return doubles;
    }
    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return "Result{" +
                "waypoints=" + waypoints +
                ", doubles=" + doubles +
                ", id=" + id +
                '}';
    }
    public  String toString2(){
        return "Result{" +
                "doubles=" + doubles +
                ", id=" + id +
                '}';
    }
}
