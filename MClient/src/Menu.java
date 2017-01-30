import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class Menu extends JFrame {
    private int serverPort = 5555,height,width;
    private String address = "192.168.1.181";
    Socket socket,redun;
    private JButton findGame,help,cancelFindGame;
    private JLabel waitingLabel;
    private Thread connectThread;
    private InetSocketAddress ipAddress;
    Thread waiting;
    OutputStream sout;
    InputStream sin;
    volatile boolean isCanceled = false;
    Menu() {
        height = 600;
        width = 600;
        setFocusable(true);
        setBounds(0, 0, width, height);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);
        findGame = new JButton("Find Game");
        help = new JButton("Help");
        cancelFindGame = new JButton("Cancel");
        waitingLabel = new JLabel("Waiting for opponent...",JLabel.CENTER);
        cancelFindGame.setVisible(false);
        waitingLabel.setVisible(false);
        waitingLabel.setBounds(width/2-100,10,200,50);
        findGame.setBounds(width/2-100,10,200,50);
        help.setBounds(width/2-100,80,200,50);
        cancelFindGame.setBounds(width/2-100,80,200,50);
        ipAddress = new InetSocketAddress(address,serverPort);


        panel.add(findGame);
        panel.add(help);
        panel.add(cancelFindGame);
        panel.add(waitingLabel);

        findGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(connectThread == null || !connectThread.isAlive()) {
                    isCanceled = false;
                    help.setVisible(false);
                    findGame.setVisible(false);
                    cancelFindGame.setVisible(true);
                    waitingLabel.setVisible(true);
                    connectThread = connectingThread();
                    connectThread.start();
                }
            }
        });

        cancelFindGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                waiting.interrupt();
                help.setVisible(true);
                findGame.setVisible(true);
                cancelFindGame.setVisible(false);
                waitingLabel.setVisible(false);
                isCanceled = true;

                try {
                    sout.write(-1);
                    socket.close();
                } catch(SocketException err1){
                    System.out.println("Disconnected1");
                    err1.printStackTrace();
                }
                catch (IOException err2) {
                    err2.printStackTrace();
                }
            }
        });

        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toBoard(1);
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
                    socket.connect(ipAddress);
                }
                catch (IOException err) {
                    if(err.getMessage().equals("Connection refused"))
                    {
                        backToMenu();
                        System.out.println("Server is not working at the moment");
                    }
                    else err.printStackTrace();
                }
                    try {
                        if(socket != null && !isCanceled && socket.isConnected() && !socket.isClosed()) {
                            sout = socket.getOutputStream();
                            sin = socket.getInputStream();
                            waiting = waitingForPlayer();
                            waiting.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        });
    }

    private void backToMenu()
    {
            help.setVisible(true);
            findGame.setVisible(true);
            cancelFindGame.setVisible(false);
            waitingLabel.setVisible(false);
    }

    private Thread waitingForPlayer()
    {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int player = 0;


                        while((player = sin.read()) == 3) {
                            System.out.println(player);
                            if(player == 3) sout.write(3);
                        }
                        if(player == 1 || player == 2) toBoard(player);
                } catch (IOException e) {
                    System.out.println("Was there");
                    if(socket.isClosed())
                    {
                        System.out.println("Disconnected");
                        backToMenu();
                    }
                    else e.printStackTrace();
                }
            }
        });

    }
    private void toBoard(int player)
    {
        Main.board = new Board(sout, sin);
        setVisible(false);
        backToMenu();
        Main.board.StartGame(player);
    }
}
