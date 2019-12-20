package bgu.spl.mics.application;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;

import java.io.FileReader;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        String jsonPath = "../input.json";
       // Object obj =  new FileReader(jsonPath);
        Event<AgentsAvailableEvent> event ;
    }
}
