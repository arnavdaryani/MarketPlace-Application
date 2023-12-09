import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Marketplace Server
 * 
 * This class represents the server to which the client connects
 * and the thread is started
 * 
 * @author Sathvik Swamy, Neha Jain, Dariush Mokhlesi, Arnav Daryani
 * 
 * @version December 2023
 */

public class MarketplaceServer {

    public static final int PORT = 5000;

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(PORT);
            while (true) {
                Socket socket = ss.accept();
                ThreadManager tm = new ThreadManager(socket);
                Thread thread = new Thread(tm);
                thread.start();
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
