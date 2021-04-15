package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

/**
 * this class is used to initialize a multi-threaded server
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
    public Server() throws IOException, InterruptedException {
        Socket clientSocket ;

        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        ClientConnectionHandler[] threads =
                new ClientConnectionHandler[MAX_THREAD];
        int count = 0;

        //initialize default userProfile
        if(!USER_PROFILE.exists()){
            fileManager fm = new fileManager();
            fm.createProfile(USER_PROFILE);
            System.out.println("User Profile not found, default profile created in Server_Data");
        }

        // to make sure it is multi-thread
        while (count<=1){
            clientSocket = serverSocket.accept();
            System.out.println("Connection "+count+" established");
            threads[count] = new ClientConnectionHandler(clientSocket,USER_PROFILE);
            threads[count].start();
            count++;
        }
        System.out.println("SERVER: Stopped listening for new connections");

        //check if both players are ready for battle

        while (true){
            System.out.println("SERVER: ready check for thread 1: "+threads[0].isReady());
            System.out.println("SERVER: ready check for thread 2: "+threads[1].isReady());
            if(threads[0].isReady()&&threads[1].isReady()){
                System.out.println("SERVER: Ready check completed!");
                battle(threads[0], threads[1]);
                threads[0].setReady(false);
                threads[1].setReady(false);
            }
            Thread.sleep(3000);
        }
    }

    /**
     * this method is the simulator as the battle going on
     * this is the battle history that will paste to the textarea that after clicking battle
     * when the battle is going, we set each item can deal how much damage
     * the rule for attacking with item, should rolling dice
     * if the point is over 70, then the player is allowed to use the item.
     * at last, it will declare the winner
     * @param p1 player1 connecting to sever
     * @param p2 player2 connecting to sever
     */
    public void battle(ClientConnectionHandler p1, ClientConnectionHandler p2){
        System.out.println("SERVER: Battle!");
        String log = "";
        Player player1;
        Player player2;

        p1.out.println("start");
        p2.out.println("start");
        //50% probability for first move
        int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
        if(randomNum==0){
            player1 = p1.getPlayer();
            player2 = p2.getPlayer();
            log += player1.username+" attacks first!#";
        }
        else {
            player1 = p2.getPlayer();
            player2 = p1.getPlayer();
            log += player2.username+" attacks first!#";
        }
        log +="Battle started between "+player1.username+" and "+player2.username+"#";

        boolean isOver = false, p1Victory = false;

        //Round loop
        int round=0;
        int p1_hp=player1.hp;
        int p2_hp=player2.hp;
        while(!isOver){
            //Round start Phase
            log +="Round:"+round+"#";
            log +="Player<"+player1.username+">hp:"+p1_hp+"#";
            log +="Player<"+player2.username+">hp:"+p2_hp+"#";

            //Calculate damage
            int damageByP1 = player1.getAttack()*player1.rank-player2.getDefence()*player2.rank;
            int damageByP2 = player2.getAttack()*player2.rank-player1.getDefence()*player1.rank;

            //item Phase
            int itemNum = ThreadLocalRandom.current().nextInt(0, 4);
            if(itemNum==0){
                log +="item used by"+player1.username+"#";
                if(player1.items.length!=0){
                    int item = ThreadLocalRandom.current().nextInt(0, player1.items.length);
                    if(player1.items[item].equalsIgnoreCase("Potion")){
                        log +="Potion!"+"#";
                        p1_hp+=20;
                    }
                    else if(player1.items[item].equalsIgnoreCase("bomb")){
                        log +="Bomb!"+"#";
                        damageByP1+=10;
                    }
                    else if(player1.items[item].equalsIgnoreCase("knife")){
                        log +="Knife!"+"#";
                        damageByP1+=5;
                    }
                    else if(player1.items[item].equalsIgnoreCase("armor")){
                        log +="Armor!"+"#";
                        damageByP2-=5;
                    }
                    else if(player1.items[item].equalsIgnoreCase("firebolt")){
                        log +="Firebolt!"+"#";
                        damageByP1+=20;
                    }
                }
                else {
                    log +=player1.username + " has no item!#";
                }
            }
            if(itemNum==1){
                log +="item used by <"+player2.username+">#";
                if(player2.items.length!=0){
                    int item = ThreadLocalRandom.current().nextInt(0, player2.items.length);
                    if(player2.items[item].equalsIgnoreCase("Potion")){
                        log +="Potion!#";
                        p2_hp+=20;
                    }
                    else if(player2.items[item].equalsIgnoreCase("bomb")){
                        log +="Bomb!#";
                        damageByP2+=10;
                    }
                    else if(player2.items[item].equalsIgnoreCase("knife")){
                        log +="Knife!#";
                        damageByP2+=5;
                    }
                    else if(player2.items[item].equalsIgnoreCase("armor")){
                        log +="Armor!#";
                        damageByP1-=5;
                    }
                    else if(player2.items[item].equalsIgnoreCase("firebolt")){
                        log +="Firebolt!#";
                        damageByP2+=20;
                    }
                }
                else {
                    log +=player2.username + " has no item!#";
                }
            }

            //attack phase
            log +="<"+player1.username+"> deals "+ damageByP1 + " damage to <"+player2.username+">#";
            log +="<"+player2.username+"> deals "+ (player1.getAttack()-player1.getDefence())+" damage to <"+player1.username+">#";
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
            log += "#";
        }
        if(p1Victory){
            log +=player1.username+" victory!#";
            player1.rank++;
        }
        else {
            log +=player2.username+" victory!#";
            player2.rank++;
        }
        log += "Battle Over#";
        p1.out.println(log);
        p2.out.println(log);
    }
    public static void main(String[] args) throws IOException, InterruptedException { Server server = new Server(); }
}
