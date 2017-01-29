import java.io.Serializable;

/**
 * Created by Danila on 20.01.17.
 */
public class Weapon extends GameCard implements Serializable {
    int at;
    Weapon(String _name,int _at)
    {
        name = _name;
        at = at;
    }

    public int getAt() {
        return at;
    }

    String getT() {
        return "Weapon";
    }
}
