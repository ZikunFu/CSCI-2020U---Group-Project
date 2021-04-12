package sample;

import java.io.*;
import java.net.Socket;
public class ClientConnectionHandler extends Thread{
    protected Socket clientSocket;//Socket
    protected BufferedReader in;  //networkInput
    protected PrintWriter out;    //networkOutput
    protected File directory;     //server folder

    public ClientConnectionHandler(Socket socket, File dir) throws IOException {
        super();
        clientSocket = socket;
        directory = dir;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void run() {
        String input = null, command, argument;
        try {
            System.out.println("listening for command");
            input = in.readLine();
            System.out.println("command received: <"+input+">");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: "+e);
        }
        if(input!=null){
            //check for single line command
            if(input.split(" ").length!=1){
                command = input.split(" ", 2)[0];
                argument = input.split(" ", 2)[1];
            }
            else{
                command= input.split(" ", 2)[0];
                argument = null;
            }
            boolean threadEnd = false;
            while (!threadEnd) {
                try { threadEnd = serverAction(command, argument); }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.err.println("Error: "+e);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Error: "+e);
                }
            }

            //closing socket
            try { clientSocket.close(); }
            catch (IOException e) { e.printStackTrace(); }
        }
        else { System.err.println("Error: Command is null"); }
    }
    protected boolean serverAction(String command, String argument){
        return true;
    }

}
