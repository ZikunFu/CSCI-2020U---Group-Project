package sample;

public class Player {
    String username, password;
    String[] items;
    int hp, attack, defence, rank;
    public Player(String name, String pw, int hp, int atk, int def, int rank, String[]items){
        username=name;
        password=pw;
        this.hp=hp;
        attack=atk;
        defence=def;
        this.rank=rank;
        this.items=items;
    }
    public int getAttack(){
        return attack*rank;
    }
    public int getDefence(){
        return defence*rank;
    }

}
