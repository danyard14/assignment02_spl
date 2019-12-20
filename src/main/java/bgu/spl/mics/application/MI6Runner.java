package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        Gson gson = new Gson();
        Inventory inventory = Inventory.getInstance();
        Squad squad = Squad.getInstance();

        try {
            JsonReader reader = new JsonReader(new FileReader("/Users/nadavshaked/assignment2_spl/src/main/java/bgu/spl/mics/application/input.json"));
            JsonParser parser = gson.fromJson(reader, JsonParser.class);
            int appDuration = parser.services.time;
            inventory.load(parser.inventory);
            squad.load(parser.squad);
            int NumberOfMs = parser.services.M;
            int NumberOfMP = parser.services.Moneypenny;
            Thread timeService = new Thread(new TimeService(appDuration));
            timeService.start();
            int NumberOfIntelligence = parser.services.intelligence.length;
            JsonParser.MI6Class.IntelligencesArray[] intelligencesArray = parser.services.intelligence;
            int idCounter = 1;
            for (JsonParser.MI6Class.IntelligencesArray missionsArray: intelligencesArray) { //TODO: run every intelligence
                List<MissionInfo> missionInfoList = convertFromJsonParserToMissionInfoList(missionsArray);
                Intelligence intelligence = new Intelligence(idCounter, missionInfoList);
                    //TODO Run the intelligence thread
//                Thread intelligenceThread = new Thread(intelligence);
//                intelligenceThread.start();
                idCounter++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
////        JsonObject rootObject = rootElement.getAsJsonObject();
        System.out.println("Testing to see how far we came");
////        JsonArray jsonarr = rootObject.getAsJsonArray("Inventory");
//        Squad sq = new Squad();
//        Inventory inv = new Inventory();
//        inv.load(GadgetsToLoad);
//        }
    }

    private static List<MissionInfo> convertFromJsonParserToMissionInfoList(JsonParser.MI6Class.IntelligencesArray missionsArray){
        List<MissionInfo> missionInfoList = new LinkedList<>();
        for (JsonParser.MI6Class.Missions mission :missionsArray.missions) {
            MissionInfo missionInfo = new MissionInfo();
            missionInfo.setMissionName(mission.missionName);
            List<String> serialAgentsNumbers = new LinkedList<>();
            for (String serialAgentsNumber: mission.serialAgentsNumbers) {
                serialAgentsNumbers.add(serialAgentsNumber);
            }
            missionInfo.setGadget(mission.gadget);
            missionInfo.setTimeIssued(mission.timeIssued);
            missionInfo.setTimeExpired(mission.timeExpired);
            missionInfo.setDuration(mission.duration);

            missionInfoList.add(missionInfo);
        }
        return missionInfoList;
    }
}
