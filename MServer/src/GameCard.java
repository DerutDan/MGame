import java.io.Serializable;

/**
 * Created by Danila on 20.01.17.
 */
public class GameCard implements Serializable  {
    String type; //Monster or potion, wepaon;
    String name;

    public String getName() {
        return name;
    }
    public String getT() { return type;}
}
