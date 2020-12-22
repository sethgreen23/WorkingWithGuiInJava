import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	ArrayList<ObjectOutputStream> outputList;
	public static void main(String[] args) {
		new Server().start();

	}

	private void start() {
		outputList = new ArrayList<ObjectOutputStream>();
		ServerSocket serverSocket;
		Socket socket;
		try {
			serverSocket = new ServerSocket(5000);
			while(true) {
				socket = serverSocket.accept();
				ObjectOutputStream oo = new ObjectOutputStream(socket.getOutputStream());
				outputList.add(oo);
				Thread t = new Thread(new MyClientHandler(socket));
				t.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public class MyClientHandler implements Runnable {
		Socket socket;
		MyClientHandler(Socket socket){
			this.socket = socket;
		}
		@Override
		public void run() {
			try {
				ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
				Object message=null;
				while((message=oi.readObject())!=null) {
					String o1=(String) message;
					boolean[] o2=(boolean[]) oi.readObject();
					tellEveryOne(o1,o2);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		private void tellEveryOne(String o1, boolean[] o2) {
			try {
				Iterator iterator = outputList.iterator();
				while(iterator.hasNext()) {
					ObjectOutputStream oo=(ObjectOutputStream)iterator.next();
					oo.writeObject(o1);
					oo.writeObject(o2);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
}
