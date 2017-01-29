import javax.swing.*;
import java.util.ArrayList;


public class Board extends JFrame {

    int width,height,player = 0;
    ArrayList<JLabel> hand = new ArrayList<>(StaticVariables.maxHandSize);
    int handSize = 4;
    Thread updater = upToDate();
    Board()
    {
        height = 1000;
        width = 600;
        setFocusable(true);
        setBounds(0, 0, width, height);
        JPanel panel = new JPanel();
        for(int i = 0; i < StaticVariables.maxHandSize;++i)
        {
            MyMouseLis ml =  new MyMouseLis();
            JLabel lbl = hand.get(i);
            lbl.setBounds(i*(StaticVariables.cardWidth + StaticVariables.cardGap),0
                    , StaticVariables.cardWidth, StaticVariables.cardHeight);
            lbl.addMouseListener(ml);
            panel.add(lbl);
        }


        add(panel);
        //panel.addMouseListener(ml);
    }
    private Thread upToDate()
    {
        return new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void StartGame(int player) {
        setVisible(true);
        this.player = player;
        updater.start();
    }
}
