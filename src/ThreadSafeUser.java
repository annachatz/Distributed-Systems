import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ThreadSafeUser {
    private final Map<String,UserStatistics> map = new HashMap<>();

    public synchronized UserStatistics get(String key){
        return map.get(key);
    }
    public synchronized UserStatistics put(String key, UserStatistics value){
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
        return "ThreadSafeUser{" +
                "map=" + map +
                '}';
    }
}
