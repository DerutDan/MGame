import java.io.Serializable;

public class Hero implements Serializable {
    int maxHp = 100;
    int hp = 100;
    int attack = 5;
    int def = 0;
    String foo = new String();
    boolean alive = true;
    Weapon weapon = new Weapon("Weapon0",0);
    Armor armor = new Armor("Armor0",0,0);
    void equipWeapon(Weapon _weapon)
    {
        if(weapon != null)
        {
           attack -= weapon.getAt();
        }
        weapon = _weapon;
        attack += weapon.getAt();
    }
    void equipArmor(Armor _armor)
    {
        if(armor != null)
        {
            def -= armor.getDef();
            maxHp -= armor.getHpBonus();
        }
        armor = _armor;
        def += armor.getDef();
        maxHp += armor.getHpBonus();
        hp += armor.getHpBonus();
    }
    void getHit(int atVal)
    {
        if(def < atVal) {
            hp -= atVal - def;
            if (hp <= 0) alive = false;
        }
    }

    public int getAt() {
        return attack;
    }

    boolean isAlive()
    {
        return alive;
    }
}
