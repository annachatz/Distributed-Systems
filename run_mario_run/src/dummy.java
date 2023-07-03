import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class dummy {
    public static void main(String args[]){
        try(Socket socket = new Socket("localhost",8000)){
            String arg = "/Users/annachatz/Downloads/gpxs/route4.gpx";
            OutputStream out = socket.getOutputStream();
            out.write(arg.getBytes());
            out.flush();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);}
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }

    }

}
