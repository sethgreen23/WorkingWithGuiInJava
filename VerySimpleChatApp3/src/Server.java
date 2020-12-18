import java.io.*;
import java.net.*;
import java.util.*;


public class Server {

	public static void main(String[] args) {
		Server server = new Server();
		server.go();

	}

	private void go() {
		try {
			//establish the connection to the port 5000
			ServerSocket serverSocket = new ServerSocket(5000);
			while(true) {
				//keep waiting for a user soket
				Socket socket  = serverSocket.accept();
				//establish a thread to handle the message received from certain client
				Thread t = new Thread(new ClientMessageHandler(socket));
				t.start();
				System.out.println("Server Connection made.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public class ClientMessageHandler implements Runnable {
		Socket socket ;
		ClientMessageHandler(Socket socket){
			this.socket=socket;
		}
		@Override
		public void run() {
			try {
				//get the client socket and read it throw a bufferedReader
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String s;
				//while the bufferedReader is not null
				while((s=reader.readLine())!=null) {
					System.out.println("Message from Clientes: "+s);
					//send the message to everyone
					tellEverone(s);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		private void tellEverone(String s) {
			//send the message to everyone
			PrintWriter writer=null;
			try {
				writer = new PrintWriter(socket.getOutputStream(),true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				writer.println(s);
			
		}

	}
}
