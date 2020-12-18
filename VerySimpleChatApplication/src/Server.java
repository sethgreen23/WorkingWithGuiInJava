import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {


	ServerSocket serverSocket;
	ArrayList messages;
	public static void main(String[] args) {
		Server server= new Server();
		server.go();

	}

	private void go() {
		// the only thing that is different from the versimple chat app 3 is that we used the arraylist
		// to store a printwriter everytime we get a message from the user
		messages = new ArrayList();
		try {
			serverSocket = new ServerSocket(5000);
			while(true) {
				Socket clientSocket = serverSocket.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(),true);
				messages.add(writer);
				Thread clientHandler = new Thread(new ClientHandler(clientSocket));
				clientHandler.start();
				System.out.println("Connection made from the server");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public class ClientHandler implements Runnable {
		Socket clientSocket;
		public ClientHandler(Socket clientSocket){
			this.clientSocket = clientSocket;
		}
		@Override
		public void run() {
			String message;
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
				while((message=reader.readLine())!=null) {
					System.out.println(message);
					sendEveryone(message);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		private void sendEveryone(String message) {
			//and here we keep getting the last notification of the client message
			//but in reality we dont need because to do all this crap because we already have the socket
			//and anyway we will get notifyed by the thread when he get started with the receive of the client message
			Iterator iterator = messages.iterator();
	        while (iterator.hasNext()) {
	            PrintWriter writer = (PrintWriter)iterator.next();
	            writer.println(message);
	        }
			
		}

	}

}
