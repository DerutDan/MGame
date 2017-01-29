import java.io.Serializable;

/**
 * Created by Danila on 25.01.17.
 */
public class Armor extends GameCard implements Serializable {
    int def;
    int hpBonus;
    Armor(String _name,int _def,int _hpBonus)
    {
        name = _name;
        def = _def;
        hpBonus = _hpBonus;
        type = "Armor";

    }

    public int getDef() {
        return def;
    }

    public int getHpBonus() {
        return hpBonus;
    }
}
