import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class ActionsForWorkers extends Thread{
    private static Index ind = new Index();
    Socket connection;
    ObjectInputStream in;
    ObjectOutputStream out;
    public ActionsForWorkers(Socket connection){
        this.connection = connection;
        try {
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//    public void setIndex(int c){
//        ind.setInd(c);
//    }
//    public int getIndex(){
//        return ind.getInd();
//    }

    static synchronized double distance(List<Waypoint> w) {  // distance in a chunk of waypoints
        double lat = w.get(0).getLat();
        double lon = w.get(0).getLon();
        double dist = 0.0;
        for (int i = 1; i < w.size(); i++) {
            dist += Math.sqrt(Math.pow((w.get(i).getLat() - lat), 2) + Math.pow(w.get(i).getLon() - lon, 2));
            lat = w.get(i).getLat();
            lon = w.get(i).getLon();
        }
        return dist;
    }
    static synchronized double totalTime(List<Waypoint> w){ // get total time
        double total = 0.0;
        for(int i =1;i<w.size();i++){
            total += w.get(i).getTime()-w.get(i-1).getTime();
        }
        return total;
    }

    // distance between 2 sets of coordinates
    static synchronized double d(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow(y2 - y1, 2));
    }
    // v = Δs/Δt

    static synchronized double calcVelocity(double d, List<Waypoint> w) { // velocity in a chunk of waypoints
        double vel = (double) d / (w.get(w.size() - 1).getTime() - w.get(0).getTime());
        return vel;
    }

    static synchronized double calcElevation(List<Waypoint> w) {

        double dist = 0;
        for (int i = 1; i < w.size(); i++) {
            if (w.get(i).getEle() > w.get(i - 1).getEle()) {
                dist += d(w.get(i - 1).getLat(), w.get(i - 1).getLon(), w.get(i).getLat(), w.get(i).getLon());
            }
        }
        return dist;
    }
    static synchronized Result map(int id,List<Waypoint> list){
        double d = distance(list);
        List<Double> res = new ArrayList<>();
        res.add(d);
        res.add(calcVelocity(d,list));
        res.add(calcElevation(list));
        res.add(totalTime(list));
        Result r = new Result(list,res,id+1);
        return r;
    }
    public synchronized void bonusCheck(List<Waypoint> list,SafeSeg segment){
        List<Waypoint> seg = segment.get("gpxgenerator.com");
        boolean flag = false;
        int index =0;
        int startPos = 0;
        int endPos = 0;
        boolean flag2 = false;
        int c = 1;
        int c2 = 0;
        int pos = 0;
        for(int j =0;j<=list.size()-2;j++){
            index = 0;
            for(int i =j;i<i+seg.size();i++){
//                System.out.println(ind.getInd());
//                if(ind.getInd()!=0){
//                    System.out.println("IN");
//                    c2 = 2;
//                    ind.up();
//                    if(i==1){
//                        for(int k = 0;k<seg.size()-ind.getInd();k++){
//                            int index2 = ind.getInd();
//                            double lon = Math.abs(list.get(i).getLon()-seg.get(index2).getLon());
//                            double lat = Math.abs(list.get(i).getLat()-seg.get(index2).getLat());
//                            double elevation = Math.abs(list.get(i).getEle()-seg.get(index2).getEle());
//                            index2++;
//                            if(!(lon<0.003 && lat<0.003 && elevation<=0.09)){
//                                c=0;
//                                break;
//
//                            }
//
//                        }
//                        if(c!=0){
//                            flag2 = true;
//                            break;
//                        }
//
//                    }
//                }
                double lon = Math.abs(list.get(i).getLon()-seg.get(index).getLon());
                double lat = Math.abs(list.get(i).getLat()-seg.get(index).getLat());
                double elevation = Math.abs(list.get(i).getEle()-seg.get(index).getEle());
                if(lon<0.003 && lat<0.003 && elevation<=0.09){

                }else{
                    break;
                }

                if(index==seg.size()-1){
                    flag = true;
                    System.out.println("user did the segment");
                    break;
                }
                if(i==list.size()-1){
                    pos = i-index+1;
                    flag2 = true;
                    c2 = 1;
                    ind.up();
                    ind.setInd(index);
                    break;
                }
                index++;
            }
            if(flag){
                startPos = j;
                endPos = j+seg.size()-1;
                break;
            }
            if(flag2){
                break;
            }
        }
//        if(flag2){
//            int d = ind.getInd();
//            if(c2==2){
//                System.out.println("FROM START POS: "+1+ " FROM END POS: "+d);
//            }
//            else{
//                System.out.println("FROM START POS else end: "+pos+ " FROM END POS: "+(pos+d));
//            }
//
//        }
        if(flag){
            List<Waypoint> userSeg = new ArrayList<>();

            for(int i=startPos;i<=endPos;i++){
                userSeg.add(list.get(i));
            }
            double time = totalTime(userSeg);
            System.out.println("USER DID SEG "+userSeg.get(0).getUser());
            System.out.println("FROM START POS: "+startPos+ " FROM END POS: "+endPos + "AND AT TIME: "+time);
        }
        else{
            System.out.println("nope");
        }



    }
//    public void run(){
//        try{
//
//            List<Waypoint> l = (List<Waypoint>) in.readObject();
//            System.out.println("Worker: Waypoint list received from Master "+ l.get(0).getTime());
//
//            Result r;
//            r = map(l.get(0).getId(),l);
//            out.writeObject(r);
//            out.flush();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }finally {
//            try{
//                in.close();
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    public void run(){
        try{

            List<Waypoint> l = (List<Waypoint>) in.readObject();
            SafeSeg sf = (SafeSeg) in.readObject();
            if(l.size()!=0){
                System.out.println("Worker: Waypoint list received from Master "+ l.get(0).getTime());
                bonusCheck(l,sf);

                Result r;
                r = map(l.get(0).getId(),l);
                out.writeObject(r);
                out.flush();
            }
            else{
                out.writeObject(null);
                out.flush();
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try{
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
