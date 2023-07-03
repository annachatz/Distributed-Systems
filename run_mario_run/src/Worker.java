import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker {
    private int port;
    ServerSocket server;
    Socket providerSocket;
    Worker(int port){
        this.port = port;
    }
//    public static  void main(String[] args){
//        new Worker(9000).openServer();
//        new Worker(9001).openServer();
//    }
    public void openServer(){
        try {
            server = new ServerSocket(port);
            while(true){
                providerSocket = server.accept();
                Thread t = new ActionsForWorkers(providerSocket);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                providerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    public static void main(String[] args){
//        new Worker(1027).openServer();
//        new Worker(1026).openServer();
//    }

}
