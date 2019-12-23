package bgu.spl.mics.application;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
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
            JsonReader reader = new JsonReader(new FileReader("/Users/nadavshaked/assignment2_spl/src/main/java/bgu/spl/mics/application/test1.json"));
            JsonParser parser = gson.fromJson(reader, JsonParser.class);
            int appDuration = parser.services.time;
            inventory.load(parser.inventory);
            squad.load(parser.squad);
            int numOfMObjects = parser.services.M;
            int numOfMoneyPennyObjects = parser.services.Moneypenny;
            JsonParser.MI6Class.IntelligencesArray[] intelligencesArray = parser.services.intelligence;
            int idCounter = 1;
            for (JsonParser.MI6Class.IntelligencesArray missionsArray: intelligencesArray) { //TODO: run every intelligence
                List<MissionInfo> missionInfoList = convertFromJsonParserToMissionInfoList(missionsArray);
                Intelligence intelligence = new Intelligence(idCounter, missionInfoList);
                //TODO Run the intelligence thread
                Thread intelligenceThread = new Thread(intelligence);
                intelligenceThread.start();
                //TODO end creation of intelligence thread
                idCounter++;
            }
            for (int i = 1; i <= numOfMObjects; i++) {
                M m = new M(i);
                Thread mThread = new Thread(m);
                mThread.start();
            }
            for (int i = 1; i <= numOfMoneyPennyObjects; i++) {
                Moneypenny moneypenny = new Moneypenny(i);
                Thread moneypennyThread = new Thread(moneypenny);
                moneypennyThread.start();
            }
            Q q = new Q(1);
            Thread qThread = new Thread(q);
            qThread.start();

            Thread timeService = new Thread(new TimeService(appDuration));
            timeService.start();
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
            missionInfo.setSerialAgentsNumbers(serialAgentsNumbers);
            missionInfo.setGadget(mission.gadget);
            missionInfo.setTimeIssued(mission.timeIssued);
            missionInfo.setTimeExpired(mission.timeExpired);
            missionInfo.setDuration(mission.duration);

            missionInfoList.add(missionInfo);
        }
        return missionInfoList;
    }
}
