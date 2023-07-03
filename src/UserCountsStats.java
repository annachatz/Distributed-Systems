import java.util.List;
public class UserCountsStats {
    int count = 0;

    public UserCountsStats() {
        this.count = 0;
    }
    public int getCount(){
        return count;
    }
    public UserCountsStats(int i) {
        this.count = i;
    }
    public synchronized void  increase(){
        this.count++;
    }
    public UserCountsStats reduce(int i){
        this.count += i;
        return new UserCountsStats(count);
    }


    @Override
    public String toString() {
        return "UserCountsStats{" +
                "count=" + count +
                '}';
    }
}

