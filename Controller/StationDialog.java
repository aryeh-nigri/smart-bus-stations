import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class StationDialog extends Thread // parallel dialogs on the same socket
{

    Socket client;
    StationServer myServer;
    BufferedReader bufferSocketIn;
    PrintWriter bufferSocketOut;
    StationDialogWin myOutput;
    MessageManager messageManager;

    Event64 evStation;

    public StationDialog(Socket clientSocket, StationServer myServer, MessageManager messageManager) {
        client = clientSocket;
        this.myServer = myServer;
        this.messageManager = messageManager;

        try {
            // Init streams to read/write text in this socket
            bufferSocketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bufferSocketOut = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);

            String line = bufferSocketIn.readLine(); // the station serial number
            int station = Integer.parseInt(line);
            evStation = new Event64();
            messageManager.addStation(station, evStation);
            
        } catch (Exception e) {
            try {
                client.close();
            } catch (IOException e2) {
            }
            System.err.println("server:Exception when opening sockets: " + e);
            return;
        }
        myOutput = new StationDialogWin("Dialog Win for: " + client.toString(), this);
        start();
    }

    public void run() {
        String line;
        boolean stop = false;
        try {
            while (true) {
                if(evStation.arrivedEvent()){
                    String message = (String) evStation.waitEvent();
                    bufferSocketOut.println(message);
                    break;
                }
                else yield();
                // line = bufferSocketIn.readLine();   //   the station serial number
                // if (line == null)
                //     break;
                // if (line.equals("end"))
                //     break;

                // int station = Integer.parseInt(line);
                // Event64 event = new Event64();
                // messageManager.addStation(station, event);
                // String message = event.waitEvent();
                // bufferSocketOut.println(message);

                // myOutput.printOther(line);
            }
        } catch (Exception e) {
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
