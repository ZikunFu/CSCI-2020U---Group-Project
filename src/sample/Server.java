package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

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
            System.out.println("User Profile not found, default profile created in Server_Data");
        }

        // to make sure it is multi-thread
        while (count<=3){
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
    public void battle(ClientConnectionHandler p1, ClientConnectionHandler p2){
        Player player1;
        Player player2;

        int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
        if(randomNum==0){
            player1 = p1.getPlayer();
            player2 = p2.getPlayer();
            System.out.println(player1.username+" attack first!");
        }
        else {
            player1 = p2.getPlayer();
            player2 = p1.getPlayer();
            System.out.println(player2.username+" attack first!");
        }
        System.out.println("Battle started between "+player1.username+" and "+player2.username);

        boolean isOver = false, p1Victory=false;

        int round=0;
        int p1_hp=player1.hp;
        int p2_hp=player2.hp;
        while(!isOver){
            //Round start Phase
            System.out.println("Round: "+round);
            System.out.println("Player <"+player1.username+"> hp:"+player1.hp);
            System.out.println("Player <"+player2.username+"> hp:"+player2.hp);

            //Calculate damage
            int damageByP1 = player1.getAttack()-player2.getDefence();
            int damageByP2 = player1.getAttack()-player2.getDefence();

            //item Phase
            int itemNum = ThreadLocalRandom.current().nextInt(0, 4);
            if(itemNum==0){
                System.out.println("item used by"+player1.username);
                if(player1.items.length!=0){
                    int item = ThreadLocalRandom.current().nextInt(0, player1.items.length);
                    if(player1.items[item].equalsIgnoreCase("Potion")){
                        System.out.println("Potion!");
                        p1_hp+=20;
                    }
                    else if(player1.items[item].equalsIgnoreCase("bomb")){
                        System.out.println("Bomb!");
                        damageByP1+=10;
                    }
                    else if(player1.items[item].equalsIgnoreCase("knife")){
                        System.out.println("Knife!");
                        damageByP1+=5;
                    }
                    else if(player1.items[item].equalsIgnoreCase("armor")){
                        System.out.println("Armor!");
                        damageByP2-=5;
                    }
                    else if(player1.items[item].equalsIgnoreCase("firebolt")){
                        System.out.println("Firebolt!");
                        damageByP1+=20;
                    }
                }
                else {
                    System.out.println(player1.username + " has no item!");
                }
            }
            if(itemNum==1){
                System.out.println("item used by <"+player2.username+">");
                if(player2.items.length!=0){
                    int item = ThreadLocalRandom.current().nextInt(0, player2.items.length);
                    if(player2.items[item].equalsIgnoreCase("Potion")){
                        System.out.println("Potion!");
                        p2_hp+=20;
                    }
                    else if(player2.items[item].equalsIgnoreCase("bomb")){
                        System.out.println("Bomb!");
                        damageByP2+=10;
                    }
                    else if(player2.items[item].equalsIgnoreCase("knife")){
                        System.out.println("Knife!");
                        damageByP2+=5;
                    }
                    else if(player2.items[item].equalsIgnoreCase("armor")){
                        System.out.println("Armor!");
                        damageByP1-=5;
                    }
                    else if(player2.items[item].equalsIgnoreCase("firebolt")){
                        System.out.println("Firebolt!");
                        damageByP2+=20;
                    }
                }
                else {
                    System.out.println(player2.username + " has no item!");
                }
            }

            //attack phase
            System.out.println("<"+player1.username+"> deals "+ damageByP1 + " damage to <"+player2.username+">");
            System.out.println("<"+player2.username+"> deals "+ (player1.getAttack()-player1.getDefence())+" damage to <"+player1.username+">");
            p1_hp -= damageByP1;
            p2_hp -= damageByP2;

            // Check if battle over
            if(p2_hp<=0){
                isOver = true;
                p1Victory = true;
            }
            else if(p1_hp<=0){
                isOver = true;
            }
            round++;
        }
        if(p1Victory){
            System.out.println(player1.username+" victory!");
            player1.rank++;
        }
        else {
            System.out.println(player2.username+" victory!");
            player2.rank++;
        }
        System.out.println("Battle Over");
    }
    public static void main(String[] args) throws IOException { Server server = new Server(); }
}
