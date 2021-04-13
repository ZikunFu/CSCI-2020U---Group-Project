package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * this class is used to initialize multi-threads server
 */
public class Server {
    //server config
    protected File USER_PROFILE = new File("Server_Data/userProfile.csv");
    protected int SERVER_PORT = 16789;
    protected int MAX_THREAD = 20;

    /**
     * this method is used to establish multiple threads
     * @throws IOException
     */
    public Server() throws IOException {
        Socket clientSocket ;

        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        ClientConnectionHandler[] threads =
                new ClientConnectionHandler[MAX_THREAD];
        int count = 1;

        //initialize default userProfile
        if(!USER_PROFILE.exists()){
            fileManager fm = new fileManager();
            fm.createProfile(USER_PROFILE);
        }

        // to make sure it is multi-thread
        while (count<=2){
            clientSocket = serverSocket.accept();
            System.out.println("Connection "+count+" established");
            threads[count] = new ClientConnectionHandler(clientSocket,USER_PROFILE);
            threads[count].start();
            count++;
        }

        //check if both players are ready for battle
        while (true){
            if(threads[0].isReady()&&threads[1].isReady()){
                System.out.println("server ready check!");
                battle(threads[0], threads[1]);
                threads[0].setReady(false);
                threads[1].setReady(false);
            }
        }
    }
    public void battle(ClientConnectionHandler player1, ClientConnectionHandler player2){
        System.out.println("Battle started between "+player1.getPlayer().username+" and "+player2.getPlayer().username);
    }
    public static void main(String[] args) throws IOException { Server server = new Server(); }
}
