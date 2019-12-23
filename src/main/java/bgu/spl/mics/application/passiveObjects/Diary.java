package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.JsonParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sun.awt.image.ImageWatched;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	private List<Report> reports = new LinkedList<>();
	private int total;

	private static class DiaryHolder {
		private static Diary instance = new Diary();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
//		DiaryHolder.instance.reports = new LinkedList<>();
		return DiaryHolder.instance;
	}

	public List<Report> getReports() {
		synchronized (this) {
			return reports;
		}
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		synchronized (this) {//TODO: How to do atomic
			reports.add(reportToAdd);
			total++;
		}
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		try (FileWriter file = new FileWriter("/Users/nadavshaked/assignment2_spl/src/main/java/bgu/spl/mics/application/output.json")) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(this);
			file.write(json);
			System.out.println("Successfully Copied JSON Object to File...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		synchronized (this) {
			return total;
		}
	}

	/**
	 * Increments the total number of received missions by 1
	 */
	public void incrementTotal(){
		synchronized (this) {
			total++;
		}
	}
}
