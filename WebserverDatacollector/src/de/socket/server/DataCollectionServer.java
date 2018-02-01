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

	private static final String dataStoreFilePath = "data.txt";
	private File dataStoreFile;

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

	public void processData(String inputLine) {

		// process

		storeDataInFile(inputLine);
	}

	public void storeDataInFile(String newData) {
		try {
			if (dataStoreFile == null)
				dataStoreFile = new File(dataStoreFilePath);

			System.out.println("received data: " + newData);
			PrintWriter writer = new PrintWriter(new FileWriter(dataStoreFile, true));

			writer.println(newData);

			writer.flush();
			writer.close();

		} catch (Exception exc) {
		}
	}
}
