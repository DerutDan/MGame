import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameBoard {
    Hero P1,P2;
    boolean gameOn = false;
    ArrayList<GameCard> handP1 = new ArrayList<>(),handP2 = new ArrayList<>();
    ArrayList<GameCard> deckP1,deckP2;
    ArrayList<Monster> monsterList = new ArrayList<>();
    ArrayList<Weapon> weaponList = new ArrayList<>();
    ArrayList<Armor> armorList = new ArrayList<>();
    ArrayList<Monster> cMonstersP1 = new ArrayList<>();
    ArrayList<Monster> cMonstersP2 = new ArrayList<>();
    ArrayList<Monster> monstersP1 = new ArrayList<>();
    ArrayList<Monster> monstersP2 = new ArrayList<>();
    ObjectOutputStream oout1,oout2;
    DataInputStream oin1,oin2;
    volatile boolean upToDate = false;
    Thread updater = upDate();
    Random rand = new Random();
    GameBoard(ObjectOutputStream _oout1,ObjectOutputStream _oout2, DataInputStream _oin1,DataInputStream _oin2)
    {
        initAll();
        deckP1 = getDeck();
        deckP2 = getDeck();
        for(int i = 0; i < StaticVariables.initHandSize;++i)
        {
            handP1.add(takeCard(deckP1));
            handP2.add(takeCard(deckP2));
        }
        P1 = new Hero();
        P2 = new Hero();
        gameOn = true;

        oout1 = _oout1;
        oout2 = _oout2;
        oin1 = _oin1;
        oin2 = _oin2;
    }


    void initAll()
    {
        //Monsters
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
        monsterList.add(m1);
        monsterList.add(m2);
        monsterList.add(m3);
        monsterList.add(m4);
        monsterList.add(m5);
        monsterList.add(m6);
        monsterList.add(m7);
        monsterList.add(m8);
        monsterList.add(m9);
        monsterList.add(m10);
        //Weapon
        Weapon w1 = new Weapon("Weapon1",1);
        Weapon w2 = new Weapon("Weapon2",2);
        Weapon w3 = new Weapon("Weapon3",4);
        weaponList.add(w1);
        weaponList.add(w2);
        weaponList.add(w3);
        //Armor
        Armor a1 = new Armor("Armor1", 1, 10);
        Armor a2 = new Armor("Armor2", 2, 20);
        Armor a3 = new Armor("Armor3", 3, 30);
        armorList.add(a1);
        armorList.add(a2);
        armorList.add(a3);
    }

    ArrayList<GameCard> getDeck()
    {
        ArrayList<GameCard> deck = new ArrayList<>(20);
        //Filling deck
        //Monsters
        for(int i = 0; i < 12;++i)
        {
            int k = Math.abs(rand.nextInt() % monsterList.size());
            Monster m = monsterList.get(k);
            deck.add(m);
        }
        //Weapon
        for(int i = 0; i < 4;++i)
        {
            int k = Math.abs(rand.nextInt() % weaponList.size());
            Weapon w = weaponList.get(k);
            deck.add(w);
        }
        //Armor
        for(int i = 0; i < 4;++i)
        {
            int k = Math.abs(rand.nextInt() % armorList.size());
            Armor a = armorList.get(k);
            deck.add(a);
        }
        shuffle(deck);
        return deck;
    }
    void shuffle(ArrayList<GameCard> deck)
    {
        Collections.shuffle(deck);
    }
    GameCard takeCard(ArrayList<GameCard> deck)
    {
        int size = deck.size();
        if(size > 0) {
            GameCard out = deck.get(size - 1);
            deck.remove(size - 1);
            return out;
        }
        else return new Monster("Monster0",1,1,5);
    }

    public ArrayList<GameCard> getHandP1() {
        return handP1;
    }

    public ArrayList<GameCard> getHandP2() {
        return handP2;
    }

    public boolean isGameOn() {
        return gameOn;
    }
    void Phase01()
    {
        if(handP1.size() <=StaticVariables.handSize) {
            handP1.add(takeCard(deckP1));
        }
        for(int  i = 0 ; i < cMonstersP1.size();++i)
        {
            if(cMonstersP1.get(i).chargeUp())
            {
                monstersP1.add(cMonstersP1.get(i));
                cMonstersP1.remove(i);
            }
        }
        upToDate = false;
    }
    void Phase11(int cardIndex)
    {
        GameCard out = handP1.get(cardIndex);
        String type = out.type;
        switch(type)
        {
            case "Monster" : { cMonstersP1.add((Monster)out); break;}
            case "Weapon" : { P1.equipWeapon((Weapon)out); break;}
            case "Armor" : { P1.equipArmor((Armor)out); break;}
        }
        upToDate = false;
    }
    void Phase21(int cardIndex)
    {
        if(cardIndex == -1 || cardIndex >= monstersP2.size())
            return;
        Monster m = monstersP2.get(cardIndex);
        P1.getHit(m.getAt());
        m.getHit(P1.getAt());
        if(!m.isAlive()) monstersP2.remove(cardIndex);
        upToDate = false;
    }
    void Phase31()
    {
        int sum = 0;
        for(int  i = 0 ; i < monstersP2.size();++i)
        {
            sum += monstersP2.get(i).getLevel();
        }
        P1.getHit(sum);
        if(!P1.isAlive()) gameOn = false;
        upToDate = false;
    }
    //P2 phases
    void Phase02()
    {
        if(handP2.size() <=StaticVariables.handSize){
            handP2.add(takeCard(deckP2));
        }
        for(int  i = 0 ; i < cMonstersP2.size();++i)
        {
            if(cMonstersP2.get(i).chargeUp())
            {
                monstersP1.add(cMonstersP2.get(i));
                cMonstersP2.remove(i);
            }
        }
        upToDate = false;
    }
    void Phase12(int cardIndex)
    {
        GameCard out = handP2.get(cardIndex);
        String type = out.type;
        switch(type)
        {
            case "Monster" : { cMonstersP2.add((Monster)out); break;}
            case "Weapon" : { P2.equipWeapon((Weapon)out); break;}
            case "Armor" : { P2.equipArmor((Armor)out); break;}
        }
        upToDate = false;
    }
    void Phase22(int cardIndex)
    {
        if(cardIndex == -1 || cardIndex >= monstersP1.size())
            return;
        Monster m = monstersP1.get(cardIndex);
        P2.getHit(m.getAt());
        m.getHit(P2.getAt());
        if(!m.isAlive()) monstersP1.remove(cardIndex);
        upToDate = false;
    }
    void Phase32()
    {
        int sum = 0;
        for(int  i = 0 ; i < monstersP1.size();++i)
        {
            sum += monstersP1.get(i).getLevel();
        }
        P2.getHit(sum);
        if(!P2.isAlive()) gameOn = false;
        upToDate = false;
    }
    //P1: hp def at weap arm cmosterNum AllMonsterIndexes monsterNum AllMonsterIndexes(curhp) P2 hp def at
    void toSent1()
    {
        try {
            oout1.writeObject(P1);
            oout1.writeObject(P2);
            oout1.writeInt(cMonstersP1.size());
            for(int i = 0; i < cMonstersP1.size();++i)
            {
                oout1.writeObject(cMonstersP1.get(i));
            }
            oout1.writeInt(monstersP1.size());
            for(int i = 0; i < monstersP1.size();++i)
            {
                oout1.writeObject(monstersP1.get(i));
            }
            oout1.writeInt(cMonstersP2.size());
            for(int i = 0; i < cMonstersP2.size();++i)
            {
                oout1.writeObject(cMonstersP2.get(i));
            }
            oout1.writeInt(monstersP2.size());
            for(int i = 0; i < monstersP2.size();++i)
            {
                oout1.writeObject(monstersP2.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    void toSent2()
    {
        try {
            oout2.writeObject(P2);
            oout2.writeObject(P1);
            oout2.writeInt(cMonstersP2.size());
            for(int i = 0; i < cMonstersP2.size();++i)
            {
                oout1.writeObject(cMonstersP2.get(i));
            }
            oout2.writeInt(monstersP2.size());
            for(int i = 0; i < monstersP2.size();++i)
            {
                oout2.writeObject(monstersP2.get(i));
            }
            oout2.writeInt(cMonstersP1.size());
            for(int i = 0; i < cMonstersP1.size();++i)
            {
                oout2.writeObject(cMonstersP1.get(i));
            }
            oout2.writeInt(monstersP1.size());
            for(int i = 0; i < monstersP1.size();++i)
            {
                oout2.writeObject(monstersP1.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    Thread upDate() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                toSent1();
                toSent2();
                upToDate = true;
            }
        });

    }
    public void StartGame()
    {

    }

}