package de.socket.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class BrowserSenderServer {

	public static final int PORT_BROWSER_CON = 80;

	public static final String DUMMY_HTML_REPLACE_MARKER_TEMP = "ARDUINO_TEMP";
	public static final String DUMMY_HTML_REPLACE_MARKER_HUMID = "ARDUINO_HUMID";
	public static final String DUMMY_HTML_REPLACE_MARKER_AIRQUAL = "ARDUINO_AIRQUAL";
	public static final String DUMMY_HTML_REPLACE_MARKER_WINDSPD = "ARDUINO_WINDSPD";

	public BrowserSenderServer() {
		start();
	}

	public void start() {

		(new Thread() {
			public void run() {
				this.setName("Browser Sender Thread");

				ServerSocket ss;
				try {
					ss = new ServerSocket(PORT_BROWSER_CON);

					while (true) {
						Socket socket = ss.accept();
						System.out.println("Browser has connected");
						
						BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String line;
						while((line = reader.readLine()) != null)
							System.out.println("'" + line + "'");
						
						sendHTMLFile(socket);

						socket.close();
						socket = null;
					}
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		}).start();

	}

	//////
	//////// Sending stuff
	//////

	
	
	public void sendHTMLFile(Socket s) {
		try {

			// read dummy html file & and put it into a String array
			ArrayList<String> lines = new ArrayList<String>();
			BufferedReader in = new BufferedReader(new FileReader(new File("dummyHTML.html")));
			String ln;
			while ((ln = in.readLine()) != null)
				lines.add(ln);
			in.close();

			// manipulate lines
			for (int i = 0; i < lines.size(); i++) {
				ln = lines.get(i);

				// TODO gesammelte Werte implementieren

				ln = ln.replace(DUMMY_HTML_REPLACE_MARKER_HUMID, "Das hier sind verschiedene Luftfeuchtigkeiten");
				ln = ln.replace(DUMMY_HTML_REPLACE_MARKER_TEMP, "Das hier sind verschiedene Temperaturen");

				lines.set(i, ln);
			}

			// send them
			PrintWriter out = new PrintWriter(s.getOutputStream());
			out.println("HTTP/1.1 200 OK");
			out.println("Server: Apache/1.3.29 PHP/4.3.4");
			out.println("Content-Length: " + countBytes(lines.toArray(new String[0])));
			out.println("Content-Language: de");
			out.println("Connection: close");
			out.println("Content-Type: text/html");
			out.println("\r\n");
			for (String line : lines) {
				System.out.println("Sending: " + line);
				out.println(line);
			}
			out.flush();
			out.close();

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	private int countBytes(String[] array){
		int size = 0;
		for(String str: array)
			size += str.getBytes().length;
		return size;
	}
}
