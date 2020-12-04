/*
 * source:
 * 
 * 1- Spacing the JElements verticaly and horizontaly
 * https://perso.ensta-paris.fr/~diam//java/online/notes-java/GUI/layouts/42boxlayout-spacing.html
 * */
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.sound.midi.*;
import javax.swing.*;

public class BeatBox {

	//global variables declaration
	JFrame frame;
	JPanel panel;
	JPanel mainPanel;
	ArrayList<JCheckBox> checkBoxes;
	String[] instrumentsNames = {"Bass Drum", "Closed Hi-Hat", 
	         "Open Hi-Hat","Acoustic Snare", "Crash Cymbal", "Hand Clap", 
	         "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", 
	         "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo", 
	         "Open Hi Conga"};
	int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
	Sequencer sequencer;
	Sequence sequence;
	Track track;
	
	public static void main(String[] args) {
		new BeatBox().buildGui();

	}

	private void buildGui() {
		//create JFrame
		frame = new JFrame("Cyber Beatbox");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//create borderlayout for panel
		BorderLayout layout = new BorderLayout();
		//create the panel
		panel = new JPanel(layout);
		//set border between the frame and panel
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		//create the box that will hold the button with boxLayout on the y axis
		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		//create and add the buttons (four buttons) with linking it with listener
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new MyStartAction());
		buttonBox.add(startButton);
		buttonBox.add(Box.createVerticalStrut(10));
		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new MyStopAction());
		buttonBox.add(stopButton);
		//add space to the button
		buttonBox.add(Box.createVerticalStrut(10));
		JButton tempoUpButton = new JButton("Tempo Up");
		tempoUpButton.addActionListener(new MyTempoUpAction());
		buttonBox.add(tempoUpButton);
		buttonBox.add(Box.createVerticalStrut(10));
		JButton tempoDownButton = new JButton("Tempo Down");
		tempoDownButton.addActionListener(new MyTempoDownAction());
		buttonBox.add(tempoDownButton);
		buttonBox.add(Box.createHorizontalStrut(10));
		//add the box button to the panel in is east
		panel.add(BorderLayout.EAST,buttonBox);
		//create the weast box to hold the names labels
		Box namesBox = new Box(BoxLayout.Y_AXIS);
		for(int i = 0; i < 16; i++) {
			JLabel text = new JLabel(instrumentsNames[i]);
			namesBox.add(text);
			//create space area in the bottom of each jlabel
			namesBox.add(Box.createVerticalStrut(10));
		}
		panel.add(BorderLayout.WEST,namesBox);
		//add the panel to the frame
		frame.getContentPane().add(panel);
		//create and add the gridLayout to the mainPanel
		GridLayout gridLayout = new GridLayout(16,16,2,1);
		mainPanel = new JPanel(gridLayout);
		//create and add the checkboxes to the mainPanel
		checkBoxes = new ArrayList<JCheckBox>();
		for(int i = 0; i < 256; i++) {
			JCheckBox checkBox = new JCheckBox();
			checkBox.setSelected(false);
			//add checkbox to the checkBoxes arraylist for later manipulation
			checkBoxes.add(checkBox);
			mainPanel.add(checkBox);
		}
		//add the mainPanel to the panel
		panel.add(BorderLayout.CENTER,mainPanel);
		setMidi();
		frame.setBounds(50,50,300,300);
		frame.pack();
		frame.setVisible(true);
		
	}
	//set up the midi event
	private void setMidi() {
		//we set the sequencer sequence and create track
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ, 4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(120);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	//create the track and play it
	private void buildTrackMidi() {
		// TODO Auto-generated method stub
		int[] trackArray = null;
		//loop throw each instrument
		for(int i=0; i<16;i++) {
			trackArray = new int[16];
			//get the key of the instrument
			int key = instruments[i];
			//loop throw each checkbox
			for(int j=0;j<16;j++) {
				//get each checkbox in the checkbox arraylist
				JCheckBox checkBox = (JCheckBox) checkBoxes.get(j+(i*16));
				//test if the checkbox is selected 
				if(checkBox.isSelected()) {//if selected then put the current bit of the key  instrument
					trackArray[j]=key;
				}
			}
			//loop throw the array and create the midi events and load them in the track
			addEventToTrack(trackArray);
		}
		//load an event in the last bit to make sure the bit 16 is reached anyway
		track.add(addMidiEvent(192,9,1,0,16));
		//start the sequencer
		try {
			sequencer.setSequence(sequence);
			//repeat the track continuously
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			sequencer.setTempoInBPM(120);
			sequencer.start();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	private void addEventToTrack(int[] array) {
		for(int i=0;i<array.length;i++) {
			int note = array[i];
			track.add(addMidiEvent(144,9,note,100,i));
			track.add(addMidiEvent(128,9,note,100,i+1));
		}
		
	}
	private MidiEvent addMidiEvent(int command, int channel, int note, int velocity, int tick) {
		MidiEvent event = null;
		
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(command,channel,note,velocity);
			event = new MidiEvent(message,tick);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return event;
	}
	//listener class for start button
	public class MyStartAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			buildTrackMidi();

		}

		

	}
	//listener class for start button
	public class MyStopAction implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			sequencer.stop();
		}
		
	}
	//listener class for start button
	public class MyTempoUpAction implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			float tempo = sequencer.getTempoFactor();
			sequencer.setTempoFactor(tempo*(1.03f));
			
		}
		
	}
	//listener class for start button
	public class MyTempoDownAction implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			float tempo = sequencer.getTempoFactor();
			sequencer.setTempoFactor(tempo*(0.97f));
		}
		
	}
}
