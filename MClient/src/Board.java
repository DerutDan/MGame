import javax.swing.*;
import java.io.*;
import java.util.ArrayList;


public class Board extends JFrame {

    int width,height,player = 0;
    Hero P1,P2;
    volatile ArrayList<Monster> cMonstersP1 = new ArrayList<>();
    volatile ArrayList<Monster> cMonstersP2 = new ArrayList<>();
    volatile ArrayList<Monster> monstersP1 = new ArrayList<>();
    volatile ArrayList<Monster> monstersP2 = new ArrayList<>();
    DataOutputStream dout;
    ObjectInputStream oin;
    volatile ArrayList<GameCard> hand = new ArrayList<>();
    ArrayList<JLabel> handL = new ArrayList<>(StaticVariables.maxHandSize);
    JPanel panel;
    Thread updater = upToDate();
    Board()
    {
        height = 1000;
        width = 600;
        setFocusable(true);
        setBounds(0, 0, width, height);
        panel = new JPanel();
        panel.setLayout(null);
        for(int i = 0; i < StaticVariables.maxHandSize;++i)
        {
            MyMouseLis ml =  new MyMouseLis();
            JLabel lbl = new JLabel("Imhere");
            lbl.setBounds(i*(StaticVariables.cardWidth + StaticVariables.cardGap),0
                    , StaticVariables.cardWidth, StaticVariables.cardHeight);


            lbl.setVisible(true);
            handL.add(lbl);
            panel.add(handL.get(i));
        }

        add(panel);
        //panel.addMouseListener(ml);
    }
    private Thread upToDate()
    {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Monster> tempM;
                    ArrayList<GameCard> tempGC;
                    P1 = (Hero)oin.readObject();
                    P2 = (Hero)oin.readObject();
                    int handSize = oin.readInt();
                    tempGC = new ArrayList<>(handSize);
                    for(int i = 0; i < handSize;++i)
                    {
                        String type = oin.readUTF();
                        switch(type)
                        {
                            case "Monster":{tempGC.add((Monster) oin.readObject());break;}
                            case "Armor":{tempGC.add((Armor) oin.readObject());break;}
                            case "Weapon":{tempGC.add((Weapon) oin.readObject());break;}
                        }

                    }
                    hand = tempGC;
                    int cmSize1 = oin.readInt();
                    tempM = new ArrayList<>(cmSize1);
                    for(int i = 0; i < cmSize1;++i)
                    {

                        tempM.add((Monster)oin.readObject());
                    }
                    cMonstersP1 = tempM;
                    int mSize1 = oin.readInt();
                    tempM = new ArrayList<>(mSize1);
                    for(int i = 0; i < cmSize1;++i)
                    {
                        tempM.add((Monster)oin.readObject());
                    }
                    monstersP1 = tempM;

                    int cmSize2 = oin.readInt();
                    tempM = new ArrayList<>(cmSize2);
                    for(int i = 0; i < cmSize2;++i)
                    {

                        tempM.add((Monster)oin.readObject());
                    }
                    cMonstersP2 = tempM;
                    int mSize2 = oin.readInt();
                    tempM = new ArrayList<>(mSize2);
                    for(int i = 0; i < cmSize2;++i)
                    {
                        tempM.add((Monster)oin.readObject());
                    }
                    monstersP2 = tempM;
                    System.out.println("Didit");
                    labelsUpdate();
                    handL.get(0).repaint();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    void labelsUpdate()
    {
        for(int i = 0; i < hand.size();++i)
        {
            handL.get(i).setText(hand.get(i).getName());
        }
    }
    public void StartGame(int player, OutputStream sout, InputStream sin) {
        try {
            dout = new DataOutputStream(sout);
            oin = new ObjectInputStream(sin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setVisible(true);
        this.player = player;
        StaticVariables.init();
        updater.start();

    }
}
