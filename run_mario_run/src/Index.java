public class Index {
    private  int ind = 0;
    private int counter = 0;
    public synchronized void setInd(int n){
        this.ind = n;
    }
    public synchronized void up(){
        counter++;
    }
    public synchronized int getCounter(){
        return counter;
    }

    public synchronized int getInd() {
        return ind;
    }
}
