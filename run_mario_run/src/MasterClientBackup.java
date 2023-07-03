////import java.io.IOException;
////import java.io.ObjectInputStream;
////import java.io.ObjectOutputStream;
////import java.net.Socket;
////import java.net.UnknownHostException;
////import org.w3c.dom.Document;
////import org.w3c.dom.Element;
////import org.w3c.dom.Node;
////import org.w3c.dom.NodeList;
////import java.io.File;
////import javax.xml.parsers.*;
////import org.xml.sax.SAXException;
////
////import javax.xml.parsers.DocumentBuilder;
////import javax.xml.parsers.DocumentBuilderFactory;
////import java.io.*;
////import java.net.*;
////import java.text.DateFormat;
////import java.text.ParseException;
////import java.text.SimpleDateFormat;
////import java.util.ArrayList;
////import java.util.Date;
////import java.util.List;
////
////public class MasterClient extends Thread{
////    final int num_of_workers = 2;
////    String address = "localhost";
////    private static int counter = 0;
////    static Waypoint wpt;
////    List<Waypoint> WaypointList = new ArrayList<Waypoint>();
////    private static final Object lock = new Object();
////    public static List<List<Waypoint>> RandomChunks(List<Waypoint> l, int chunks){
////        List<List<Waypoint>> list = new ArrayList<>();
////        int length = l.size();
////        int index = 0;
////        for(int i =0;i<length/chunks;i++){
////            List<Waypoint> backup = new ArrayList<>();
////            for(int j =0;j<chunks;j++){
////                backup.add(l.get(index));
////                index++;
////            }
////            if(i == (length/chunks-1) && length % chunks != 0){
////                backup.add(l.get(length-1));
////            }
////            list.add(backup);
////        }
////
////        return list;
////    }
////    public void run(){
////        Socket requestSocket = null;
////        Socket [] worker_sockets = null;
////        ObjectOutputStream [] outs = null;
////        ObjectInputStream in = null;
////        ObjectInputStream [] ins = null;
////
////        try{
////                requestSocket = new Socket("localhost",4000);
////                for(int i=0;i<num_of_workers;i++){
////                    worker_sockets[i] = new Socket("localhost",2005+i);
////                }
////
////                System.out.println("connected to UserServer");
////                for(int i = 0;i<num_of_workers;i++){
////                    outs[i] = new ObjectOutputStream(worker_sockets[i].getOutputStream());
////                }
////                in = new ObjectInputStream(requestSocket.getInputStream());
////                for(int i = 0;i<num_of_workers;i++){
////                    ins[i] = new ObjectInputStream(worker_sockets[i].getInputStream());
////                }
////                String file = (String) in.readObject();
////                //System.out.println("Received GPX file from: "+ connection.getInetAddress().getHostAddress());
////
////                File gpxFile = new File(file);
////
////                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
////                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
////                Document doc = dBuilder.parse(gpxFile);
////                Element root = doc.getDocumentElement();
////                String creator = root.getAttribute("creator");
////                System.out.println(creator);
////                doc.getDocumentElement().normalize();
////
////                NodeList nList = doc.getElementsByTagName("wpt");
////                ArrayList<Double> latitudes = new ArrayList<>();
////                ArrayList<Double> longitudes = new ArrayList<>();
////                ArrayList<Double> elevations = new ArrayList<>();
////                ArrayList<Long> times = new ArrayList<>();
////                synchronized (lock){
////                    for (int temp = 0; temp < nList.getLength(); temp++) {
////                        Node nNode = nList.item(temp);
////
////                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
////                            Element eElement = (Element) nNode;
////                            Double latitude = Double.valueOf(eElement.getAttribute("lat"));
////                            Double longitude = Double.valueOf(eElement.getAttribute("lon"));
////                            Double elevation = Double.valueOf(eElement.getElementsByTagName("ele").item(0).getTextContent());
////                            String time = eElement.getElementsByTagName("time").item(0).getTextContent();
////
////                            latitudes.add(latitude);
////                            longitudes.add(longitude);
////                            elevations.add(elevation);
////
////                            String str = time;
////                            String l = str.replaceAll("T","-");// eng T
////                            l = l.replaceAll("Z","");//eng Z
////                            l = l.replaceAll("Ζ","");//greek Z
////                            l = l.replaceAll("Τ",""); // greek T
////                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
////                            Date date = df.parse(l);
////                            long t =  date.getTime();
////                            times.add(t);
////                            //wpt = increaseCounter(latitude,longitude,elevation,t,creator);
////                            wpt = new Waypoint(latitude,longitude,elevation,t,creator,counter);
////                            //counter++;
////                            WaypointList.add(wpt);
////
////                        }}counter++;
////
////
////                }
////                int rand = (int) (Math.random()* (WaypointList.size()-1+1)+1);
////                //System.out.println(WaypointList.size());
////                List<List<Waypoint>> list = RandomChunks(WaypointList,4);
////                System.out.println("Received list"+list);
////                for(int i=0;i<num_of_workers;i++){
////                    outs[i].writeObject(list.get(i));
////                    outs[i].flush();
////                }
////
////                for(int i =0;i<num_of_workers;i++){
////                    System.out.println("Master: Object sent to client");
////                    Result result = (Result) ins[i].readObject();
////                    System.out.println("Master Server: "+result.toString());
////                }
////
////
////            //}
////        } catch (UnknownHostException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        } catch (ParseException e) {
////            e.printStackTrace();
////        } catch (SAXException e) {
////            e.printStackTrace();
////        } catch (ParserConfigurationException e) {
////            e.printStackTrace();
////        } catch (ClassNotFoundException e) {
////            e.printStackTrace();
////        }finally {
////            try{
////                in.close();
////                for(int i = 0;i<num_of_workers;i++){
////                    ins[i].close();
////                }
////                for(int i = 0;i<num_of_workers;i++){
////                    outs[i].close();
////                }
////                requestSocket.close();
////
////                for(int i =0;i<num_of_workers;i++){
////                    worker_sockets[i].close();
////                }
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
////    }
////    public static void main(String[] args){
////        for(int i=0;i<2;i++){
////        Thread t = new Thread(new MasterClient()::run);
////        t.start();}
////    }
////}
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import java.io.File;
//import javax.xml.parsers.*;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.io.*;
//import java.net.*;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class MasterClientBackup extends Thread{
//    int numofworkers = 2;
//    int numOfUsers = 1;
//    private static int counter = 0;
//    static Waypoint wpt;
//    List<Waypoint> WaypointList = new ArrayList<Waypoint>();
//    private static final Object lock = new Object();
//    public static List<List<Waypoint>> RandomChunks(List<Waypoint> l, int chunks){
//        List<List<Waypoint>> list = new ArrayList<>();
//        int length = l.size();
//        int index = 0;
//        for(int i =0;i<length/chunks;i++){
//            List<Waypoint> backup = new ArrayList<>();
//            for(int j =0;j<chunks;j++){
//                backup.add(l.get(index));
//                index++;
//            }
//            if(i == (length/chunks-1) && length % chunks != 0){
//                backup.add(l.get(length-1));
//            }
//            list.add(backup);
//        }
//
//        return list;
//    }
//    public void run(){
////        Socket workerSocket3 = null;
////        Socket workerSocket2 = null;
//        Socket [] requestSocket = new Socket[1];
//        Socket [] workerSockets = new Socket[numofworkers];
////        Socket workerSocket = null;
//        ObjectOutputStream [] outs = new ObjectOutputStream[numofworkers];
////        ObjectOutputStream out = null;
////        ObjectOutputStream out2 = null;
////        ObjectOutputStream out3 = null;
////        ObjectOutputStream out4 = null;
//        ObjectInputStream [] in = new ObjectInputStream[1];
//        ObjectInputStream [] ins = new ObjectInputStream[numofworkers];
////        ObjectInputStream in2 = null;
////        ObjectInputStream in3 = null;
////        ObjectInputStream in4 = null;
//        try{
//            //while(true){
//            for(int i=0;i<numOfUsers;i++){
//                requestSocket[0] = new Socket("localhost",4000);
//            }
//            for(int i = 0;i<numofworkers;i++){
//                workerSockets[i] = new Socket("localhost",2005+i);
//            }
//            //requestSocket = new Socket("localhost",4000);
////            workerSocket= new Socket("localhost",2005);
////            workerSocket2= new Socket("localhost",2005);
////            workerSocket3 = new Socket("localhost",2006);
//
//            System.out.println("connected to UserServer");
//            for(int i=0;i<numofworkers;i++){
//                outs[i] = new ObjectOutputStream(workerSockets[i].getOutputStream());
//            }
////            out2 = new ObjectOutputStream(workerSocket.getOutputStream());
////            out3 = new ObjectOutputStream(workerSocket2.getOutputStream());
////            out4 = new ObjectOutputStream(workerSocket3.getOutputStream());
//            for(int i=0;i<numOfUsers;i++){
//                in[i] = new ObjectInputStream(requestSocket[i].getInputStream());
//            }
//            for(int i=0;i<numofworkers;i++){
//                ins[i] = new ObjectInputStream(workerSockets[i].getInputStream());
//            }
//
//
////            in2 = new ObjectInputStream(workerSocket.getInputStream());
////            in3 = new ObjectInputStream(workerSocket2.getInputStream());
////            in4 = new ObjectInputStream(workerSocket3.getInputStream());
//            String file = (String) in[0].readObject();
//            //System.out.println("Received GPX file from: "+ connection.getInetAddress().getHostAddress());
//
//            File gpxFile = new File(file);
//
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(gpxFile);
//            Element root = doc.getDocumentElement();
//            String creator = root.getAttribute("creator");
//            System.out.println(creator);
//            doc.getDocumentElement().normalize();
//
//            NodeList nList = doc.getElementsByTagName("wpt");
//            ArrayList<Double> latitudes = new ArrayList<>();
//            ArrayList<Double> longitudes = new ArrayList<>();
//            ArrayList<Double> elevations = new ArrayList<>();
//            ArrayList<Long> times = new ArrayList<>();
//            synchronized (lock){
//                for (int temp = 0; temp < nList.getLength(); temp++) {
//                    Node nNode = nList.item(temp);
//
//                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//                        Element eElement = (Element) nNode;
//                        Double latitude = Double.valueOf(eElement.getAttribute("lat"));
//                        Double longitude = Double.valueOf(eElement.getAttribute("lon"));
//                        Double elevation = Double.valueOf(eElement.getElementsByTagName("ele").item(0).getTextContent());
//                        String time = eElement.getElementsByTagName("time").item(0).getTextContent();
//
//                        latitudes.add(latitude);
//                        longitudes.add(longitude);
//                        elevations.add(elevation);
//
//                        String str = time;
//                        String l = str.replaceAll("T","-");// eng T
//                        l = l.replaceAll("Z","");//eng Z
//                        l = l.replaceAll("Ζ","");//greek Z
//                        l = l.replaceAll("Τ",""); // greek T
//                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
//                        Date date = df.parse(l);
//                        long t =  date.getTime();
//                        times.add(t);
//                        //wpt = increaseCounter(latitude,longitude,elevation,t,creator);
//                        wpt = new Waypoint(latitude,longitude,elevation,t,creator,counter);
//                        //counter++;
//                        WaypointList.add(wpt);
//
//                    }}counter++;
//
//
//            }
//            int rand = (int) (Math.random()* (WaypointList.size()-1+1)+1);
//            //System.out.println(WaypointList.size());
//            List<List<Waypoint>> list = RandomChunks(WaypointList,4);
//            System.out.println("Received list"+list);
//            outs[0].writeObject(list.get(0));
//            outs[0].flush();
//            outs[1].writeObject(list.get(1));
//            outs[1].flush();
////            out2.writeObject(list.get(0));
////            out2.flush();
////            out3.writeObject(list.get(1));
////            out3.flush();
////            out4.writeObject(list.get(2));
////            out4.flush();
//            System.out.println("Master: Object sent to client");
//            Result result = (Result) ins[0].readObject();
//            System.out.println("Master Server: "+result.toString());
//            System.out.println("Master: Object sent to client");
//            Result result2 = (Result) ins[1].readObject();
//            System.out.println("Master Server: "+result2.toString());
//
////            System.out.println("Master: Object sent to client");
////            Result result = (Result) in2.readObject();
////            System.out.println("Master Server: "+result.toString());
////            System.out.println("Master: Object sent to client");
////            Result result2 = (Result) in3.readObject();
////            System.out.println("Master Server: "+result2.toString());
////            System.out.println("Master: Object sent to client");
////            Result result3 = (Result) in4.readObject();
////            System.out.println("Master Server: "+result3.toString());
//
//
//            //}
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }finally {
//            try{
//                for(int i=0;i<numOfUsers;i++){
//                    in[i].close();
//                }
//                for(int i=0;i<numofworkers;i++){
//                    ins[i].close();
//                }
//                for(int i=0;i<numofworkers;i++){
//                    outs[i].close();
//                }
//                //in.close();
////                in2.close();
////                in3.close();
////                in4.close();
////                //out.close();
////                out2.close();
////                out3.close();
////                out4.close();
//                for(int i=0;i<numOfUsers;i++){
//                    requestSocket[i].close();
//                }
//                for(int i=0;i<numofworkers;i++){
//                    workerSockets[i].close();
//                }
//                //requestSocket.close();
////                workerSocket.close();
////                workerSocket2.close();
////                workerSocket3.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public static void main(String[] args){
//        for(int i=0;i<2;i++){
//            Thread t = new Thread(new MasterClient()::run);
//            t.start();}
//    }
//}
