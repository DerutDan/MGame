import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args)
    {
        int port = 5555;
        try {
            ServerSocket ss = new ServerSocket(port);
            Socket player1Socket = ss.accept();
            System.out.println("Conneted1");
            Socket player2Socket = ss.accept();
            System.out.println("Conneted2");
            int lala = 1;


            while(!player1Socket.isClosed() && !player2Socket.isClosed()) {


                if(!player1Socket.isClosed()) {
                    player1Socket = ss.accept();
                    System.out.println("Conneted1");
                }
                if(!player2Socket.isClosed()) {
                    player2Socket = ss.accept();
                    System.out.println("Conneted2");
                }
            }
            InputStream sin1 = player1Socket.getInputStream();
            OutputStream sout1 = player1Socket.getOutputStream();
            DataInputStream in1 = new DataInputStream(sin1);
            ObjectOutputStream out1 = new ObjectOutputStream(sout1);
            InputStream sin2 = player2Socket.getInputStream();
            OutputStream sout2 = player2Socket.getOutputStream();
            DataInputStream in2 = new DataInputStream(sin2);
            ObjectOutputStream out2 = new ObjectOutputStream(sout2);



        GameBoard game = new GameBoard(out1,out2);

        while(game.gameOn && player1Socket.isConnected() && player2Socket.isConnected())
        {
                game.Phase01();
                game.Phase11(in1.readInt());
                game.Phase21(in1.readInt());
                game.Phase31();
                game.Phase02();
                game.Phase12(in2.readInt());
                game.Phase22(in2.readInt());
                game.Phase32();

        }
        } catch (IOException e)
        {
            System.out.println("Connection failure");
            e.printStackTrace();
        }

    }



}
