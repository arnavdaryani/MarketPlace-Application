import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

public class MarketplaceServer {

    public static final int PORT = 5000;
    public static File f = new File("users.txt");
    public static File p = new File("products.txt");

    public static void main(String[] args) {
            try {
                ServerSocket ss = new ServerSocket(PORT);
                Socket socket = ss.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                BufferedReader userReader = new BufferedReader(new FileReader(f)); 
                BufferedReader productReader = new BufferedReader(new FileReader(p));  
                PrintWriter userWriter = new PrintWriter(new FileWriter(f, true));
                PrintWriter productWriter = new PrintWriter(new FileWriter(p, true));


                // Login or Signup process
                String userChoice =  reader.readLine();
                if (userChoice.equals("Login")) {
                    boolean loggedIn = false;
                    while (!loggedIn) {
                        String username = reader.readLine();
                        String password = reader.readLine();
                        String line = userReader.readLine();
                        while (line != null) {
                            String[] userData = line.split(",");
                            String storedUsername = userData[0];
                            String storedPassword = userData[1];
                            if (username.equals(storedUsername) && password.equals(storedPassword)) {
                                loggedIn = true;
                                break;
                            }
                            line = userReader.readLine();
                        }
                        writer.write(line);
                        writer.println();
                        writer.flush();
                        writer.write("true");
                        writer.println();
                        writer.flush();
                    }
                } else {
                    while (true) {
                        String username = reader.readLine();
                        String line = userReader.readLine();
                        while (line != null) {
                            String[] userData = line.split(",");
                            if (userData[0].equals(username)) {
                                writer.write("true");
                                writer.println();
                                writer.flush();
                                break;
                            }
                            line = userReader.readLine();
                        }
                        writer.write("false");
                        writer.println();
                        writer.flush();
                        break;
                    }

                    String userInfo = reader.readLine();
                    userWriter.write(userInfo);
                    userWriter.println();
                    userWriter.flush(); 
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    
}
