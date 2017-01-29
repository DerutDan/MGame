import java.io.Serializable;

/**
 * Created by Danila on 20.01.17.
 */
public class Weapon extends GameCard implements Serializable {
    int at;
    Weapon(String _name,int _at)
    {
        name = _name;
        at = _at;
        type = "Weapon";
    }

    public int getAt() {
        return at;
    }


}
