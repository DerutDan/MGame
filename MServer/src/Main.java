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

            while(player1Socket.isClosed() && player2Socket.isClosed()) {


                if(!player1Socket.isClosed()) {
                    player1Socket = ss.accept();
                    System.out.println("Conneted1");
                }
                if(!player2Socket.isClosed()) {
                    player2Socket = ss.accept();
                    System.out.println("Conneted2");
                }
            }





        GameBoard game = new GameBoard(player1Socket,player2Socket);

        game.StartGame();
        } catch (IOException e)
        {
            System.out.println("Connection failure");
            e.printStackTrace();
        }

    }



}
