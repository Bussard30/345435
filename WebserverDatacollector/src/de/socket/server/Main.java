
package de.socket.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	private static final int PORT_INCOMING_DATA = 66743;
	private static final int PORT_BROWSER_CON = 80;

	private static File dataStoreFile = new File("data.txt");

	public static void main(String[] args) {
		startDataCollectorServer();
	}

	//////
	//////// Data Collector Server
	//////

	public static void startDataCollectorServer() {
		(new Thread() {
			public void run() {
				this.setName("data collector - Thread");
				try {
					ServerSocket serverSocket = new ServerSocket(PORT_INCOMING_DATA);
					BufferedReader reader;
					while (true) {
						Socket socket = serverSocket.accept();

						reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

						processData(reader.readLine());

						socket.close();
					}
				} catch (Exception exc) {
				}
			}
		}).start();
	}

	public static void processData(String inputLine) {

		// process

		storeDataInFile(inputLine);
	}

	public static void storeDataInFile(String newData) {
		try {
			System.out.println(newData);
			PrintWriter writer = new PrintWriter(new FileWriter(dataStoreFile, true));

			writer.println(newData + "\n");

			writer.close();
		} catch (Exception exc) {
		}
	}

	//////
	//////// Browser Server
	//////

	public static void startBrowserServer() {

		(new Thread() {
			public void run() {
				this.setName("Browser Connector Thread");

				try {
					ServerSocket serverSocket = new ServerSocket(PORT_BROWSER_CON);

					while (true) {
						Socket socket = serverSocket.accept();

						sendHTMLFile(socket);

						socket.close();
					}
				} catch (Exception exc) {

				}
			}
		}).start();

	}

	//////
	//////// Sending stuff
	//////

	public static void sendHTMLFile(Socket s) {
		try {

			// create HTML file
			File outputFile = new File("index.html");

			sendRawFile(s, outputFile);

		} catch (Exception exc) {

		}
	}

	public static void sendRawFile(Socket s, File f) {
		try {
			FileInputStream fStream = new FileInputStream(f);

			byte[] bytes = new byte[16 * 1024];

			int count;
			while ((count = fStream.read(bytes)) > 0) {
				s.getOutputStream().write(bytes, 0, count);
			}

		} catch (Exception exc) {
		}
	}
}
