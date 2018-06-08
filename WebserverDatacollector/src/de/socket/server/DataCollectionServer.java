package de.socket.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DataCollectionServer {

	public static final int PORT_INCOMING_DATA = 7637;

//	public static final File dataStoreDir = new File("arduinoDatenWebserver/");
	public static final File dataStoreDir = new File("H:/MintExWebserver/data/");
	
	public DataCollectionServer() {
		this.start();
	}

	public void start() {
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

//		// Reihenfolge: 'start, Luftdruck, Luftfeuchtigkeit, Lufttemperatur, Windgeschwindigkeit(km/h), Wetter, Regenzustand,end' 
//		
//		// macht aus 78.44;327;27.89;23; (sieben stück)		
//		// ein float array
//		String[] valuesStr = inputLine.split(";");
//		float[] values = new float[6];
//
//		if(valuesStr.length != values.length){
//			System.out.println("could not process inputs => amount of entered values doesn't equal " + values.length);
//		}
//		
//		boolean successfull = true;
//		for (int i = 0; i < valuesStr.length; i++) {
//			try {
//				values[i] = Float.parseFloat(valuesStr[i]);
//			} catch (Exception exc) {
//				successfull = false;
//				break;
//			}
//		}
//		
//		// process
//		if(successfull) {
//			storeData(convertToStorageFormat(values));
		
		System.out.println("before: '" + inputLine + "'");
		//inputLine = inputLine.replace("; ", " ");
		inputLine = inputLine.replace(";", ":");
		inputLine = inputLine.substring(0, inputLine.length()-1);
		System.out.println("after: '" + inputLine + "'");
		
		storeData(inputLine);
//			Logger.storeLog("INFO", "stored data");
//		}else {
//			Logger.storeLog("ERROR", "could not parse values from arduino to float");
//		}
	}

//	public String convertToStorageFormat(float[] pureData) {
//		return pureData[0] + ":" + pureData[1] + ":" + pureData[2] + ":" + pureData[3] + ":" + pureData[4] + ":" + pureData[5];
//	}

	private void storeData(String inputLine) {
		dataStoreDir.mkdirs();
		File dataFile = new File(dataStoreDir + "/data_" + Logger.getCurrentTime("yyyy_MM_dd") + ".txt");

		try {

			System.out.println("adding data to file: '" + dataFile.getAbsolutePath() + "'");
			// creates file IF FILE DOESNT YET EXIST
			dataFile.createNewFile();

			PrintWriter writer = new PrintWriter(new FileWriter(dataFile, true));

			writer.println("[" + Logger.getCurrentTime() + "]:'" + inputLine + "'");
			writer.flush();

			writer.close();
		} catch (Exception exc) {
		}

	}

	

}
