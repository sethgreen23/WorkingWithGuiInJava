import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	public ArrayList<String> messages ;

	public void receiveMessage() {
		try {
			
			ServerSocket server = new ServerSocket(4242);
			while(true) {
				Socket socket = server.accept();
				//Inuput stream work as bridge between socket(bytes) and objectInputStream(object)
				InputStream stream = socket.getInputStream();
				ObjectInputStream reader = new ObjectInputStream(stream);
				//get the object from the client
				messages=(ArrayList<String>)reader.readObject();
				//System.out.println(messages);
				//show the object content
				showMessages();
				socket.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//showMessages();
	}
	
	public void showMessages() {
		
		for(String message : messages) {
			System.out.println(message);
		}
	}
	public boolean available(int port){
		final int MIN_NUMBER_PORT=0;
		final int MAX_NUMBER_PORT=65535;
		
		if(port<MIN_NUMBER_PORT || port>MAX_NUMBER_PORT) {
			throw new IllegalArgumentException("Invalid start port: " + port);
		}
		
		ServerSocket server=null;
		
		try {
			server = new ServerSocket(port);
			return true;
		}catch(Exception e) {}
		finally {
			if(server!=null) {
				try {
					server.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		if(server.available(4242)) {
			server.receiveMessage();
		}
	}
}

