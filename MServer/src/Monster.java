import java.io.Serializable;

public class Monster extends GameCard implements Serializable {
    int hp;
    int at;
    int charge;
    int level;
    boolean alive = false;
    Monster(String _name,int _at,int _hp,int _charge)
    {
        name = _name;
        type = "Monster";
        hp = _hp;
        at = _at;
        charge = _charge;
        level = charge;
        alive = true;
    }
    void getHit(int atVal)
    {
        hp -= atVal;
        if(hp <= 10) alive = false;
    }

    public int getAt() {
        return at;
    }
    boolean chargeUp()
    {
        if(charge >= 0)charge--;
        if(charge == 0) return true;
        else return false;
    }

    public int getLevel() {
        return level;
    }

    public boolean isAlive() {
        return alive;
    }



}
