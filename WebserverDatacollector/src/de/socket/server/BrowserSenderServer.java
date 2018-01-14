package de.socket.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class BrowserSenderServer {

	public static final int PORT_BROWSER_CON = 80;

	public static final String DUMMY_HTML_REPLACE_MARKER_TEMP = "REPLACE_ME_TEMP";
	public static final String DUMMY_HTML_REPLACE_MARKER_HUMID = "REPLACE_ME_HUMID";

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

					while (Main.online) {
						Socket socket = ss.accept();
						System.out.println("Browser has connected");

						sendHTMLFile(socket);

						socket.close();
					}

					ss.close();
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
			out.println("Content-Type: text/html");
			out.println("\r\n");
			for (String line : lines) {
				out.println(line);
			}
			out.flush();
			out.close();

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	// public static void sendRawFile(Socket s, File f) {
	// try {
	// FileInputStream fStream = new FileInputStream(f);
	//
	// byte[] bytes = new byte[16 * 1024];
	//
	// int count;
	// while ((count = fStream.read(bytes)) > 0) {
	// s.getOutputStream().write(bytes, 0, count);
	// }
	//
	// } catch (Exception exc) {
	// }
	// }
}
