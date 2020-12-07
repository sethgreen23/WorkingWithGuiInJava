import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.*;
import javax.swing.event.*;

public class Client {



	JFrame frame;
	JPanel mainPanel;
	JTextArea textArea;
	JButton send;
	public static void main(String[] args) {
		Client client = new Client();
		client.go();

	}

	private void go() {
		//create frame
		frame = new JFrame("Simple Chat Room");
		//create mainpanel
		//make flowlayout with h and v gap of 30px
		FlowLayout flow = new FlowLayout(FlowLayout.CENTER,30,30);
		//main panel with the flow layout that we already made
		mainPanel = new JPanel(flow);
		//create textfield
		textArea= new JTextArea(10,35);
		//setting of the textarea
		textArea.setSize(3,20);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText("Write your message ......");
        //add listener for the textarea
        textArea.addFocusListener(new MyTextAreaListener());
		JScrollPane pane = new JScrollPane(textArea);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);;
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);;
		mainPanel.add(pane);
		
		send = new JButton("Send");
		send.addActionListener(new MySendListener());
		mainPanel.add(send);
		frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
		frame.setSize(500,300);
		frame.pack();
		//the send button take the focus
		send.requestFocusInWindow();
		frame.setVisible(true);
		
	}
	private void clearField() {
		// TODO Auto-generated method stub
		textArea.setText("");
		//give the focus to the button
		send.requestFocusInWindow();
		emptyMessage(textArea);
		
	}

	private void sendMessage() {
		// TODO Auto-generated method stub
		
		try {
			//make a socket to the server in localhost with port 4242
			Socket socket = new Socket("127.0.0.1",4242);
			//create a print writer that we hock it with the socket
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			//make a buffered writer that you hock it with pritwriter to writer manylines
			BufferedWriter writer = new BufferedWriter(out);
			//send the message with println
			String message = null;
			message=textArea.getText();
			out.println(message);
			//clear the stream
			out.flush();
			System.out.println(message);
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public class MySendListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			sendMessage();
			clearField();

		}

		

	}
	public  class MyTextAreaListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {
			JTextArea textArea = (JTextArea) e.getSource();
			writeMessage(textArea);
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			JTextArea textArea = (JTextArea) e.getSource();
			emptyMessage(textArea);
			
		}
	
	}
	
	private void emptyMessage(JTextArea textArea) {
		if(textArea.getText().equalsIgnoreCase("")) {
			textArea.setText("Write your message ......");
		}
	}
	private void writeMessage(JTextArea textArea) {
		if(textArea.getText().equalsIgnoreCase("Write your message ......")) {
			textArea.setText("");
		}
	}
	
}
