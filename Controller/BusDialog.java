
//file name: Dialog78.java
//Iyar 5770 update Sivan 5778
//Levian Yehonatan
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class BusDialog extends Thread // parallel dialogs on the same socket
{

    Socket client;
    BusServer myServer;
    BufferedReader bufferSocketIn;
    PrintWriter bufferSocketOut;
    BusDialogWin myOutput;
    MessageManager messageManager;

    int busLine;

    public BusDialog(Socket clientSocket, BusServer myServer, MessageManager messageManager) {
        client = clientSocket;
        this.myServer = myServer;
        this.messageManager = messageManager;

        try {
            // Init streams to read/write text in this socket
            bufferSocketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bufferSocketOut = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);

            String line = bufferSocketIn.readLine();   //   bus serial number
            busLine = Integer.parseInt(line);
            List<Integer> stations = messageManager.busBeginTrip(busLine);
            bufferSocketOut.println(stations);

        } catch (IOException e) {
            try {
                client.close();
            } catch (IOException e2) {
            }
            System.err.println("server:Exception when opening sockets: " + e);
            return;
        }
        myOutput = new BusDialogWin("Dialog Win for: " + client.toString(), this);
        start();
    }

    public void run() {
        String line;
        boolean stop = false;
        try {
            while (true) {
                line = bufferSocketIn.readLine();   //   station
                int station = Integer.parseInt(line);
                messageManager.busArrivedAtStation(busLine, station);
                break;

                // if (line == null)
                // break;
                // if (line.equals("end"))
                // break;

                // int busLine = Integer.parseInt(line);
                // List<Integer> stations = messageManager.busBeginTrip(busLine);
                // bufferSocketOut.println(stations);

                // myOutput.printOther(line);
            }
        } catch (IOException e) {
        } finally {
            try {
                client.close();
            } catch (IOException e2) {
            }
        }

        myOutput.printMe("end of  dialog ");
        myOutput.send.setText("Close");

    }

    void exit() {
        try {
            client.close();
        } catch (IOException e2) {
        }
    }
}
