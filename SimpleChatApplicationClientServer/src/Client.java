import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;


public class Client {

	JTextArea outGoing;
	PrintWriter writer;
	JFrame frame;
	Socket socket;
	
	
	private void go() {
		frame = new JFrame("Simple Chat Application");
		JPanel mainPanel = new JPanel();
		outGoing = new JTextArea(3,30);
		JButton send = new JButton();
		send.setText("Send");
		send.addActionListener(new MySendListener());
		mainPanel.add(outGoing);
		mainPanel.add(send);
		frame.getContentPane().add(mainPanel);
		setUpNetwork();
		frame.setSize(400,150);
		frame.setVisible(true);
		
	}
	
	private void setUpNetwork() {
		
		try {
			socket = new Socket("127.0.0.1",4242);
			writer = new PrintWriter(socket.getOutputStream());
			System.out.println("Connection established");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public class MySendListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			writer.println(outGoing.getText());
			writer.flush();
			outGoing.setText("");
			outGoing.requestFocus();

		}

	}
	

	public static void main(String[] args) {
		Client client = new Client();
		client.go();

	}
}

