import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

	ArrayList<String> messagesList = new ArrayList<String>();
	public void sendMessage() {
		try {
			Socket socket = new Socket("127.0.0.1",4242);
			OutputStream stream =socket.getOutputStream();
			ObjectOutputStream writer = new ObjectOutputStream(stream);
			sendMessages();
			writer.writeObject(messagesList);
			socket.close();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessages() {
		Scanner scanner = new Scanner(System.in);
		String message = null;
		do {
			System.out.println("Enter a message: ");
			message = scanner.nextLine();
			if(message.equalsIgnoreCase("exit")) 
				break;
			messagesList.add(message);
		}while(true);
		System.out.println("Thank you !");
		
	}

}
