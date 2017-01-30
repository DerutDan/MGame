import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Main {
    volatile static Socket player1Socket = null,player2Socket = null;
    static InputStream sin1 = null,sin2 = null;
    static OutputStream sout1 = null,sout2 = null;
    volatile static boolean flag = true;
    public static void main(String[] args)
    {

        int port = 5555;
        try {



            ServerSocket ss = new ServerSocket(port);
            //Thread ch1 = checker1(), ch2 = checker2();
            //ch1.start();
            //ch2.start();
            while(player1Socket == null || player2Socket == null || player1Socket.isClosed() || player2Socket.isClosed()) {

                if(player1Socket == null || player1Socket.isClosed()) {
                    player1Socket = ss.accept();
                    System.out.println("Conneted1");
                    check2();
                }
                try{
                    sin1 = player1Socket.getInputStream();
                    sout1 = player1Socket.getOutputStream();


                } catch (IOException e)
                {
                    e.printStackTrace();

                }
                if(player2Socket == null || player2Socket.isClosed()) {
                    player2Socket = ss.accept();
                    System.out.println("Conneted2");
                    check1();
                }

                try{
                    sin2 = player2Socket.getInputStream();
                    sout2 = player2Socket.getOutputStream();

                } catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
            //ch1.interrupt();
            //ch2.interrupt();
            System.out.println("Starts");
            GameBoard game = new GameBoard(sin1, sout1, sin2, sout2);


        game.StartGame();
        } catch (IOException e)
        {
            System.out.println("Connection failure");
            e.printStackTrace();
        }
    }
    static void check1()
    {
        boolean close;
        try {
            close = false;
            if (player1Socket != null && !player1Socket.isClosed()) {
                if(sout1 != null) sout1.write(3);
                if(sin1 != null && sin1.read() == 255)
                {
                    close = true;
                }
            }
        }catch (IOException e) {
            close = true;
        }
        if(close) try {
            if(!player1Socket.isClosed())
            {
                player1Socket.close();
                System.out.println("Closed1");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void check2()
    {
        boolean close;
        close = false;
        if (player2Socket != null && !player2Socket.isClosed()) {
            try {
                if(sout2 != null)
                {
                    sout2.write(3);
                }
                if(sin2 != null && sin2.read() == 255)
                {
                    close = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                close = true;
            }
        }
        if(close) try {
            if(!player2Socket.isClosed())
            {
                player2Socket.close();
                System.out.println("Closed2");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
