package bgu.spl.mics.application;


import bgu.spl.mics.application.passiveObjects.Agent;

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
    }
}