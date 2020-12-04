import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;


public class BeatBox {
	


	JFrame frame;
	String[] instrumentsNames = {"Bass Drum", "Closed Hi-Hat", 
	         "Open Hi-Hat","Acoustic Snare", "Crash Cymbal", "Hand Clap", 
	         "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", 
	         "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo", 
	         "Open Hi Conga"};
	int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
	JPanel mainPanel;
	JPanel panel;
	ArrayList<JCheckBox> checkBoxList;
	Sequencer sequencer;
	Sequence sequence;
	Track track;
	public static void main(String[] args) {
		BeatBox beatBox = new BeatBox();
		beatBox.go();

	}

	private void go() {
		//create JFrame 
		frame = new JFrame("BeatBox Player");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//create container panel
		panel = new JPanel(new BorderLayout());
		//create a box for names
		Box namesBox = new Box(BoxLayout.Y_AXIS);
		//give it 25 pixel right space
		namesBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));
		//loop throw the instruments table and fill the namebox with tha label of each instrument name
		for(int i=0;i<instrumentsNames.length;i++) {
			JLabel label = new JLabel(instrumentsNames[i]);
			namesBox.add(label);
			namesBox.add(Box.createRigidArea(new Dimension(10, 10)));
		}
		//make buttonsBox to hold the buttons
		Box buttonsBox = new Box(BoxLayout.Y_AXIS);
		//give it a left space with 25 pixels
		buttonsBox.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
		//create a start button and add a listener to it
		JButton start = new JButton("Start");
		start.addActionListener(new MyStartListener());
		//create a stop button and add a listener to it
		JButton stop = new JButton("Stop");
		stop.addActionListener(new MyStopListener());
		//create a tempup button and add a listener to it
		JButton tempoUp = new JButton("Tempo Up");
		tempoUp.addActionListener(new MyTempoUpListener());
		//add tempo down button and add a listener to it
		JButton tempoDown = new JButton("Tempo Down");
		tempoDown.addActionListener(new MyTempoDownListener());
		//create a save button and add a listener to it
//		JButton serializeIt = new JButton("Save Track");
//		serializeIt.addActionListener(new MySerializeItListener());
//		//create a restore button and add a listener to it
//		JButton restoreIt = new JButton("Restore Track");
//		restoreIt.addActionListener(new MyRestoreItListener());
		//add the buttons and add a space down of each one with 5px
		buttonsBox.add(start);
		buttonsBox.add(Box.createRigidArea(new Dimension(0, 5)));
		buttonsBox.add(stop);
		buttonsBox.add(Box.createRigidArea(new Dimension(0, 5)));
		buttonsBox.add(tempoUp);
		buttonsBox.add(Box.createRigidArea(new Dimension(0, 5)));
		buttonsBox.add(tempoDown);
//		buttonsBox.add(Box.createRigidArea(new Dimension(0, 5)));
//		buttonsBox.add(serializeIt);
//		buttonsBox.add(Box.createRigidArea(new Dimension(0, 5)));
//		buttonsBox.add(restoreIt);
		//create a grid layout that holds 256 checkbox with 16 rows and 16 colums
		GridLayout layout = new GridLayout(16,16);
		//create a mainPanel with grid layout 
		mainPanel = new JPanel(layout);
		//inialise the checkbox list that will hold the checkbox objects
		checkBoxList = new ArrayList<JCheckBox>();
		for(int i=0; i<256; i++) {
			JCheckBox checkBox = new JCheckBox();
			//enable the checkboxes
			checkBox.setEnabled(true);
			//add them to the arraylist
			checkBoxList.add(checkBox);
			//add them to the mainpanel
			mainPanel.add(checkBox);
			//give to each checkbox actionlistener
			checkBox.addActionListener(new CheckBoxListener());
		}
		//add JMenubar
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new MySaveListener());
		JMenuItem load = new JMenuItem("Load");
		load.addActionListener(new MyLoadListener());
		menu.add(save);
		menu.add(load);
		menuBar.add(menu);
		//add the namebox to the west 
		panel.add(BorderLayout.WEST,namesBox);
		//add the buttonbox to the east
		panel.add(BorderLayout.EAST,buttonsBox);
		//add the mainpanel to the center to to principal panel
		panel.add(BorderLayout.CENTER,mainPanel);
		//give the pricipal panel a spacing from all directions with 25px
		panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25)); 
		frame.getContentPane().add(BorderLayout.CENTER,panel);
		//add to frame
		frame.setJMenuBar(menuBar);
		setUp();
		frame.setBounds(200,200,300,300);
		//pack method will adjust the frame proportionally to the centeral panel
		frame.pack();
		frame.setVisible(true);
	}
	
	
	
	
	
	
	//Subclasses declaration
	
	private void setUp() {
		//set the sequencer and open it
		try {
			//create and open the sequencer
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			//create the sequence
			sequence = new Sequence(Sequence.PPQ, 4);
			//create the track
			track = sequence.createTrack();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//methods declaration

	private void buildTrackAndPlay() {
		//delete the old track 
		sequence.deleteTrack(track);
		//create a brand new one
		track=sequence.createTrack();
		//array to store the notes one for every instrument in 16 bits
		boolean[] trackEvents = null;
		for(int i=0;i<instruments.length;i++) {
			//instantiate the array for each iteration that correspond to a different instrument
			trackEvents = new boolean[16];
			//
			for(int j=0;j<16;j++) {
				//take every 16 bits that correspond to and instrument and we check which checkbox correspond to each bit is On
				JCheckBox checkBox = (JCheckBox) checkBoxList.get(j+(16*i));
				//if the checkbox is On we mark that bit in the array with true otherwise it is false(default value for boolean primary variable)
				if(checkBox.isSelected()) {
					trackEvents[j] = true;
				}
			}
			//after marking every bit is On for a particular instrument we start making the track related to each instrument
			buildTrack(trackEvents,instruments[i]);
		}
		//we make sure to mark the 16 bit with a dummy note that the sequencer will go throw all the 16 bits
		makeNote(192,9,1,0,15);
		try {
			sequencer.setSequence(sequence);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mark the sequencer to loop continuously on the track
		sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		//set the tempo to 120 bits per minute
		sequencer.setTempoInBPM(120);
		//start playing the track
		sequencer.start();
		
	}





	private void buildTrack(boolean[] trackEvents,int note) {
		//go through the 16 bit of each instrument
		for(int i=0;i<trackEvents.length;i++) {
			boolean flag = trackEvents[i];
			//check if the current bit of that instrument is true
			if(flag == true) {
				//if so make note On and off of that note on the correspondent bit
				track.add(makeNote(144,9,note,100,i));
				//track.add(makeNote(176,9,127,0,i));
				track.add(makeNote(128,9,note,100,i+1));
			}
		}
		
	}





	private MidiEvent makeNote(int com, int channel, int note, int velocity, int tick) {
		//the standard way of making note 
		//the channel 9 correspond the many notes of the drum
		MidiEvent event=null;
		
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(com,channel,note,velocity);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event = new MidiEvent(message,tick);
		
		return event;
		
	}





	//checkbox  listener
	public class CheckBoxListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//when we check a checkbox the sequencer stop playing
			sequencer.stop();
			//we get the source object
			Object obj = e.getSource();
			//if that object is a checkbox thats where the work start
			if (obj instanceof JCheckBox) {
				//we cast the object to a checkbox
			    JCheckBox cb = (JCheckBox)obj;
			    //we check if the checkbox is selected
			    if(cb.isSelected()) {
			    	//we mark it as selected
			    	cb.setSelected(true);
			    }else {
			    	//if not we mark it as not selected
			    	cb.setSelected(false);
			    	
			    }
			    
			}
			//we build and play the track again
			buildTrackAndPlay();

		}

	}
	
	//start button listener
	public class MyStartListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//build and play the track
			buildTrackAndPlay();
			
		}
		
	}
	
	//stop button listener
	public class MyStopListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//stop the sequencer
			sequencer.stop();
			
		}
		
	}
	
	//tempoUp button listener
	public class MyTempoUpListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//we get the current tempo factore which is by default 1.0f because it is a float
			float tempo = sequencer.getTempoFactor();
			//we multiply it with +3% to make it faster
			sequencer.setTempoFactor((float)(1.03*tempo));
		}
		
	}
	
	//tempoDown button Listener
	public class MyTempoDownListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//we get the current tempo factore which is by default 1.0f because it is a float
			float tempo = sequencer.getTempoFactor();
			//we multiply it with -3% to make it slower
			sequencer.setTempoFactor((float)(0.97*tempo));
			
		}
		
	}
	
	
	
	
	
	//serializeIt button listener
//	public class MySerializeItListener implements ActionListener {
//		
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			// TODO Auto-generated method stub
//			try {
//				//we create connection to the file 
//				//if the file doesnt exit then the FileOutputStream will make one with "sound.ser" name
//				FileOutputStream file = new FileOutputStream("sound.ser");
//				//hook and ObjectOutputStream to FileOutputStream as chaning stream to work with objects in the writing
//				ObjectOutputStream os = new ObjectOutputStream(file);
//				//write the Arraylist in the file serialized
//				os.writeObject(checkBoxList);
//				//close the ObjectOutputStream and it will make sure to close the FileOutputStream
//				os.close();
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			//we show a message telling that the track has been serilized (saved)
//			JOptionPane.showInternalMessageDialog(null, "You Saved the Track.");
//			
//		}
//		
//	}
	
	//restoreIt button listener
	public class MyRestoreItListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//we stop the sequencer
			sequencer.stop();
			//create a list to hold the deserialized data of the checkboxlist
			ArrayList<JCheckBox> list = new ArrayList<JCheckBox>();
			
			try {
				//as usual we hook an ObjectInputStream as chaining stream to a FilInputStream as connection stream
				//to make sure we read from the "sound.ser" file if the file doenst exit and runtime error will raise 
				FileInputStream file = new FileInputStream("sound.ser");
				ObjectInputStream os = new ObjectInputStream(file);
				//we cast the read object from the readObject() method
				list = (ArrayList) os.readObject();
				//we go through the deserialize
				for(int i=0; i<list.size();i++) {
					//we get the checkbox from the deserialized object in the current index
					JCheckBox checkbox = list.get(i);
					//if the checkbox is selected
					if(checkbox.isSelected()) {
						//we mark it in the checklist as selected
						checkBoxList.get(i).setSelected(true);
					}else {
						//else we mark it as not selected
						checkBoxList.get(i).setSelected(false);
					}
				}
				//we build and play the restored track
				buildTrackAndPlay();
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
		
	}
	


	public class MySaveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser(new File(".").getAbsolutePath());
			int userSelection = chooser.showSaveDialog(frame);
			 
			if (userSelection == JFileChooser.APPROVE_OPTION) {
			    String fileName = chooser.getSelectedFile().getName()+".ser";
			    System.out.println(fileName);
			    File fileToSave = new File(fileName);
			    saveData(fileToSave);
			}

		}

		private void saveData(File fileToSave) {
			try {
				//we create connection to the file 
				//if the file doesnt exit then the FileOutputStream will make one with "sound.ser" name
				FileOutputStream file = new FileOutputStream(fileToSave);
				//hook and ObjectOutputStream to FileOutputStream as chaning stream to work with objects in the writing
				ObjectOutputStream os = new ObjectOutputStream(file);
				//write the Arraylist in the file serialized
				os.writeObject(checkBoxList);
				//close the ObjectOutputStream and it will make sure to close the FileOutputStream
				os.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//we show a message telling that the track has been serilized (saved)
			JOptionPane.showInternalMessageDialog(null, "You Saved the Track.");
			
		}

	}

	public class MyLoadListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser(new File(".").getAbsolutePath());
			fileChooser.setDialogTitle("Specify a file to load");   
			 
			int userSelection = fileChooser.showOpenDialog(frame);
			 
			if (userSelection == JFileChooser.APPROVE_OPTION) {
			    File fileToSave= fileChooser.getSelectedFile();
			    //System.out.println(fileToSave.getAbsolutePath());//good to know the absolutepath
			    loadData(fileToSave);
			}

		}

		private void loadData(File fileToLoad) {
			//we stop the sequencer
			sequencer.stop();
			//create a list to hold the deserialized data of the checkboxlist
			ArrayList<JCheckBox> list = new ArrayList<JCheckBox>();
			
			try {
				//as usual we hook an ObjectInputStream as chaining stream to a FilInputStream as connection stream
				//to make sure we read from the "sound.ser" file if the file doenst exit and runtime error will raise 
				FileInputStream file = new FileInputStream(fileToLoad);
				ObjectInputStream os = new ObjectInputStream(file);
				//we cast the read object from the readObject() method
				list = (ArrayList) os.readObject();
				//we go through the deserialize
				for(int i=0; i<list.size();i++) {
					//we get the checkbox from the deserialized object in the current index
					JCheckBox checkbox = list.get(i);
					//if the checkbox is selected
					if(checkbox.isSelected()) {
						//we mark it in the checklist as selected
						checkBoxList.get(i).setSelected(true);
					}else {
						//else we mark it as not selected
						checkBoxList.get(i).setSelected(false);
					}
				}
				//we build and play the restored track
				//buildTrackAndPlay();
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}

	}

}

