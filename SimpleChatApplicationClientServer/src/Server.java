import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	ServerSocket server;
	public static void main(String[] args) {
		Server server = new Server();
		//if(server.available(4242))
			server.go();

	}

	private void go() {
		
		try {
			server = new ServerSocket(4242);
			Socket socket = null;
			while(true) {
				socket = server.accept();
				InputStreamReader brige  = new InputStreamReader(socket.getInputStream());
				BufferedReader reader = new BufferedReader(brige);
				String message = null;
				while((message = reader.readLine())!=null) {
					System.out.println(message);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private boolean available(int port) {
		if(port<0 || port>56535) {
			throw new IllegalArgumentException("Wrong port number");
		}
		ServerSocket server = null;
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

}
