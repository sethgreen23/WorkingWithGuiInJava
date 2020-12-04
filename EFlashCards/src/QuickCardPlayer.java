import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class QuickCardPlayer {


	JFrame frame;
	JPanel mainPanel;
	JTextArea display;
	boolean isShownAnswer;
	ArrayList<QuickCard> cardList;
	int currentCardIndex;
	QuickCard currentCard;
	JButton nextButton;
	boolean isLoaded=false;
	public static void main(String[] args) {
		QuickCardPlayer player = new QuickCardPlayer();
		player.go();

	}

	private void go() {
		//create a frame
		frame = new JFrame("Quick Card Player");
		//create mainPanel
		mainPanel = new JPanel();
		//instantiate the textArea
		display = new JTextArea("",10,30);
		display.setFont(new Font("sans-serif",Font.BOLD,16));//set font
		display.setLineWrap(true);//set wrap new line
		display.setWrapStyleWord(true);//set it to wrap the words
		display.setEditable(false);//we cant edit it
		//create scroll pane of the TextArea
		JScrollPane dScroll = new JScrollPane(display);
		dScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);//set verticall scroll
		dScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);//disaple horizontal scroll
		//create the button
		nextButton = new JButton("Show Question");
		nextButton.addActionListener(new NextQuestionListener());//add listener to the button
		//add the TextArea and button to the panel
		mainPanel.add(dScroll);
		mainPanel.add(nextButton);
		//create the menubar and its item 
		JMenuBar menuBar = new JMenuBar();//create menubar
		JMenu menu = new JMenu("File");//create menu
		JMenuItem load = new JMenuItem("Load");//create the load button
		load.addActionListener(new LoadCardListener());//add listener to the load button
		//add load button to the menu
		menu.add(load);
		//add the menu to the menubar
		menuBar.add(menu);
		//set the frame menubar to the one that we created
		frame.setJMenuBar(menuBar);
		//add the mainPanel to the center of the frame
		frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
		
		frame.setSize(500,350);
		frame.setVisible(true);
	}

	
	
	//next button listener
	public class NextQuestionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//we do proced if we loaded a card list
			if(isLoaded) {
				//if we clicked on show answer
				if(isShownAnswer) {
					String answer = currentCard.getAnswer();//get the answer of the current card
					display.setText(answer);//set the text of diplay to the answer
					nextButton.setText("Show Question");//change the text of the button to showquestion
					isShownAnswer=false;//now we prepere the clicking on show question
				}else {
					//if we pressed on show question button
					//check if we reached the last element of the cardlist 
					if(currentCardIndex<cardList.size()) {//if we didnt reach the end of the cardlist
						showQuestion();//show the question
					}else {//if we reached the last element in the cardlist
						display.setText("We reached the end of the cardset");//in the display textarea we show message
						nextButton.setText("End Card Set");//we set the text of the button
						nextButton.setEnabled(false);//we disable the button for further pushing
					}
				}

			}else {//if not we show a message
				JOptionPane.showMessageDialog(frame, "Please Load a Card Set");
			}

		}

		private void showQuestion() {
			//we get the current card
			currentCard = cardList.get(currentCardIndex);
			//we increment the count to the next card in the list
			currentCardIndex++;
			//we get the question from the cardlist
			String question = currentCard.getQuestion();
			//display the qustion in the text area
			display.setText(question);
			//change the button to show answer
			nextButton.setText("Show Answer");
			//we prepare pushing on the show answer
			isShownAnswer = true;
			
		}

	}
	//load button listener
	public class LoadCardListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//we load the dialogue for opening a file
			JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int returnVal = chooser.showOpenDialog(frame);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	//we get the file we choosed
		       File file = chooser.getSelectedFile();
		       //we feed the file to the load method
		       loadCardsList(file);
		    }
		    isLoaded=true;
		}

		private void loadCardsList(File file) {
			try {
				//we create a bufferreader to load the data from the file
				BufferedReader reader = new BufferedReader(new FileReader(file));
				//inialise the line to null
				String line = null;
				//instantiate the cardlist to hold the object stored in the file
				cardList = new ArrayList<QuickCard>();
				//loop through the file till we read all the lines
				while((line=reader.readLine())!=null) {
					//we take each line and we split it
					String[] lineArray = line.split("/");
					//we make object from the data collected
					QuickCard card =makeCard(lineArray);
					//we add the card to the cardlist
					cardList.add(card);
				}
				//after finishing the reading we close the buffer and it will close by itself the filereader
				reader.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		private QuickCard makeCard(String[] lineArray) {
			//create an object from the array element and return it back
			QuickCard card = new QuickCard(lineArray[0],lineArray[1]);
			return card;
			
		}
		
	}
}
