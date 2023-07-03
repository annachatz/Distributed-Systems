import java.io.Serializable;

public class Waypoint implements Serializable{
    private double lat;
    private double lon;
    private double ele;
    private long time;
    private int id;
    private String user;
    private double dist;
    private double velocity;

    public Waypoint(double lat,double lon,double ele,long time,String user,int i) {
        this.lat = lat;
        this.lon = lon;
        this.ele = ele;
        this.time = time;
        this.user = user;
        this.id = i;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getEle() {
        return ele;
    }

    public void setEle(double ele) {
        this.ele = ele;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public String toString() {
        return "Waypoint{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", ele=" + ele +
                ", time=" + time +
                ", id=" + id +
                ", user='" + user + '\'' +
                '}';
    }
}
