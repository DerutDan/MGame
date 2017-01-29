import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Danila on 29.01.17.
 */
public class MyMouseLis implements MouseListener {
    private int x = 0,y = 0;
    @Override
    public void mouseClicked(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        System.out.println(x + " " + y);
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
