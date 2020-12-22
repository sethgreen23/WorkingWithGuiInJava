import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	ArrayList<ObjectOutputStream> outputList;
	public static void main(String[] args) {
		//start the server
		new Server().start();

	}

	private void start() {
		//initialize the arraylist that will hold the
		//ObjectOutputStream each time we get notification with
		//the pattern sent from the user
		outputList = new ArrayList<ObjectOutputStream>();
		//declate the ServerSocket and the Socket
		ServerSocket serverSocket;
		Socket socket;
		try {
			//get the server listening on the port 5000
			serverSocket = new ServerSocket(5000);
			while(true) {
				//the server open the connection and accept the socket from the client 
				//the while loop will freeze till we get message from client
				socket = serverSocket.accept();
				//it is very important to insitialize the ObjectOutpurStream before the ObjectInputStream
				//that we dont get a deadlok here folow thread explaining that
				//https://stackoverflow.com/questions/54095782/the-program-stops-when-the-objectinputstream-object-is-created
				//explanation of ObjectInputStream and ObjectOutputStream
				//https://stackoverflow.com/questions/12715321/java-networking-explain-inputstream-and-outputstream-in-socket/12715760#:~:text=In%20Java%2C%20to%20send%20data,a%20socket%20on%20the%20wall.
				ObjectOutputStream oo = new ObjectOutputStream(socket.getOutputStream());
				//add the objectOutput stream to the list to make sure we will write with it when it is needed
				outputList.add(oo);
				//start the thread to handle the messages comming from the user we pass the socket and argument
				Thread t = new Thread(new MyClientHandler(socket));
				t.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public class MyClientHandler implements Runnable {
		//we get the socket from the start function where we will get the messages from clients
		Socket socket;
		MyClientHandler(Socket socket){
			this.socket = socket;
		}
		@Override
		public void run() {
			try {
				//once we get message from any client
				//we create an objectinput stream to read the messages
				ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
				Object message=null;
				//for our application we will get the message and the state of the checkboxes
				//readObject() method return and object the we need to cust what we will receve from it
				while((message=oi.readObject())!=null) {
					String o1=(String) message;
					boolean[] o2=(boolean[]) oi.readObject();
					//onece we got the message and the state object we have to send it to all the client
					tellEveryOne(o1,o2);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		private void tellEveryOne(String o1, boolean[] o2) {
			try {
				//here we keep geting the Objectoutputstream to write the object we received from the client 
				//to write it back to all the clients
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
