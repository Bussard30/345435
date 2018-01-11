
package de.socket.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
 
public class Main {
	
	private static final int PORT_PI_CON = 5426;
	private static final int PORT_BROWSER_CON = 80;
	
	private static File dataStoreFile = new File("data.txt");
	
	private static boolean online;
	
	public static void main(String[] args) {
		System.out.println("test");
		startDataCollectorServer();
		startBrowserServer();
		
	}
	
	private static long lastBrowserSend = 0;
	
	public static void sendHTMLFile(Socket s){
		try{
			
			if(System.currentTimeMillis() > lastBrowserSend + 1000){
				//create HTML file
				System.out.println("8");
				CopyOption[] options = new CopyOption[]{
				  StandardCopyOption.REPLACE_EXISTING,
				  StandardCopyOption.COPY_ATTRIBUTES
				}; 
				Path p0 = Paths.get("H://dummy.html");
				Path p1 = Paths.get("H://index.html");
				Files.copy(p0, p1, options);
				//copy dummy 
				Charset charset = StandardCharsets.UTF_8;
				System.out.println("8");
				String content = new String(Files.readAllBytes(p1), charset);
				//TODO GET DATA FROM RASPBERRY PI
				System.out.println("8");
				content = content.replaceAll("vk94720", "hallo das 1 daten ist" );
				Files.write(p1, content.getBytes(charset));
				
				System.out.println("8a");
				
				File outputFile = p1.toFile();
				sendRawFile(s, outputFile);		
				
				lastBrowserSend = System.currentTimeMillis();
			}
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}
	
	public static void sendRawFile(Socket s, File f){
		try{
//			PrintWriter out = new PrintWriter(s.getOutputStream());
			OutputStream out = s.getOutputStream();

//			out.print("HTTP/1.1 200 OK");
//		    out.print("\r\n");
//		    out.print("Content-Type: text/html");
//		    
////		    out.println("<p> Hello world </p>");
//
//
//		    BufferedReader br = new BufferedReader(new FileReader(f));
//		    String ln;
//		    while((ln = br.readLine()) != null){
//		    	System.out.println("sending: " + ln);
//		    	out.print(ln);
//			    out.print("\r\n");
//		    }
//		    
		    
		    
		    FileInputStream fIn = new FileInputStream(f);
		    
		    String server = "Java Http Server";
		    String statusLine = null;
		    String typeLine = null;
		    String body = null;
		    String lengthLine = "error";

		    
	       statusLine = "HTTP/1.0 200 OK" + "\r\n";
	       //get content type by extension
	       typeLine = "Content-type: html  \r\n";
	       lengthLine = "Content-Length: " + (new Integer(fIn.available())).toString() + "\r\n";
		    

		    out.write(statusLine.getBytes());
		    out.write(server.getBytes());
		    out.write(typeLine.getBytes());
		    out.write(lengthLine.getBytes());

		    out.write("\r\n".getBytes());
		    
//			FileInputStream fStream = new FileInputStream(f);
//			//TODO mehrere dateien nacheinander schicken (html /css)
//			byte[] bytes = new byte[16*1024];
//			
//			int count;
//			while((count = fStream.read(bytes)) > 0){
//				s.getOutputStream().write(bytes, 0, count);
//			}
		    
		    byte[] buffer = new byte[1024];
		    int bytes = 0;

		    while ((bytes = fIn.read(buffer)) != -1) {
		      out.write(buffer, 0, bytes);
		    }
			
			out.flush();
			
			out.close();
			
			fIn.close();
			
		}catch(Exception exc){
			
		}
	}
	
	public static void startDataCollectorServer(){
		online = true;
		(new Thread(){
			public void run(){
				this.setName("Server Data Collector Thread");
				try{
					ServerSocket serverSocket = new ServerSocket(PORT_PI_CON);
					
					while(online){
						Socket socket = serverSocket.accept();
						
						BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						
						processData(reader.readLine());		
						
						socket.close();
					}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void processData(String inputLine){
		
		//TODO process
		
		storeDataInFile(inputLine);
	}
	
	public static void storeDataInFile(String newData){
		try{
			System.out.println(newData);
			PrintWriter writer = new PrintWriter(new FileWriter(dataStoreFile,true));
			
			writer.println(newData + "\n");			
						
			writer.close();
		}catch(Exception exc){			
		}
	}
	
	public static void startBrowserServer(){
		
		(new Thread(){
			public void run(){
				this.setName("Browser Connector Thread");
				
				try{
					ServerSocket serverSocket = new ServerSocket(PORT_BROWSER_CON);
					
					while(true){
						Socket socket = serverSocket.accept();
						
						sendHTMLFile(socket);
						
						
						socket.close();
					}
				}catch(Exception exc){
					
				}
			}
		}).start();
		
	}
	
	public static byte[] toBytes(String s)
	{
		return s.getBytes();
	}
}
