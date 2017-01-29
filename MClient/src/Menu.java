import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Menu extends JFrame {
    int serverPort = 5555,height,width;
    String address = "192.168.1.181";
    Socket socket;
    JButton findGame,help,cancelFindGame;
    JLabel label;
    InetSocketAddress ipAddress;
    volatile boolean isCanceled = false;
    Menu() {
        height = 773;
        width = 1280;
        setFocusable(true);
        setBounds(0, 0, width, height);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);
        findGame = new JButton("Find Game");
        help = new JButton("Help");
        cancelFindGame = new JButton("Cancel");
        label = new JLabel("Waiting for opponent...",JLabel.CENTER);
        cancelFindGame.setVisible(false);
        label.setVisible(false);
        label.setBounds(400,10,200,50);
        findGame.setBounds(400,10,200,50);
        help.setBounds(400,80,200,50);
        cancelFindGame.setBounds(400,80,200,50);
        ipAddress = new InetSocketAddress(address,serverPort);


        panel.add(findGame);
        panel.add(help);
        panel.add(cancelFindGame);
        panel.add(label);

        findGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    help.setVisible(false);
                    findGame.setVisible(false);
                    cancelFindGame.setVisible(true);
                    label.setVisible(true);
                    Thread thr = connectingThread();
                    thr.start();

            }
        });

        cancelFindGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                help.setVisible(true);
                findGame.setVisible(true);
                cancelFindGame.setVisible(false);
                label.setVisible(false);
                isCanceled = true;

                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    private Thread connectingThread()
    {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket();
                    socket.connect(ipAddress,1000);

                }
                catch (IOException err) {
                    if(err.getMessage().equals("Connection refused"))
                    {
                        backToMenu();
                        System.out.println("Server is not working at the moment");
                    }
                    else err.printStackTrace();
                }
                //if(!iscanceled && socket.isConnected())
                isCanceled = false;
            }
        });
    }
    private void backToMenu()
    {
            help.setVisible(true);
            findGame.setVisible(true);
            cancelFindGame.setVisible(false);
            label.setVisible(false);
    }


}
