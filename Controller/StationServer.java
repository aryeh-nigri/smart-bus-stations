import java.io.*;
import java.net.*;

class StationServer extends Thread // the parallel server
{

    int DEFAULT_PORT = 51000;
    ServerSocket listenSocket;
    Socket clientSockets;

    MessageManager messageManager;

    public StationServer(MessageManager messageManager) // constructor of a TCP server
    {
        this.messageManager = messageManager;

        try {
            listenSocket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e) // error
        {
            System.out.println("Problem creating the server-socket");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Server starts on port " + DEFAULT_PORT);
        start();
    }

    public void run() {
        try {
            while (true) {
                clientSockets = listenSocket.accept();
                new StationDialog(clientSockets, this, messageManager);
            }

        } catch (IOException e) {
            System.out.println("Problem listening server-socket");
            System.exit(1);
        }

        System.out.println("end of server");
    }
}
