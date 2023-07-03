import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class MasterSegment extends Thread{
    int numOfWorkers;
    String [] workerAddresses = new String[numOfWorkers];
    Socket requestSocket;
    public static SafeSeg sf = new SafeSeg();
    public MasterSegment(){
        this.sf = getseg();
    }
    public SafeSeg getsf(){
        return sf;
    }

    public MasterSegment(Socket request,int workers,String [] addresses){
        this.requestSocket = request;
        this.numOfWorkers = workers;
        this.workerAddresses = addresses;
    }
    private static SafeSeg sfseg = new SafeSeg();
    private static ThreadSafeUser mapUserFinal = new ThreadSafeUser();
    private static ThreadSafeCounter mapcount = new ThreadSafeCounter();
    private static ThreadSafeUser mapUserFinalStats = new ThreadSafeUser();
    private static ThreadSafeUser mapUser = new ThreadSafeUser();
    private static  MyThreadSafeHashMap map2 = new MyThreadSafeHashMap();
    private static volatile ConcurrentHashMap<Integer, UserStatistics> map = new ConcurrentHashMap<>();
    //ThreadSafeHashmap<Integer,UserStatistics> mine = new ThreadSafeHashmap<>();
    //HashMap<Integer,UserStatistics> has = new HashMap<>();

    int numOfUsers = 1;
    private static int counter = 0;
    static Waypoint wpt;
    List<String> User_id = new ArrayList<>();
    List<Waypoint> WaypointList = new ArrayList<Waypoint>();
    List<Waypoint> seg = new ArrayList<Waypoint>();
    private static final Object lock = new Object();
    public static List<List<Waypoint>> RandomChunks(List<Waypoint> l, int chunks){
        List<List<Waypoint>> list = new ArrayList<>();
        int length = l.size();
        int index = 0;
        for(int i =0;i<chunks;i++){
            List<Waypoint> backup = new ArrayList<>();
            for(int j =0;j<length/chunks;j++){
                backup.add(l.get(index));
                index++;
            }
            if(chunks>1 && i< (chunks-1)){
                backup.add(l.get(index));
            }
            if(i == (chunks-1) && length % chunks != 0){
                while(index<length){
                    backup.add(l.get(index));
                    index++;
                }
                //backup.add(l.get(length-1));
            }
            list.add(backup);
        }

        return list;
    }
    public SafeSeg getseg(){
        return sfseg;
    }
    public void run(){


        //Socket [] requestSocket = new Socket[1];
        Socket [] workerSockets = new Socket[numOfWorkers];
        ObjectOutputStream [] outs = new ObjectOutputStream[numOfWorkers];
        ObjectInputStream in = null;
        ObjectInputStream [] ins = new ObjectInputStream[numOfWorkers];

        try{

//            for(int i=0;i<numOfUsers;i++){
//                requestSocket[0] = new Socket("localhost",4000);
//            }
            for(int i = 0; i< numOfWorkers; i++){
                workerSockets[i] = new Socket(workerAddresses[i],3025+i);
            }

            System.out.println("connected to UserServer");
            for(int i = 0; i< numOfWorkers; i++){
                outs[i] = new ObjectOutputStream(workerSockets[i].getOutputStream());
            }
            in = new ObjectInputStream(requestSocket.getInputStream());
            for(int i = 0; i< numOfWorkers; i++){
                ins[i] = new ObjectInputStream(workerSockets[i].getInputStream());
            }

            File gpxFile = new File("/Users/annachatz/Downloads/server_gpxs/route"+(counter+1)+".gpx");
            FileOutputStream fileOutputStream = new FileOutputStream(gpxFile);

            byte[] buffer = new byte[1024];
            int bytesRead = 0;

            while ((bytesRead = in.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            fileOutputStream.close();


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(gpxFile);
            Element root = doc.getDocumentElement();
            String creator = root.getAttribute("creator");
            System.out.println(creator);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("wpt");
            ArrayList<Double> latitudes = new ArrayList<>();
            ArrayList<Double> longitudes = new ArrayList<>();
            ArrayList<Double> elevations = new ArrayList<>();
            ArrayList<Long> times = new ArrayList<>();
            synchronized (lock){
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        Double latitude = Double.valueOf(eElement.getAttribute("lat"));
                        Double longitude = Double.valueOf(eElement.getAttribute("lon"));
                        Double elevation = Double.valueOf(eElement.getElementsByTagName("ele").item(0).getTextContent());
                        String time = eElement.getElementsByTagName("time").item(0).getTextContent();

                        latitudes.add(latitude);
                        longitudes.add(longitude);
                        elevations.add(elevation);

                        String str = time;
                        String l = str.replaceAll("T","-");// eng T
                        l = l.replaceAll("Z","");//eng Z
                        l = l.replaceAll("Ζ","");//greek Z
                        l = l.replaceAll("Τ",""); // greek T
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
                        Date date = df.parse(l);
                        long t =  date.getTime();
                        times.add(t);
                        //wpt = increaseCounter(latitude,longitude,elevation,t,creator);
                        wpt = new Waypoint(latitude,longitude,elevation,t,creator,counter);
                        if(!wpt.getUser().equals("gpxgenerator.com")){
                            UserStatistics ob = new UserStatistics();
                            if(!map2.contains(counter+1)){
                                map2.put(counter+1,ob);
                            }
                            UserCountsStats ob3 = new UserCountsStats();
                            UserStatistics ob2 = new UserStatistics();
                            if(!mapUser.contains(creator)){
                                mapUser.put(creator,ob2);
                                mapcount.put(creator,ob3);
                                mapUserFinal.put(creator,ob2);
                            }
                        }
                        //mapUser.put(creator,ob2);
                        //counter++;
                        if(wpt.getUser().equals("gpxgenerator.com")){
                            seg.add(wpt);
                        }
                        else{
                            if(!User_id.contains(wpt.getUser())){
                                User_id.add(wpt.getUser());
                            }
                            WaypointList.add(wpt);
                        }


                    }
                }
                if(!wpt.getUser().equals("gpxgenerator.com")){
                    counter++;
                }



            }
            int rand = (int) (Math.random()* (WaypointList.size()-1+1)+1);
            //System.out.println(WaypointList.size());
            List<List<Waypoint>> list = null;
            if(WaypointList.size()!=0){
                list = RandomChunks(WaypointList,numOfWorkers);
                System.out.println("First"+WaypointList);
                System.out.println("SEG"+seg);
                System.out.println("Firs"+list+list.size());
                System.out.println(WaypointList.size()+"SIZE");
            }
            if(WaypointList.size() !=0){
                for(int i=0;i<numOfWorkers;i++){
                    outs[i].writeObject(list.get(i));
                    outs[i].flush();
                    outs[i].writeObject(sfseg);
                    outs[i].flush();
                }
                for(int i=0;i<numOfWorkers;i++){

                    System.out.println("Master: Object sent to client");
                    Result result;
                    synchronized (lock){
                        result = (Result) ins[i].readObject();
                        map2.put(result.getId(),map2.get(result.getId()).reduce(result.getDoubles()));
                        String user = result.getWaypoints().get(0).getUser();
                        if(i==numOfWorkers-1){
                            mapcount.put(user,mapcount.get(user).reduce(1));
                        }
                        //mapcount.put(user,mapcount.get(user).reduce(1));
                        mapUser.put(user, mapUser.get(user).reduce(result.getDoubles()));
                        mapUserFinal.put(user, mapUserFinal.get(user).reduce(result.getDoubles()));
                        double sumd = 0;
                        double sumv = 0;
                        double sumele = 0;
                        long sumtime = 0;

                        for (String e : mapUser.getKeys()) {
                            double d = mapUser.get(e).getDistance();
                            double v = mapUser.get(e).getVelocity();
                            double ele = mapUser.get(e).getElevation();
                            long time = mapUser.get(e).getTime();
                            sumd+=d;
                            sumele +=ele;
                            sumtime+=time;
                            sumv+=v;
                            //UserStatistics s2 = new UserStatistics(d/(mapcount.get(e).count/numOfWorkers),v/(mapcount.get(e).count/numOfWorkers),ele/(mapcount.get(e).count/numOfWorkers),time/(mapcount.get(e).count/numOfWorkers));
                            //mapUserFinal.put(e,s2);

                        }

                        int s = mapUser.getSize();
                        UserStatistics so = new UserStatistics(sumd/s,sumv/s,sumele/s,sumtime/s);
                        mapUserFinalStats.put("0",so);

                    }

                    System.out.println("Master Server: "+result.toString2());
                }
            }
            else{
                sfseg.put(seg.get(0).getUser(),seg);
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try{

//                for(int i=0;i<numOfUsers;i++){
//                    in[i].close();
//                }
                in.close();
                for(int i = 0; i< numOfWorkers; i++){
                    ins[i].close();
                }
                for(int i = 0; i< numOfWorkers; i++){
                    outs[i].close();
                }
//                for(int i=0;i<numOfUsers;i++){
//                    requestSocket[i].close();
//                }
                for(int i = 0; i< numOfWorkers; i++){
                    workerSockets[i].close();
                }
                System.out.println("MAPPPPPUSEEEERBEFOREEEE"+mapUser.toString());
                for(int i=0;i<mapcount.getKeys().size();i++){
                    String key = (String) mapcount.getKeys().toArray()[i];
                    UserStatistics ob = new UserStatistics(mapUser.get(key).getDistance()/mapcount.get(key).getCount(),mapUser.get(key).getVelocity()/mapcount.get(key).getCount(),mapUser.get(key).getElevation()/mapcount.get(key).getCount(),mapUser.get(key).getTime()/mapcount.get(key).getCount());
                    mapUserFinal.put(key,ob);
                }
                System.out.println("User_id"+User_id);
                System.out.println("MAPPPPP2222"+map2.toString());
                System.out.println("MAPUSER"+mapUser.toString());
                System.out.println("MAPFINAL"+mapUserFinal.toString());
                System.out.println("COUNTER"+mapcount.toString());
                //System.out.println("COUNTERUSERFINALSTATS"+mapcount.toString());
                System.out.println("FINALSTATS"+mapUserFinalStats.toString());
                System.out.println("SEGMENTS"+sfseg.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args){
        int workers = 1;
        //String [] addresses = new String[workers];
        //int workers = Integer.parseInt(args[0]);
        String [] addresses = new String[workers];

//        for(int i =0;i<workers;i++){
//            addresses[i] = args[1+i];
//        }
        for(int i =0;i<workers;i++){
            addresses[i] = "localhost";
        }
        ServerSocket serverSocket;
        Socket socket = null;
        try {
            serverSocket= new ServerSocket(6060);
            while(true){
                socket = serverSocket.accept();
                Thread t = new MasterSegment(socket,workers,addresses);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try{
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
