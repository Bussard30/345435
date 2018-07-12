package de.socket.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class BrowserSenderServer {

	/**
	 * Port auf dem die HTTPS GET Requests reinkommen
	 */
	public static final int PORT_BROWSER_CON = 2001;


	/**
	 * Pfad des Ordners der die Website Dateien enthält (=> index.html, javascript.js, etc.)
	 */
	private String fileDir = "H:/MintExWebserver/";

	public BrowserSenderServer() {
		start();
	}

	public void start() {
		
		(new Thread() {
			public void run() {
				this.setName("Browser Sender Thread");

				ServerSocket serverSocket;
				try {
					serverSocket = new ServerSocket(PORT_BROWSER_CON);
					Logger.log("BSS-BOOT", "started Browser Sender Server (BSS). waiting for incoming Connection on port " + PORT_BROWSER_CON);
					
					
					BufferedReader reader;
					Socket connection;
					OutputStream out;
					while (true) {
						try
						{
							connection = serverSocket.accept();
							out = connection.getOutputStream();
							reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
							
							Logger.newLine();
							Logger.log("BSS-INFO", "Browser has connected: '" + connection.getInetAddress() + ":" + connection.getPort() + "'");
							
	
							//process all incoming lines
							String line;
							while((line = reader.readLine()) != null && !line.equals("")){
								processReceivedLine(line, out);							
							}
	
							//close connection
							connection.close();
						}
						catch(Throwable t)
						{
							Logger.log("ERROR", t.getMessage());
							t.printStackTrace();
						}
					}
				} catch (Exception exc) {
					Logger.log("ERROR", exc.getMessage());
					exc.printStackTrace();
				}
			}
		}).start();

	}	
	
	private void processReceivedLine(String line, OutputStream out){
		
		if(!line.startsWith("GET ") || !(line.endsWith("HTTP/1.1")) ){
//			System.out.println("received line is not a GET request!");
		}else{
			
			Logger.log("BSS-INFO", "received GET-Request: '" + line + "'");
			
			String requestedFile = line.split(" ")[1];
			
			if(!(
					requestedFile.equals("/") 
					|| requestedFile.equals("/index.html") 
					|| requestedFile.equals("/dataFile.txt") 
					|| requestedFile.equals("/javascript.js") 
					|| requestedFile.equals("/style.css")
				)){
				Logger.log("WARNING", "a file other than '/' '/index.html' '/dataFile.txt' was requested. REQUEST REJECTED");
				return;
			}
			
			if(requestedFile.contains("..")){ 
				Logger.log("BSS-WARNING", "accesDenied, '..' not permitted");				
			}else{
				
				if(requestedFile.equals("/"))
					requestedFile = "/index.html";
//					requestedPath = "/index.html";
//					requestedPath = "F://MintX/Projekt 17-18/page.html";
				 
				if(requestedFile.equals("/dataFile.txt")) 
					requestedFile = createDataFileIfDoesntExist().getName();
				
					
				
//				requestedPath =  fileDir + requestedPath.substring(1);
				
				File f = new File(fileDir + requestedFile);
				
				if(!f.exists()) {
					Logger.log("404", "requested file was not found: '" + requestedFile + "'");
					sendHTTPResponse404(out);					
//					sendFileNotFoundHTTPHeader(out);					
					return;
				}
				
				try{
					Logger.log("SENDING", "sending file '" + requestedFile + "'");
					sendHTTPResponse200(out, guessContentType(requestedFile));
					sendFile(new FileInputStream(f), out);
				}catch(Exception e){
					Logger.log("ERROR on sending file", e.getMessage());
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
	
	private String guessContentType(String path) {
		if (path.endsWith(".html") || path.endsWith(".htm"))
            return "text/html";
        else if (path.endsWith(".js"))
        	return "application/javascript";        
        else if (path.endsWith(".css"))
        	return "text/css";
        else
            return "text/plain";
	}
	
	private File dataExportFile = new File(fileDir + "dataFile.txt");
	private File createDataFileIfDoesntExist() {
		//TODO only create a new file, if data was updated!
		try {
			Logger.log("INFO", "created new data export file");
			//create new data export file, if it doesnt yet exist
			dataExportFile.createNewFile();
			
			PrintWriter writer = new PrintWriter(new FileWriter(dataExportFile, false));
			
			
			//get last data sets
			String dataSets = getLastDataSets(13);
			
			//write them into the export file
//			for(String dataSet : dataSets)
			writer.println(dataSets);
			System.out.println("Datensets :'" + dataSets + "'");
			
			
			writer.close();
			
			return dataExportFile;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;	
	}
	
	private String getLastDataSets(int amount) {
		String dataSets = "";
		
		File[] files = DataCollectionServer.dataStoreDir.listFiles();
		if(files == null)
			return null;
		Arrays.sort(files);
		
//		for(File f : files)
//			System.out.println(f.getName());
		
		//now get the newest 10 datasets
		int dataSetsCn = 0;
		int filesTried = 0;
		BufferedReader reader;
		ArrayList<String> dataSetsFile;
		while(dataSetsCn < amount) {
			
			//try a new file
			try {
				//put all dataSets from the file into a ArrayList
				if(filesTried >= files.length)
					break;
				reader = new BufferedReader(new FileReader(files[files.length - 1 - filesTried]));
				
				dataSetsFile = new ArrayList<String>();				
				String line;
				while((line = reader.readLine()) != null) 
					dataSetsFile.add(line.split("'")[1]);
				
				//loop through dataSet lines and add them to the dataSets Array
				for(int i = dataSetsFile.size()-1; i >= 0; i--) {
					dataSets += dataSetsFile.get(i) + " ";
					dataSetsCn++;
					
					if(dataSetsCn >= amount)
						break;
				}
				
				filesTried++;
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return dataSets;
	}
}
