import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicBorders;

public class Client implements Runnable{

	Socket socket;
	JTextArea clientsMessage;
	JTextArea message;
	JButton send;
	JFrame frame;
	JPanel mainPanel;
	BufferedReader reader;
	PrintWriter writer;
	public static void main(String[] args) {
		new Client().go();
	}
	private void go() {
		//create frame
		frame = new JFrame("Very Simple ChatApp");
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		//create textarea for messages of clients
		clientsMessage=new JTextArea(10,40);
		clientsMessage.setLineWrap(true);
		clientsMessage.setWrapStyleWord(true);
		clientsMessage.setEditable(false);
		JScrollPane mScroll = new JScrollPane(clientsMessage);
		mScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		mScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		message = new JTextArea(3,40);
		message.setLineWrap(true);
		message.setWrapStyleWord(true);
		message.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		send = new JButton();
		send.setText("Send");
		send.addActionListener(new MySendListener());
		mainPanel.add(mScroll);
		mainPanel.add(message);
		mainPanel.add(Box.createVerticalStrut(100));
		mainPanel.add(Box.createVerticalStrut(100));
		mainPanel.add(send);
		frame.add(BorderLayout.CENTER,mainPanel);
		setUpConnection();
		Thread t = new Thread(this);
		t.start();
		frame.setSize(500,500);
		frame.setVisible(true);
		
	}
	
	//setUpconnection function 
	private void setUpConnection() {
		
		try {
			socket=new Socket("127.0.0.1",5000);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(),true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Connection made.");
		
		
	}
	
	//run method for the thread
	@Override
	public void run() {
		String message;
		try {
			while((message=reader.readLine())!=null) {
				clientsMessage.append("-"+message+"\n");
				System.out.println(message);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//send listener java
	public class MySendListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String data = message.getText();
			writer.println(data);
			message.setText("");
			message.requestFocus();

		}

	}

}
