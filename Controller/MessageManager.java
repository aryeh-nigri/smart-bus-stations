import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * MessageManager
 */
public class MessageManager {

    // Map<Integer, Set<Integer, Event64>> stationsByLine;
    // List<List<Integer>> stationsByLine;   //   {{0, 5, 7}, {1, 4, 8}, {2, 6, 9}}
    Map<Integer, List<Integer>> stationsByLine;
    Map<Integer, Event64> stationsMap;
    final int distance = 5;

    public MessageManager() {
        // stationsByLine = new Map<>();
        // stationsByLine = new ArrayList<>();
        stationsByLine = new HashMap<>();
        stationsMap = new HashMap<>();
    }

    public void addStation(Integer station, Event64 event) {
        stationsMap.put(station, event);    
    }

    public List<Integer> busBeginTrip(int busLine) {
        // return stationsByLine.get(busLine);
        if(stationsByLine.containsKey(busLine)){
            return stationsByLine.get(busLine);
        }
        else{
            return null;
        }
        
    }

    public void busArrivedAtStation(int busLine, Integer station) {
        // String message = "A certain bus, a certain line, arrives at a certain station in an age and ages and a branch of Idan.";

        List<Integer> stations = stationsByLine.get(busLine);
        if(stations != null){   //   List<Integer>
            for (int stat : stations) {
                if(stat == station){
                    String message = "Line " + busLine + "\tStation " + stat + "\tTime " + distance;
                    stationsMap.get(stat).sendEvent(message);
                }                
            }
        }
    }

}