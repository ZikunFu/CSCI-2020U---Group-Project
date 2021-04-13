package sample;

import java.io.*;
import java.net.Socket;
public class ClientConnectionHandler extends Thread{
    protected Socket clientSocket;//Socket
    protected BufferedReader in;  //networkInput
    protected PrintWriter out;    //networkOutput
    protected File userProfile;

    public ClientConnectionHandler(Socket socket, File userProfile) throws IOException {
        super();
        clientSocket = socket;
        this.userProfile = userProfile;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void run() {
        String input = null, command, argument;
        try {
            System.out.println("listening for message");
            input = in.readLine();
            System.out.println("message received: <"+input+">");
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
                /*try {*/ threadEnd = serverAction(command, argument); //}
                /*
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.err.println("Error: "+e);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Error: "+e);
                }

                 */
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
    protected void userLogin(String command, String argument) throws IOException {
        String username;
        String password;
        username = argument.split(" ")[0];
        password = argument.split(" ")[1];
        if(command == "Login"){
            fileManager fm = new fileManager();
            boolean accountExist = false, passwordExist = false;
            accountExist = fm.matchCSV(userProfile,username,0);
            passwordExist = fm.matchCSV(userProfile,password,1);
            if(accountExist&&passwordExist){
                out.println("correct");
            }
            else if(!accountExist){
                out.println("invalidAccount");
            }
            else {
                out.println("invalidPassword");
            }
        }
        else if(command == "Register"){
            fileManager fm = new fileManager();
            boolean accountExist = false;
            accountExist = fm.matchCSV(userProfile,argument,0);
            if(accountExist){
                out.println("invalidAccount");
            }
            else {
                fm.appendCSV(userProfile,username + " " + password + "50,10,2,1,potion bomb knife armor fireball");
                out.println("correct");
            }
        }

    }
    protected void getProfile(String command, String argument) throws IOException {
        fileManager fm = new fileManager();
        String username = argument;
        String data = fm.searchCSV(userProfile,username,0);
        String temp[] = data.split(" ");
        if(command == "profile"){
            //hp, attack, defence, rank
            String profile = temp[2]+" "+temp[3]+" "+temp[4]+" "+temp[5];
            out.println(profile);
        }
        else if(command == "bag"){
            //item
            String bag = temp[6];
            out.println(bag);
        }
    }
    protected void battle(){

    }


}
