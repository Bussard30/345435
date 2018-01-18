
package de.socket.server;

public class Main {


	public static boolean online = true;

	private static DataCollectionServer dataCollServer;
	private static BrowserSenderServer browserSenderServer;

	
	public static void main(String[] args) {

		dataCollServer = new DataCollectionServer();
		browserSenderServer = new BrowserSenderServer();
	
}
