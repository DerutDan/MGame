import javax.swing.*;


public class Board extends JFrame {

    int width,height,player = 0;
    Board()
    {
        height = 600;
        width = 600;
        setFocusable(true);
        setBounds(0, 0, width, height);
        MyMouseLis ml =  new MyMouseLis();
        JPanel panel = new JPanel();
        //MouseListener mll = ;
        add(panel);
        panel.addMouseListener(ml);
    }
    private Thread communicate()
    {
        return new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}
