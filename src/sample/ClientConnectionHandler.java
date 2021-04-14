package sample;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

/**
 * this class is a single thread for a given user
 */
public class ClientConnectionHandler extends Thread{
    protected Socket clientSocket;//Socket
    protected BufferedReader in;  //networkInput
    protected PrintWriter out;    //networkOutput
    protected File userProfile;
    protected boolean ready;
    protected Player currentPlayer;

    //Constructor
    public ClientConnectionHandler(Socket socket, File userProfile) throws IOException {
        super();
        clientSocket = socket;
        this.userProfile = userProfile;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        ready = false;
    }

    /**
     * This method runs the thread
     * it uses listen() and serverAction()
     * to listen and respond to client messages
     * @throws IOException
     */
    public void run() {
        boolean threadEnd = false;
        while (!threadEnd) {
            String input = null, command, argument;
            input = listen();

            //check for single line command
            if(input.split(" ").length!=1){
                command = input.split(" ", 2)[0];
                argument = input.split(" ", 2)[1];
            }
            else{
                command= input.split(" ", 2)[0];
                argument = null;
            }
            if(input!=null){
                try { threadEnd = serverAction(command, argument); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
        //closing socket
        try { clientSocket.close(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * This method is used to listen for message from client
     * then returns the message
     * @return The message we heard from client
     */
    protected String listen(){
        String input = null;
        try {
            System.out.println("listening for message");
            input = in.readLine();
            System.out.println("message received: <"+input+">");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: "+e);
        }
        return input;
    }

    /**
     * This method is used to extinguish different command from the message
     * @param command the command in message
     * @param argument the argument in message
     * @return True if we want the thread to end
     * @throws IOException
     */
    protected boolean serverAction(String command, String argument) throws IOException {
        System.out.println("Server Action");
        if(command.equals("Login")||command.equals("Register")){
            userLogin(command,argument);
        }
        else if(command.equals("profile")||command.equals("bag")){
            getProfile(command);
        }
        else if(command.equals("battle")){
            battle();
        }
        return false;
    }

    /**
     * This method is used to realize the login\register command
     * it can authenticate the user login or register an account
     * @param command the command in message
     * @param argument the argument in message
     * @throws IOException
     */
    protected void userLogin(String command, String argument) throws IOException {
        String username;
        String password;
        username = argument.split(" ")[0];
        password = argument.split(" ")[1];
        if(command.equals("Login")){
            //System.out.println("THREAD: Login received with argument<"+argument+">");
            fileManager fm = new fileManager();
            boolean accountExist = false, passwordExist = false;
            accountExist = fm.matchCSV(userProfile,username,0);
            passwordExist = fm.matchCSV(userProfile,password,1);
            if(accountExist&&passwordExist){
                out.println("correct");
                //Record user profile
                String data = fm.searchCSV(userProfile,username,0);
                String temp[] = data.split(",");
                currentPlayer = new Player(temp[0],temp[1],Integer.parseInt(temp[2]),Integer.parseInt(temp[3]),Integer.parseInt(temp[4]),Integer.parseInt(temp[5]),temp[6].split(" "));
            }
            else if(!accountExist){
                out.println("invalidAccount");
            }
            else {
                out.println("invalidPassword");
            }
        }
        else if(command.equals("Register")){
            //System.out.println("THREAD: Register received with argument<"+argument+">");
            fileManager fm = new fileManager();
            boolean accountExist = false;
            accountExist = fm.matchCSV(userProfile,username,0);
            if(accountExist){
                out.println("invalidAccount");
            }
            else {
                fm.appendCSV(userProfile,username + "," + password + ",50,10,2,1,potion bomb knife armor fireball");
                out.println("correct");
            }
        }

    }

    /**
     * This method is used to realize the profile\bag command
     * it can send the user's profile to the client
     * @param command the command in message
     * @throws IOException
     */
    protected void getProfile(String command) throws IOException {
        fileManager fm = new fileManager();
        String username = currentPlayer.username;
        String data = fm.searchCSV(userProfile,username,0);
        System.out.println("accessing profile: ");
        System.out.println("data: "+data);
        String temp[] = data.split(",");

        if(command.equals("profile")){
            //System.out.println("Profile received with argument<"+username+">");
            //hp, attack, defence, rank
            out.println(data);
        }
        else if(command.equals("bag")){
            //System.out.println("bag received with argument<"+username+">");
            //item
            String bag = temp[6];
            out.println(bag);
        }
    }

    /**
     * This method is used to realize the battle command
     * it alerts the server to initiate battle
     */
    protected void battle(){
        System.out.println("THREAD: Battle received with username <"+currentPlayer.username+">");
        ready = true;
    }

    //getter and setter for player profile and battle indicators
    public boolean isReady(){
        return ready;
    }
    public void setReady(boolean setter){
        ready = setter;
    }
    public Player getPlayer(){
        return currentPlayer;
    }


}
