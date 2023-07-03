import java.util.HashMap;
import java.util.Map;
public class MyThreadSafeHashMap {
    private final Map<Integer,UserStatistics> map = new HashMap<>();

    public synchronized UserStatistics get(Integer key){
        return map.get(key);
    }
    public synchronized boolean contains(Integer key){
        return map.containsKey(key);
    }
    public synchronized UserStatistics put(Integer key, UserStatistics value){
        return map.put(key,value);
    }

    @Override
    public synchronized String toString() {
        return "MyThreadSafeHashMap{" +
                "map=" + map +
                '}';
    }
}
