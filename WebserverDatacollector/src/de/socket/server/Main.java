
package de.socket.server;

public class Main {

	private static DataCollectionServer dataCollServer;
	private static BrowserSenderServer browserSenderServer;

	
	public static void main(String[] args) {
		
		Logger.newLine();
		Logger.newLine();
		Logger.newLine();

		dataCollServer = new DataCollectionServer();
		browserSenderServer = new BrowserSenderServer();
		
		//TODO daten senden können
		
//		browserSenderServer.loadDataFromFiles(1);
		
	}
}
