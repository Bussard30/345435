
package de.socket.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
 
public class Main {
	
	private static ServerSocket serverSocket;
	public static final int PORT = 63040;
	
	private static Socket socket; 
	
	
	private static File dataFile = new File("H://beispiel.txt");
	private static File dataFile2 = new File("H://beispiel2.txt");
	
	
	public static void main(String[] args) {
		
	
		
		try{
			
			serverSocket = new ServerSocket(PORT);
			socket = serverSocket.accept();
			
			sendFile(socket, dataFile);
			
			
//			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
//
//			String line;
//			PrintWriter dataWriter;
//			while(true){
//				line = reader.readLine();
//				
//				dataWriter = new PrintWriter(dataFile);
//				dataWriter.println(line);
//				System.out.println("received: '" + line + "'");
//				dataWriter.close();
//			}			
			socket.close();
		}catch(Exception exc){
			exc.printStackTrace();
		}
		
		
	}
	
	public static void sendFile(Socket s, File f){
		try{
			FileInputStream fStream = new FileInputStream(f);
			//TODO mehrere dateien nacheinander schicken (html /css)
			byte[] bytes = new byte[16*1024];
			
			int count;
			while((count = fStream.read(bytes)) > 0){
				s.getOutputStream().write(bytes, 0, count);
			}			
			
			
		}catch(Exception exc){
			
		}
	}
	
	
}
