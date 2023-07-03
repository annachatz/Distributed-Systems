import javax.xml.crypto.dsig.XMLObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;
import java.net.*;
import java.net.Socket;
import java.io.IOException;
public class UserClient extends Thread{
    String path;
    String address;
    UserClient(String address,String str){
        this.path = str;
        this.address = address;
    }

    public void run(){
        //Socket re = null;
        Socket requestSocket = null;
        ObjectOutputStream out = null;

        try{
            requestSocket = new Socket(address,6060);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            String rr = new String(path);

            File file = new File(rr);
            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] buffer = new byte[1024];
            int bytesRead = 0;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            fileInputStream.close();

            //File file = new File(rr);
            //out.writeObject(file);
            //out.flush();
            System.out.println(rr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }


    }
    public static void main(String [] args) throws IOException {
        new UserClient("localhost","/Users/annachatz/Downloads/gpxs/route2.gpx").start();
/*
                    new UserClient(args[0],args[1]).start();
        new UserClient("localhost","/Users/annachatz/Downloads/gpxs/route5.gpx").start();
                new UserClient("/Users/annachatz/Downloads/gpxs/route2.gpx").start();
                new UserClient("/Users/annachatz/Downloads/gpxs/route3.gpx").start();
                new UserClient("/Users/annachatz/Downloads/gpxs/route4.gpx").start();
                new UserClient("/Users/annachatz/Downloads/gpxs/route5.gpx").start();
        new UserClient("/Users/annachatz/Downloads/gpxs/route6.gpx").start();
        */

//        try (ServerSocket serverSocket = new ServerSocket(8000)) {
//            System.out.println("Waiting for connection from Android application...");
//
//            Socket clientSocket = serverSocket.accept();
//            System.out.println("Connected to Android application.");
//
//            InputStream inputStream = clientSocket.getInputStream();
//            byte[] buffer = new byte[1024];
//            int bytesRead = inputStream.read(buffer);
//            String receivedData = new String(buffer, 0, bytesRead);
//            System.out.println("Received string from Android application: " + receivedData);
//
//            // Process the received string as desired
//            new UserClient("localhost",receivedData).start();
//            clientSocket.close();
//            System.out.println("Connection closed.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

