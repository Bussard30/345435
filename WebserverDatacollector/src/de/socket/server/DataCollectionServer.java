package de.socket.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DataCollectionServer {

	/**
	 * Port auf dem das Programm Daten vom Arduino enthält
	 */
	public static final int PORT_INCOMING_DATA = 7637;

	/**
	 * Pfad des Ordners der die gespeicherten Daten enthält (=> e.g. data_XX_XX_XX.txt)
	 * Es dürfen keine anderen Dateien als die Daten-Dateien in dem Verzeichnis sein!
	 */
	public static final File dataStoreDir = new File("H:/MintExWebserver/data/");
	
	public DataCollectionServer() {
		this.start();
	}

	public void start() {
		(new Thread() {
			public void run() {
				this.setName("data collector - Thread");
				
				while(true) {
				
					try {
						
						ServerSocket serverSocket = new ServerSocket(PORT_INCOMING_DATA);
						BufferedReader reader;
						Logger.log("DCS-BOOT", "waiting for incoming data from Arduino");
						while (true) {
							Socket socket = serverSocket.accept();
	
							reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	
							processData(reader.readLine());
	
							socket.close();
						}
	
					} catch (Exception exc) {
					}
					
				}
			}
		}).start();
	}

	/**
	 * 
	 * @param inputLine
	 */
	public void processData(String inputLine) {
		
		Logger.log("INFO", "line received: '" + inputLine + "'");
//		// Reihenfolge: 'start, Luftdruck, Luftfeuchtigkeit, Lufttemperatur, Windgeschwindigkeit(km/h), Wetter, Regenzustand,end' 

//		Logger.log("INFO (internal)", "before: '" + inputLine + "'");
		//inputLine = inputLine.replace("; ", " ");
		inputLine = inputLine.replace(";", ":");
		inputLine = inputLine.substring(0, inputLine.length()-1);
//		Logger.log("INFO (internal)", "after: '" + inputLine + "'");
		
		storeData(inputLine);
	}

	private void storeData(String inputLine) {
		dataStoreDir.mkdirs();
		File dataFile = new File(dataStoreDir + "/data_" + Logger.getCurrentTime("yyyy_MM_dd") + ".txt");

		try {

			Logger.log("INFO", "adding data to file: '" + dataFile.getAbsolutePath() + "'");
			// creates file IF FILE DOESNT YET EXIST
			dataFile.createNewFile();

			PrintWriter writer = new PrintWriter(new FileWriter(dataFile, true));

			writer.println("[" + Logger.getCurrentTime() + "]:'" + inputLine + "'");
			writer.flush();

			writer.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}

	}

	

}
