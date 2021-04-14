package sample;

import java.io.*;
import java.net.Socket;

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
            command = input.split(" ", 2)[0];
            argument = input.split(" ", 2)[1];
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
            getProfile(command,argument);
        }
        else if(command.equals("battle")){
            battle(argument);
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
            System.out.println("Login received with argument<"+argument+">");
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
        else if(command.equals("Register")){
            System.out.println("Register received with argument<"+argument+">");
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
     * @param argument the argument in message
     * @throws IOException
     */
    protected void getProfile(String command, String argument) throws IOException {
        fileManager fm = new fileManager();
        String username = argument;
        String data = fm.searchCSV(userProfile,username,0);
        String temp[] = data.split(" ");
        if(command.equals("profile")){
            System.out.println("Profile received with argument<"+argument+">");
            //hp, attack, defence, rank
            String profile = temp[2]+" "+temp[3]+" "+temp[4]+" "+temp[5];
            out.println(profile);
        }
        else if(command.equals("bag")){
            System.out.println("bag received with argument<"+argument+">");
            //item
            String bag = temp[6];
            out.println(bag);
        }
    }

    /**
     * This method is used to realize the battle command
     * it can alert the server to initiate battle and
     * create the class Player based on the current user profile
     * @throws IOException
     */
    protected void battle(String username) throws IOException {
        System.out.println("Battle received with username<"+username+">");
        ready = true;
        fileManager fm = new fileManager();
        String data = fm.searchCSV(userProfile,username,0);
        String temp[] = data.split(" ");
        currentPlayer = new Player(temp[0],temp[1],Integer.parseInt(temp[2]),Integer.parseInt(temp[3]),Integer.parseInt(temp[4]),Integer.parseInt(temp[5]),temp[6].split(" "));
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
