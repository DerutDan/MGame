import java.util.HashMap;

/**
 * Created by Danila on 21.01.17.
 */
public class StaticVariables {
    static int deckSize = 20;
    static int maxHandSize = 5;
    static int initHandSize = 4;
    static int cardWidth = 50;
    static int cardHeight = 100;
    static int cardGap = 5;
    static boolean inited = false;
    static HashMap<String,Monster> MonsterDecl = new HashMap<>();
    static HashMap<String,Weapon> WeaponDecl = new HashMap<>();
    static HashMap<String,Armor> ArmorDecl = new HashMap<>();


    static void initMonsters()
    {
        Monster m1 = new Monster("Monster1",1,10,1);
        Monster m2 = new Monster("Monster2",2,10,2);
        Monster m3 = new Monster("Monster3",4,5,2);
        Monster m4 = new Monster("Monster4",20,100,6);
        Monster m5 = new Monster("Monster5",7,13,4);
        Monster m6 = new Monster("Monster6",6,10,3);
        Monster m7 = new Monster("Monster7",2,11,2);
        Monster m8 = new Monster("Monster8",3,13,3);
        Monster m9 = new Monster("Monster9",4,17,4);
        Monster m10 = new Monster("Monster10",8,19,5);
        MonsterDecl.put(m1.getName(),m1);
        MonsterDecl.put(m2.getName(),m2);
        MonsterDecl.put(m3.getName(),m3);
        MonsterDecl.put(m4.getName(),m4);
        MonsterDecl.put(m5.getName(),m5);
        MonsterDecl.put(m6.getName(),m6);
        MonsterDecl.put(m7.getName(),m7);
        MonsterDecl.put(m8.getName(),m8);
        MonsterDecl.put(m9.getName(),m9);
        MonsterDecl.put(m10.getName(),m10);
    }
    static void initWeapon()
    {
        Weapon w1 = new Weapon("Weapon1",1);
        Weapon w2 = new Weapon("Weapon2",2);
        Weapon w3 = new Weapon("Weapon3",4);
        WeaponDecl.put(w1.getName(),w1);
        WeaponDecl.put(w2.getName(),w2);
        WeaponDecl.put(w3.getName(),w3);

    }
    static void initArmor()
    {
        Armor a1 = new Armor("Armor1", 1, 10);
        Armor a2 = new Armor("Armor2", 2, 20);
        Armor a3 = new Armor("Armor3", 3, 30);
        ArmorDecl.put(a1.getName(),a1);
        ArmorDecl.put(a2.getName(),a2);
        ArmorDecl.put(a3.getName(),a3);

    }
    static void init()
    {
        if(!inited)
        {
            initMonsters();
            initWeapon();
            initArmor();
            inited = true;
        }
    }

}
