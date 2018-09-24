
package de.socket.server;

import java.io.File;

public class Main {

	/**
	 * Port auf dem das Programm Daten vom Arduino enthält
	 */
	public static final int PORT_INCOMING_DATA = 7637;

	/**
	 * Pfad des Ordners der die gespeicherten Daten enthält (=> e.g. data_XX_XX_XX.txt)
	 * Es dürfen keine anderen Dateien als die Daten-Dateien in dem Verzeichnis sein!
	 */
	public static final File DATA_STORE_DIR = new File("arduinoDatenWebserver/");
//	public static final File dataStoreDir = new File("H:/MintExWebserver/data/");
	
	
	
	
	/**
	 * Port auf dem die HTTPS GET Requests reinkommen
	 */
	public static final int PORT_BROWSER_CON = 2001;
	
	/**
	 * Pfad des Ordners der die Website Dateien enthält (=> index.html, javascript.js, etc.)
	 */
//	public static final String fileDir = "H:/MintExWebserver/";
	public static final String FILE_DIR = "res/";
	
	
	private static DataCollectionServer dataCollServer;
	private static BrowserSenderServer browserSenderServer;

	
	public static void main(String[] args) {
		
		Logger.newLine();
		Logger.newLine();
		Logger.newLine();

		dataCollServer = new DataCollectionServer();
		browserSenderServer = new BrowserSenderServer();
		
	}
}
