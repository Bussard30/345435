package de.socket.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class BrowserSenderServer {

	public static final int PORT_BROWSER_CON = 1234;

//	private String fileDir = "files/";
	private String fileDir = "H:/Programmieren/Javascript/GET request testing";
	
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
					OutputStream out;
					while (true) {
						connection = serverSocket.accept();
						out = connection.getOutputStream();
						Logger.newLine();
						Logger.log("INFO", "Browser has connected: '" + connection.getInetAddress() + ":" + connection.getPort() + "'");
						
						//init reader
						reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						
						
						//first line = get request header						
						String line;
						
						while(!(line = reader.readLine()).equals("")){
//							Logger.log("REC", "'" + line + "'");
							processReceivedLine(line, out);							
						}
						
//						Logger.log("INFO","Browser send a empty line, therefore the end of the http request was reached");						
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
			
			String requestedFile = line.split(" ")[1];
			
			if(requestedFile.contains("..")){
				System.out.println("accesDenied, auﬂerdem sind '/'es nicht zugelassen");				
			}else{
				
				if(requestedFile.equals("/"))
					requestedFile = "/index.html";
//					requestedPath = "/index.html";
//					requestedPath = "F://MintX/Projekt 17-18/page.html";
				 
//				requestedPath =  fileDir + requestedPath.substring(1);
				
				File f = new File(fileDir + requestedFile);
				
				if(!f.exists()) {
					Logger.log("404", "requested file was not found");
					sendHTTPResponse404(out);					
//					sendFileNotFoundHTTPHeader(out);					
					return;
				}
				
				try{
					Logger.log("SENDING", "sending file '" + requestedFile + "'");
					sendHTTPResponse200(out, guessContentType(requestedFile));
					sendFile(new FileInputStream(f), out);
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
		
		try{
			byte[] buf = new byte[1000];
			int bytes; 
			while((bytes = in.read(buf)) != -1){
				out.write(buf, 0, bytes);
//				System.out.write(buf,  0,  bytes);
//				System.out.println("writing bytes to browser one byte: " + buf[0]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public void sendHTTPResponse404(OutputStream out) {
		PrintStream pout = new PrintStream(out);
		pout.print("HTTP/1.1 404\r\n"
				+ "Date: " + new Date() + "\r\n"
				+ "Server: FileServer 1.0\r\n\r\n");
	}
	
	public void sendHTTPResponse200(OutputStream out, String contentType) {
		PrintStream pout = new PrintStream(out);
		
		pout.print("HTTP/1.1 200 OK\r\n"
				+ "Content-Type: " + contentType + "\r\n"
				+ "Date: " + new Date() + "\r\n"
				+ "Server: FileServer 1.0\r\n\r\n");
	}
	
//	public void sendHTTPProtocolHeader(OutputStream out, String contentType) {
//		PrintStream pout = new PrintStream(out);
//		
//		pout.print("HTTP/1.1 200 OK\r\n"
//				+ "Content-Type: " + contentType + "\r\n"
//				+ "Date: " + new Date() + "\r\n"
//				+ "Server: FileServer 1.0\r\n\r\n");
//		
////		System.out.print("HTTP/1.1 200 OK\r\n"
////				+ "Content-Type: " + contentType + "\r\n"
////						+ "Date: " + new Date() + "\r\n"
////								+ "Server: FileServer 1.0\r\n\r\n");
//	}
//	
//	public void sendFileNotFoundHTTPHeader(OutputStream out) {
//		PrintStream pout = new PrintStream(out);
//		pout.print("HTTP/1.1 404\r\n"
//				+ "Date: " + new Date() + "\r\n"
//				+ "Server: FileServer 1.0\r\n\r\n");
//		
//	}
	
	private String guessContentType(String path) {
		if (path.endsWith(".html") || path.endsWith(".htm")) 
            return "text/html";
        else if (path.endsWith(".js"))
        	return "application/javascript";        
        else    
            return "text/plain";
	}
	
//	public float[][] loadDataFromFiles(int amount){
//		float[][] dataVals = new float[amount][7]; 
//		
//		
//		ArrayList<String> allLines = new ArrayList<String>();
//		//get last File
//		
//		String[] filesListed = DataCollectionServer.dataStoreDir.list();
//		Arrays.sort(filesListed);
//		
//		for(String s : filesListed)
//			System.out.println("file sorted: '" + s + "'");
//		
//		return dataVals;
//	}
//	
//	private int countBytes(String[] array){
//		int size = 0;
//		for(String str: array)
//			size += str.getBytes().length;
//		return size;
//	}
}
