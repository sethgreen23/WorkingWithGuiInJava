import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class Client implements Runnable{

	JFrame frame;
	JPanel mainPanel;
	JTextArea screen;
	JTextArea message;
	JButton send;
	Socket socket;
	final String HOST="127.0.0.1";
	BufferedReader reader;
	PrintWriter writer;
	
	public static void main(String[] args) {
		Client client = new Client();
		client.go();

	}

	private void go() {
		prepareUI();
		
	}

	private void prepareUI() {
		frame = new JFrame("Very Simple Chat App");
		//make a flowlayout for the panel with 30 pixel space in all directions
		FlowLayout flow = new FlowLayout(FlowLayout.CENTER,30,30);
		mainPanel = new JPanel(flow);
		screen = new JTextArea(10,35);
		//disable the writing on the textarea
		screen.setEditable(false);
		screen.setLineWrap(true);
		screen.setWrapStyleWord(true);
		//create border for the entire screen textarea
		screen.setBorder(BorderFactory.createDashedBorder(Color.black));
		JScrollPane sScreen = new JScrollPane(screen);
		//give the textare a vertical scroll
		sScreen.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sScreen.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		message = new JTextArea(1,37);
		message.setBorder(BorderFactory.createDashedBorder(Color.black));
		message.setLineWrap(true);
		message.setWrapStyleWord(true);
		//the message textarea get the initial focus
		message.requestFocusInWindow();
		send = new JButton("Send");
		//add listener to the send button
		send.addActionListener(new MySendListener());
		mainPanel.add(sScreen);
		mainPanel.add(message);
		mainPanel.add(send);
		//add the main panel to the center
		frame.add(BorderLayout.CENTER,mainPanel);
		frame.setSize(450,500);
		//start the connection with the server
		setUpConnection();
		frame.setVisible(true);
	}

	private void setUpConnection() {
		try {
			//set up the connection with the localserver on the port number 5000
			socket = new Socket(HOST,5000);
			//initialize the writer
			writer = new PrintWriter(socket.getOutputStream(),true);
			//initialize the reader
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//start the thread that will do
			//listen to the Server when he send messages and write them instantly on the screen TextArea
			//it will be a loop
			Thread t = new Thread(this);
			t.start();
			System.out.println("Connection made");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//send action listener class
	public class MySendListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//when we press the send button
			//get the written message from the textarea
			String output = message.getText();
			//send it to the server
			writer.println(output);
			//empty the message text area
			message.setText("");
			message.requestFocus();

		}

	}

	@Override
	public void run() {
		String input;
		//we keep listening till we get a message from the server
		try {
			while((input=reader.readLine())!=null){
				//append the received message to the screen textarea
				screen.append("*"+input+".\n");
				System.out.println("Message from Server "+input);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
