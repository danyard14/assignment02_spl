package bgu.spl.mics.application;


import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Diary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonParser {
    public String[] inventory;
    public MI6Class services;
    public Agent[] squad;


    public class MI6Class{
        int M;
        int Moneypenny;
        IntelligencesArray[] intelligence;
        int time;

        public class IntelligencesArray {
            Missions[] missions;
        }

        public class Missions {
            public String[] serialAgentsNumbers;
            public int duration;
            public String gadget;
            public String missionName;
            public int timeExpired;
            public int timeIssued;
        }

        public void printToFile(Diary diary) {
            try (FileWriter file = new FileWriter("/Users/nadavshaked/assignment2_spl/src/main/java/bgu/spl/mics/output.json")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(diary);
                file.write(json);
                System.out.println("Successfully Copied JSON Object to File...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
/*
        public void printToFile(Object toPrint) {
            if(!fileName.Contains(".json"))
                logM.log.severe("file name isn't from json type");
            else {
                File file = new File(fileName);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                try{
                    logM.log.info("File " + fileName + " Created");
                    gson.toJson(toPrint, fw);
                } catch (IOException e) {
                    logM.log.severe("Print to file didn't work well");
                }
            }
        }
 */
    }
}