/**
 * UseStationServer
 */
public class UseStationServer {

    public static void main(String[] args) {
        MessageManager messageManager = new MessageManager();
        new BusServer(messageManager);
        new StationServer(messageManager);
    }
    
}