package de.socket.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class DataCollectionServer {

	public static final int PORT_INCOMING_DATA = 7637;

	public DataCollectionServer() {
		this.start();
	}

	public void start() {
		System.out.println("lal");
		(new Thread() {
			public void run() {
				this.setName("data collector - Thread");
				try {
					
					ServerSocket ss = new ServerSocket(PORT_INCOMING_DATA);
					BufferedReader reader;
					System.out.println("started receiver loop for pi in wetterstation");
					while (Main.isOnline()) {
						Socket socket = ss.accept();

						reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

						processData(reader.readLine());

						socket.close();
					}

					ss.close();

				} catch (Exception exc) {
				}
			}
		}).start();
	}

	/**
	 * 
	 * @param inputLine
	 */
	public void processData(String inputLine) {

		// Reihenfolge: 'start, Luftdruck, Luftfeuchtigkeit, Lufttemperatur, Windgeschwindigkeit(km/h), Wetter, Regenzustand,end' 
		
		// macht aus 78.44;327;27.89;23; (sieben stück)		
		// ein float array
		String[] valuesStr = inputLine.split(";");
		float[] values = new float[6];

		if(valuesStr.length != values.length){
			System.out.println("could not process inputs => amount of entered values doesn't ");
		}
		
		for (int i = 0; i < valuesStr.length; i++)
			try {
				values[i] = Float.parseFloat(valuesStr[i]);
			} catch (Exception exc) {
				values[i] = -999;
			}

		// process
		storeData(convertToStorageFormat(values));
		Logger.storeLog("INFO", "stored data");
	}

	public String convertToStorageFormat(float[] pureData) {
		return "AP:" + pureData[0] + ":HUM:" + pureData[1] + ":TEMP:" + pureData[2] + ":SPEED:" + pureData[3] + ":WTR:" + pureData[4] + ":RGN:" + pureData[5];
	}

	

	public static final File dataStoreDir = new File("H://arduinoDatenWebserver/");

	private void storeData(String inputLine) {
		dataStoreDir.mkdirs();
		File dataFile = new File(dataStoreDir + "/data_" + Logger.getCurrentTime("yyyy_MM_dd") + ".txt");

		try {

			System.out.println("adding data to file: '" + dataFile.getAbsolutePath() + "'");
			// creates file IF FILE DOESNT YET EXIST
			dataFile.createNewFile();

			PrintWriter writer = new PrintWriter(new FileWriter(dataFile, true));

			writer.println("[" + Logger.getCurrentTime() + "]: '" + inputLine + "'");
			writer.flush();

			writer.close();
		} catch (Exception exc) {
		}

	}

	

}
