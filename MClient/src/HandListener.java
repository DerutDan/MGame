import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Danila on 29.01.17.
 */
public class HandListener implements MouseListener {
    DataOutputStream dout;
    ArrayList<GameCard> hand;
    int num;
    HandListener(DataOutputStream _dout,int _num)
    {
        dout = _dout;
        num = _num;

    }
    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            dout.writeInt(num);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
