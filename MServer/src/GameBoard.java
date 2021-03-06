import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.lang.Thread.sleep;

public class GameBoard {
    Hero P1,P2;
    boolean isGameOn = false;
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
    DataInputStream din1,din2;
    InputStream sin1,sin2;
    OutputStream sout1,sout2;

    volatile boolean upToDate = false;
    Thread updater = upDate();
    Random rand = new Random();
    GameBoard(InputStream _sin1,OutputStream _sout1,InputStream _sin2, OutputStream _sout2)
    {
        sin1 = _sin1;
        sin2 = _sin2;
        sout1 = _sout1;
        sout2 = _sout2;
        initAll();
        deckP1 = getDeck();
        deckP2 = getDeck();
        for(int i = 0; i < StaticVariables.initHandSize;++i)
        {
            handP1.add(takeCard(deckP1));
            handP2.add(takeCard(deckP2));
        }
        P1 = new Hero();
        P1.foo = "Asd";
        P2 = new Hero();
        isGameOn = true;

        try {
            sout1.write(1);
            sout2.write(2);
            din1 = new DataInputStream(sin1);
            oout1 = new ObjectOutputStream(sout1);

            din2 = new DataInputStream(sin2);
            oout2 = new ObjectOutputStream(sout2);
        } catch (IOException e) {
            e.printStackTrace();
        }


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

    void Phase01()
    {
        System.out.println(1);
       wForUpDate();
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
        System.out.println(2);
        Phase11();
    }

    void Phase11()
    {
        System.out.println(3);
        wForUpDate();
        int cardIndex = reader1();
        System.out.println(cardIndex);
        if(cardIndex >=  0 && cardIndex < handP1.size()) {
            GameCard out = handP1.get(cardIndex);
            String type = out.type;
            switch (type) {
                case "Monster": {
                    cMonstersP1.add((Monster) out);
                    break;
                }
                case "Weapon": {
                    P1.equipWeapon((Weapon) out);
                    break;
                }
                case "Armor": {
                    P1.equipArmor((Armor) out);
                    break;
                }
            }
            handP1.remove(cardIndex);
            upToDate = false;
        }
        System.out.println(4);
        Phase21();
    }

    void Phase21()
    {
        System.out.println(5);
        int cardIndex = reader1();
        System.out.println(cardIndex);
        if(cardIndex >=  0 && cardIndex < monstersP2.size()) {
            Monster m = monstersP2.get(cardIndex);
            P1.getHit(m.getAt());
            m.getHit(P1.getAt());
            if (!m.isAlive()) monstersP2.remove(cardIndex);
            upToDate = false;
        }
        System.out.println(6);
        Phase31();
    }

    void Phase31()
    {
        System.out.println(7);
        wForUpDate();
        int sum = 0;
        for(int  i = 0 ; i < monstersP2.size();++i)
        {
            sum += monstersP2.get(i).getLevel();
        }
        P1.getHit(sum);
        if(!P1.isAlive()) isGameOn = false;
        upToDate = false;
        System.out.println(8);
        Phase02();
    }

    //P2 phases
    void Phase02()
    {
        System.out.println(9);
        wForUpDate();
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
        System.out.println(10);
        Phase12();
    }

    void Phase12()
    {
        System.out.println(11);
        wForUpDate();
        int cardIndex = reader2();
        System.out.println(cardIndex);
        if(cardIndex >=  0 && cardIndex < handP2.size()) {
            GameCard out = handP2.get(cardIndex);
            String type = out.type;
            switch (type) {
                case "Monster": {
                    cMonstersP2.add((Monster) out);
                    break;
                }
                case "Weapon": {
                    P2.equipWeapon((Weapon) out);
                    break;
                }
                case "Armor": {
                    P2.equipArmor((Armor) out);
                    break;
                }
            }
            handP2.remove(cardIndex);
            upToDate = false;
        }
        System.out.println(12);
        Phase22();

    }

    void Phase22()
    {
        System.out.println(13);
        wForUpDate();
        int cardIndex = reader2();
        System.out.println(cardIndex);
        if(cardIndex >=  0 && cardIndex < monstersP1.size()) {
            Monster m = monstersP1.get(cardIndex);
            P2.getHit(m.getAt());
            m.getHit(P2.getAt());
            if (!m.isAlive()) monstersP1.remove(cardIndex);
            upToDate = false;
        }
        System.out.println(14);
        Phase32();
    }

    void Phase32()
    {
        System.out.println(15);
        wForUpDate();
        int sum = 0;
        for(int  i = 0 ; i < monstersP1.size();++i)
        {
            sum += monstersP1.get(i).getLevel();
        }
        P2.getHit(sum);
        if(!P2.isAlive()) isGameOn = false;
        upToDate = false;
        System.out.println(16);
        Phase01();

    }

    //P1: hp def at weap arm cmosterNum AllMonsterIndexes monsterNum AllMonsterIndexes(curhp) P2 hp def at
    void toSent1()
    {
        try {
            oout1.writeObject(P1);
            oout1.writeObject(P2);
            oout1.writeInt(handP1.size());
            for(int i = 0; i < handP1.size();++i)
            {
                oout1.writeUTF(handP1.get(i).getT());
                oout1.writeObject(handP1.get(i));
            }
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
            oout1.flush();
        } catch (IOException e) {
           e.printStackTrace();
        }

    }

    void toSent2()
    {
        try {
            oout2.writeObject(P2);
            oout2.writeObject(P1);
            oout2.writeInt(handP2.size());
            for(int i = 0; i < handP2.size();++i)
            {
                oout2.writeUTF(handP2.get(i).getT());
                oout2.writeObject(handP2.get(i));
            }
            oout2.writeInt(cMonstersP2.size());
            for(int i = 0; i < cMonstersP2.size();++i)
            {
                oout2.writeObject(cMonstersP2.get(i));
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
            oout2.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    Thread upDate()
    {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while(isGameOn) {
                    if(!upToDate) {
                        toSent1();
                        toSent2();
                        upToDate = true;
                    }
                }
            }
        });

    }

    int reader1()
    {
        try {
            return din1.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    int reader2()
    {
        try {
            return din2.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void StartGame()
    {

        updater.start();
        Phase01();
    }

    void wForUpDate()
    {
        while(!upToDate)
            try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}