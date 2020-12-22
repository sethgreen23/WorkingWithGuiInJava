import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Client {
	
	public static int nextNumber;
	public final String HOST="127.0.0.1";
	
	String userName;
	int userId;
	Socket sock;
	ObjectOutputStream oo;
	ObjectInputStream oi;
	Sequencer sequencer;
	Sequence sequence;
	Track track;
	JTextArea myMessage;
	JList messageList;
	Vector<String> listVector;
	int checkBoxNumber = 256;
	ArrayList<JCheckBox> checkBoxList;
	HashMap<String,boolean[]> mapStringToPattern = new HashMap<String,boolean[]>();
	String[] instrumentsNames = {"Bass Drum", "Closed Hi-Hat", 
	         "Open Hi-Hat","Acoustic Snare", "Crash Cymbal", "Hand Clap", 
	         "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", 
	         "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo", 
	         "Open Hi Conga"};
	int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
	//start the client by asking the username
	Client(){
		//setup username
		setUserName();
		//increment the static counter
		nextNumber++;
		//attribute the current static number to the userid
		userId=nextNumber;
	}

	
	
	public static void main(String[] args) {
		//start the client application
		int number = Integer.parseInt(JOptionPane.showInputDialog("How many clients you want to make: "));
		for(int i=0;i<number;i++)
			new Client().startClient();

	}

	
	public void setUserName() {
		//get the username from dialog input
		userName = JOptionPane.showInputDialog("Please enter you username:");
		//if the input is null or empty then return default username "john_doe" otherwise return the username
		userName = (userName == null||userName.isEmpty())?"john_doe":userName;
		//print it out to the console
		System.out.println(userName);
	}
	
	
	private void startClient() {
		//initialise the arraylist that holds the checkbox objects for later manipulation
		checkBoxList = new ArrayList<JCheckBox>();
		//initialize the vector that will hold the strings of the JList
		listVector = new Vector<String>();
		//start the connection with the server
		try {
			//make connection to the localhost on the port number 5000
			sock = new Socket(HOST,5000);
			//initilise an object writer
			oo = new ObjectOutputStream(sock.getOutputStream());
			//initialize an object reader
			oi = new ObjectInputStream(sock.getInputStream());
			//if the connection succeed show this message
			System.out.println("Connection made.Now you can connect with server");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Sorry dude you have to play alone.");
		}
		
		//setUpMidi
		setUpMidi();
		//buildGUI
		buildGUI();
	}

	
	private void setUpMidi() {
		//setup sequencer 
		try {
			//intilise the sequencer and open it
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			//create sequence
			sequence = new Sequence(Sequence.PPQ, 4);
			//create track
			track = sequence.createTrack();
			//if everything is fine show this message
			System.out.println("Midi System setup");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void buildGUI() {
		//create frame
		JFrame frame = new JFrame("BeatBox Application");
		//activate the exit on close option
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//create the main container with border layout we overwride the default flowlayout
		BorderLayout layout = new BorderLayout();
		JPanel background =  new JPanel(layout);
		//set invisible border to make space with 10 px all around
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		//make a box to hold the names the layout will be created verticaly
		Box namesBox = new Box(BoxLayout.Y_AXIS);
		//create a vertical spacing of 5 px and 10px right
		namesBox.setBorder(BorderFactory.createEmptyBorder(5, 0, 0,10));
		for(int i=0;i<instrumentsNames.length;i++) {
			//create labels with instruments names
			namesBox.add(new JLabel(instrumentsNames[i]));
			//make vertical spacing with 10 px after eachone
			namesBox.add(Box.createRigidArea(new Dimension(0, 10)));
		}
		//add the names box to the main container on the left
		background.add(BorderLayout.WEST,namesBox);
		
		//create a button box to hold the buttons with the textarea and list
		Box buttonsBox = new Box(BoxLayout.Y_AXIS);
		//add verticals spacing for the top top of the buttons box of 5px
		buttonsBox.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		//wrap each button with a panel where you put the button on the left 
		//of the panel then add the panel to the buttonsBox
		JButton start = new JButton("Start");
		start.addActionListener(new MyStartListener());
		JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		startPanel.add(start);
		buttonsBox.add(startPanel);
		JButton stop = new JButton("Stop");
		stop.addActionListener(new MyStopListener());
		JPanel stopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		stopPanel.add(stop);
		buttonsBox.add(stopPanel);
		JButton tempoUp = new JButton("Tempo Up");
		tempoUp.addActionListener(new MyTempoUpListener());
		JPanel tempoUpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tempoUpPanel.add(tempoUp);
		buttonsBox.add(tempoUpPanel);
		JButton tempoDown = new JButton("Tempo Down");
		tempoDown.addActionListener(new MyTempoDownListener());
		JPanel tempoDownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tempoDownPanel.add(tempoDown);
		buttonsBox.add(tempoDownPanel);
		JButton sendPattern = new JButton("Send Pattern");
		sendPattern.addActionListener(new MySendListener());
		JPanel sendPatternPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		sendPatternPanel.add(sendPattern);
		buttonsBox.add(sendPatternPanel);
		
		//create text area without scroll
		//otherwise i will have spaced buttons from the left
		myMessage = new JTextArea();
		myMessage.setBorder(BorderFactory.createDashedBorder(Color.black));
		myMessage.setWrapStyleWord(true);
		myMessage.setLineWrap(true);
		buttonsBox.add(myMessage);
		buttonsBox.add(Box.createRigidArea(new Dimension(5,5)));
		
		//create list that holds the patterns and messges from the other clients
		//add a vertical stroll to the pane
		messageList = new JList();
		messageList.addListSelectionListener(new MyListListener());
		messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane pane = new JScrollPane(messageList);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		messageList.setListData(listVector);
		buttonsBox.add(pane);
		//add the button box to the right
		background.add(BorderLayout.EAST,buttonsBox);
		
		//create grid layout to hold the 256 boxes 16 bits for each instrument
		GridLayout gridLayout = new GridLayout(16,16,2,2);
		JPanel mainPanel = new JPanel(gridLayout);
		//give the grid panel a left and right spacing with 20px
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		
		//create checkboxes and add them to the checkboxlist and to the grid panel
		//unselect them all
		for(int i=0;i<checkBoxNumber;i++) {
			JCheckBox checkBox = new JCheckBox();
			checkBox.setSelected(false);
			checkBoxList.add(checkBox);
			mainPanel.add(checkBox);
		}
		
		//add label on the top region of the main container where you put
		//the username and user id set the font,color(foreground),vertical and horizontal alignement
		JLabel userInfo = new JLabel("User Name: "+userName+",  ID: "+userId);
		userInfo.setFont(new Font("sansserif",Font.BOLD,18));
		userInfo.setForeground(new Color(240,180,90));
		userInfo.setVerticalTextPosition(SwingConstants.TOP);
		userInfo.setHorizontalTextPosition(SwingConstants.CENTER);
		JPanel northPanel = new JPanel();
		
		//add the label to the north reageion of the north panel
		northPanel.add(BorderLayout.NORTH,userInfo);
		background.add(BorderLayout.NORTH,northPanel);
		background.add(BorderLayout.CENTER,mainPanel);
		
		frame.add(BorderLayout.CENTER,background);
		frame.setBounds(400, 200, 1, 1);
		//add the thread that will handle the server messages
		Thread t = new Thread(new MyServerHandler());
		t.start();
		frame.pack();
		frame.setVisible(true);
		
	}

	
	
	private void buildTrack() {
		//declare a 16 bits array to store the bits of each instrument
		boolean[] trackArray = null;
		//recreate the track
		sequence.deleteTrack(track);
		track = sequence.createTrack();
		
		//on each iteration get the checked bits for each instrument
		//and create a track for it individually
		for(int i=0;i<16;i++) {
			//each time initialize a new boolean array to hold 
			//the checked checkbooks with true else the bit take false instead
			trackArray = new boolean[16];
			//get the instrument you want to play for the next 16 bits
			int note = instruments[i];
			for(int j=0;j<16;j++) {
				//here we keep track of the 16 bits of each instrument
				JCheckBox checkBox = (JCheckBox) checkBoxList.get(j+(16*i));
				if(checkBox.isSelected()) {
					trackArray[j]=true;
				}else {
					trackArray[j]=false;
				}
					
			}
			//build the track for each instrument
			buildTrack(trackArray,note);
		}
		//here is a dump midievent on the 16th bit to make sure we go till the last bit
		track.add(makeEvent(192,1,9,0,15));
		try {
			//read continually the sequence and start the sequencer
			sequencer.setSequence(sequence);
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			sequencer.setTempoInBPM(120);
			sequencer.start();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void buildTrack(boolean[] trackArray, int note) {
		for(int i=0;i<trackArray.length;i++) {
			boolean flag=trackArray[i];
			//build the bits on and off for the bits that are marked with true
			if(flag==true) {
				track.add(makeEvent(144,9,note,100,i));
				track.add(makeEvent(128,9,note,100,i+1));	
			}

		}
		
	}

	//create a midiEvent
	private MidiEvent makeEvent(int command, int channel, int note, int velocity, int bit) {
		MidiEvent event=null;
		try {
			ShortMessage message = new ShortMessage();
			message.setMessage(command, channel, note,velocity);
			event = new MidiEvent(message,bit);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return event;
		
	}

	//start button listener
	public class MyStartListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			buildTrack();

		}

	}
	
	
	//stop button listener
	public class MyStopListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			sequencer.stop();
		}
		
	}
	
	
	//tempoup button listener
	public class MyTempoUpListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//add 3% of velocity to the track reading
			float tempo = sequencer.getTempoFactor()*1.03f;
			System.out.println(tempo);
			sequencer.setTempoFactor(tempo);
			
		}
		
	}
	
	
	//tempodown button listener
	public class MyTempoDownListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//lower 3% of the velocity of reading the track
			float tempo = sequencer.getTempoFactor()*0.97f;
			System.out.println(tempo);
			sequencer.setTempoFactor(tempo);
			
		}
		
	}
	
	
	//send button listener
	public class MySendListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//initialize the boolean array that will hold
			//the states of current pattern that we will get from
			//checkBoxList
			boolean[] checkBoxState = new boolean[256];
			for(int i=0;i<checkBoxList.size();i++) {
				JCheckBox checkbox = (JCheckBox) checkBoxList.get(i);
				//if the checkbox is selected we put true else we put false
				if(checkbox.isSelected()) {
					checkBoxState[i]=true;
				}else {
					checkBoxState[i]=false;
				}
			}
			//get the message from the textarea
			//make the massage contains username,userid and the message
			String message = userName+" "+userId+": "+myMessage.getText();
			myMessage.setText("");
			try {
				//write the message and the current pattern
				oo.writeObject(message);
				oo.writeObject(checkBoxState);
				
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	}
	
	//listselection listener
	public class MyListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			//explaining what does getValueIsAdjusting() function
			//https://stackoverflow.com/questions/10860419/what-exactly-does-getvalueisadjusting-do
			if(!e.getValueIsAdjusting()) {
				String message = (String)messageList.getSelectedValue();
				if(message!=null) {
					boolean[] checkBoxState = mapStringToPattern.get(message);
					//System.out.println(Arrays.toString(checkBoxState)+"**********"+message);
					fromStateToPattern(checkBoxState);
				}
				
			}

		}



	}
	
	private void fromStateToPattern(boolean[] checkBoxState) {
		sequencer.stop();
		//get the checkbox
		int i=0;
		JCheckBox checkBox=null;
		for(boolean val : checkBoxState) {
			checkBox = checkBoxList.get(i);
			if(val==true) {
				checkBox.setSelected(true);
				checkBoxList.set(i,checkBox);
			}else {
				checkBox.setSelected(false);
				checkBoxList.set(i,checkBox);	
			}
			i++;
		}
		buildTrack();
	}
	
	// my server handler
	public class MyServerHandler implements Runnable {

		@Override
		public void run() {
			Object obj=null;
			String o1=null;
			boolean[] o2=null;
			//keep listeneing to the server
			try {
				while((obj=oi.readObject())!=null) {
					o1 = (String) obj;
					o2 = (boolean[]) oi.readObject();
					System.out.println(o1);
					//add the message to the vector
					listVector.add(o1);
					//add the two object to the hashmap for further work
					mapStringToPattern.put(o1, o2);
					//set the list data to the new vector
					messageList.setListData(listVector);
				}
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
