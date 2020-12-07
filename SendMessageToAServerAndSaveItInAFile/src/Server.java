import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		Server server = new Server();
		if(server.available(4242)) {
			server.go();
		}


	}

	private void go() {
		// TODO Auto-generated method stub
		
		try {
			//create server socket
			ServerSocket server = new ServerSocket(4242);
			//lunch the while loop 
			while(true) {
				//create socket and wait request of a client
				Socket socket = server.accept();
				//make an input reader that translate bytes from socket a character
				InputStreamReader bridge = new InputStreamReader(socket.getInputStream());
				//create buffered reader to read a stream of characters
				BufferedReader in = new BufferedReader(bridge);
				String line=null;
				StringBuilder message = new StringBuilder();
				//read the bufferedReader content for each message
				while((line=in.readLine())!=null) {
					message.append(line+"*");
				}
				//we delete the last * in every message
				message = message.delete(message.length()-1, message.length());
				//we insert a '-' and we take it as separator of messages
				message.append("\n-");
				System.out.println(message);
				//write on a file manytime we use the bufferedWriter
				try(FileWriter fw = new FileWriter("chat.txt", true);
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
				{
				    out.println(message);
				    //more code
				} catch (IOException e) {
				    //exception handling left as an exercise for the reader
				}
				
				//write continuously on a file
				
				socket.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//check availibility of the port
	private boolean available (int port) {
		if(port<0 || port>65535) {
			throw new IllegalArgumentException("Port number is illigal");
		}
		ServerSocket socket=null;
		try {
			socket = new ServerSocket(port);
			return true;
		}catch(Exception e) {}
		finally {
			if(socket!=null)
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return false;
	}

}
