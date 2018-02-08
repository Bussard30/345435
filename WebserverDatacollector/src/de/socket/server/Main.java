
package de.socket.server;

public class Main {

	private static DataCollectionServer dataCollServer;
	private static BrowserSenderServer browserSenderServer;

	
	public static void main(String[] args) {

		dataCollServer = new DataCollectionServer();
		browserSenderServer = new BrowserSenderServer();
		
		//TODO 
		
		browserSenderServer.loadDataFromFiles(1);
		
	}
	
	public static boolean isOnline()
	{
		return true;
	}
}
