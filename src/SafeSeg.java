import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SafeSeg implements Serializable {
    private final Map<String, List<Waypoint>> map = new HashMap<>();

    public synchronized List<Waypoint> get(String key){
        return map.get(key);
    }
    public synchronized List<Waypoint> put(String key, List<Waypoint> value){
        return map.put(key,value);
    }

    public synchronized boolean contains(String key){
        return map.containsKey(key);
    }
    public int getSize(){
        return map.size();
    }
    public Set<String> getKeys(){
        return map.keySet();
    }

    @Override
    public String toString() {
        return "SafeSeg{" +
                "map=" + map +
                '}';
    }
}
