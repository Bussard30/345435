package de.socket.server;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
	private static final File logFile = new File("H://arduinoDatenWebserver/log.txt");

	public static void storeLog(String lineHeader, String log) {

		try {
			// creates file IF FILE DOESNT YET EXIST
			logFile.createNewFile();

			PrintWriter writer = new PrintWriter(new FileWriter(logFile, true));

			writer.println("[" + getCurrentDate() + "][" + getCurrentTime() + "] [" + lineHeader + "]: '" + log + "'");
			writer.flush();

			writer.close();
		} catch (Exception exc) {
		}
	}
	
	public static void log(String lineHeader, String log){
		System.out.println("[" + getCurrentDate() + "][" + getCurrentTime() + "] [" + lineHeader + "]: '" + log + "'");
	}
	
	public static String getCurrentDate() {
		return getCurrentTime("yyyy:MM:dd");
	}

	public static String getCurrentTime() {
		return getCurrentTime("HH:mm:ss");
	}

	public static String getCurrentTime(String format) {
		return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
	}
}
