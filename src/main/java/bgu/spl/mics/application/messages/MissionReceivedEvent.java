package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEvent extends Event<Integer>{
   private MissionInfo missionInfo;

   public MissionInfo getMissionInfo() {
      return missionInfo;
   }

   public void setMissionInfo(MissionInfo missionInfo) {
      this.missionInfo = missionInfo;
   }
}
