package de.socket.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class BrowserSenderServer {

	public static final int PORT_BROWSER_CON = 80;

	private String fileDir = "files/";
	
//	public static final String DUMMY_HTML_REPLACE_MARKER_TEMP = "ARDUINO_TEMP";
//	public static final String DUMMY_HTML_REPLACE_MARKER_HUMID = "ARDUINO_HUMID";
//	public static final String DUMMY_HTML_REPLACE_MARKER_AIRQUAL = "ARDUINO_AIRQUAL";
//	public static final String DUMMY_HTML_REPLACE_MARKER_WINDSPD = "ARDUINO_WINDSPD";

//	private File fileIndex;
//	private File fileCSS;
//	private File fileJavaScript;
//	
//	private File loadFile(String fileName){
//		return new File(fileDir + fileName);
//	}
//	
//	private void loadFileToMemory(){
//		fileIndex = loadFile("index.html");
//		fileCSS = loadFile("css.css");
//		fileJavaScript = loadFile("javascript.js");
//	}
	
	public BrowserSenderServer() {		
//		loadFileToMemory();
		
		start();
	}

	public void start() {
		(new Thread() {
			public void run() {
				this.setName("Browser Sender Thread");

				ServerSocket serverSocket;
				try {
					serverSocket = new ServerSocket(PORT_BROWSER_CON);
					System.out.println("browser sender server started waiting for connection on port " + PORT_BROWSER_CON);
					
					
					BufferedReader reader;
					Socket connection;
					while (true) {
						connection = serverSocket.accept();
						Logger.log("INFO", "Browser has connected");
						
						//init reader
						reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						
						
						//first line = get request header						
						String line;
						
						while(!(line = reader.readLine()).equals("")){
							Logger.log("REC", "'" + line + "'");
							processReceivedLine(line, connection.getOutputStream());							
						}
						
						Logger.log("INFO","Browser send a empty line, therefore the end of the http request was reached");						
//						sendHTMLFile(socket);					
						
						
						connection.close();
					}
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		}).start();

	}	
	
	private void processReceivedLine(String line, OutputStream out){
		
		if(!line.startsWith("GET ") || !(line.endsWith("HTTP/1.1")) ){
//			System.out.println("received line is not a GET request!");
		}else{
			
			String requestedPath = line.split(" ")[1];
			
			if(requestedPath.contains("..")){
				System.out.println("accesDenied, auﬂerdem sind '/'es nicht zugelassen");
			}else{
				
				if(requestedPath.equals("/"))
//					requestedPath = "/index.html";
					requestedPath = "F://MintX/Projekt 17-18/page.html";
				 
//				requestedPath =  fileDir + requestedPath.substring(1);
				
				try{
					System.out.println("sending file");
					sendFile(new FileInputStream(new File(requestedPath)), out);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
	}

	//////
	//////// Sending stuff
	//////

	private void sendFile(InputStream in, OutputStream out){
		
		PrintWriter writer = new PrintWriter(out);
		
		writer.print("HTTP/1.1 200 OK\r\n");
//				Date: Mon, 11 Mar 2013 11:17:09 GMT
//				Server: Apache
//				X-Powered-By: PHP/5.3.8
//				Vary: Accept-Encoding
//				Content-Encoding: gzip
//				Content-Length: 832
//				Keep-Alive: timeout=1, max=100
//				Connection: Keep-Alive
		writer.print("Content-Type: text/htm\r\n\r\n");
		try{
			byte[] buf = new byte[1000];
			int bytes; 
			while((bytes = in.read(buf)) != -1){
				out.write(buf, 0, bytes);
//				System.out.write(buf,  0,  bytes);
//				System.out.println("writing bytes to browser one byte: " + buf[0]);
			}
			System.out.println("finsished sending");
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public float[][] loadDataFromFiles(int amount){
		float[][] dataVals = new float[amount][7]; 
		
		
		ArrayList<String> allLines = new ArrayList<String>();
		//get last File
		
		String[] filesListed = DataCollectionServer.dataStoreDir.list();
		Arrays.sort(filesListed);
		
		for(String s : filesListed)
			System.out.println("file sorted: '" + s + "'");
		
		return dataVals;
	}
	
	private int countBytes(String[] array){
		int size = 0;
		for(String str: array)
			size += str.getBytes().length;
		return size;
	}
}
