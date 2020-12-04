import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;


public class QuickCardBuilder {
	

	JFrame frame;
	JPanel mainPanel;
	JTextArea questionTextArea;
	JTextArea answerTextArea;
	ArrayList<QuickCard> cardList = new ArrayList<QuickCard>();
	
//	public static void main(String[] args) {
//		QuickCardBuilder builder = new QuickCardBuilder();
//		builder.go();
//
//	}

	private void go() {
		//create frame
		frame = new JFrame("Quick Card Builder");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		//create mainpanel
		mainPanel = new JPanel();
		//mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS)); //we can add the box layout to the panel (1)
             
		
		// create a line border with the specified color and width   
        //Border border = BorderFactory.createLineBorder(Color.BLUE, 5);//we can add border to the labels (2)
        
        //create the Jlabes
		JLabel questionLabel = new JLabel("Question:");
		//questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // set the border of this component
        //questionLabel.setBorder(border);// using border (2)
        
        //create the Jlabes
        JLabel answerLabel = new JLabel("Answer:  ");
        //answerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // set the border of this component
        //answerLabel.setBorder(border); //using border(2)
        
        //create font
		//create the JTextArea
        questionTextArea = new JTextArea("", 10, 30);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setFont(new Font("sans-serif",Font.BOLD,16));
        JScrollPane questionScrollPane = new JScrollPane(questionTextArea);
        questionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        questionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        answerTextArea = new JTextArea("", 10, 30);
        answerTextArea.setLineWrap(true);
        answerTextArea.setWrapStyleWord(true);        
        answerTextArea.setFont(new Font("sans-serif",Font.BOLD,16));
        JScrollPane answerScrollPane = new JScrollPane(answerTextArea);
        answerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        answerScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//create the button
        JButton nextQuestion = new JButton("Next Card");
       // nextQuestion.setAlignmentX(Component.CENTER_ALIGNMENT);
        //add actionlistener to the button
        nextQuestion.addActionListener(new NextButtonListener());
		//create the menu
        JMenuBar menuBar=new JMenuBar();  
        JMenu menu=new JMenu("File"); 
        JMenuItem newButton =new JMenuItem("New");  
        JMenuItem saveButton =new JMenuItem("Save"); 
        //add action listener to the buttons of the menu
        newButton.addActionListener(new NewMenuListener());
        saveButton.addActionListener(new SaveMenuListener());
        menu.add(newButton);
        menu.add(saveButton);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
		//add the elements to its propers container
		mainPanel.add(questionLabel);
		mainPanel.add(questionScrollPane);
		mainPanel.add(answerLabel);
		mainPanel.add(answerScrollPane);
		mainPanel.add(nextQuestion);
		frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
		frame.setSize(500,600);
		frame.setVisible(true);
		
		
		
		//setvisible the frame and give it a bouns
		
		
	}
	
	
	//function part
	private void addCard(boolean isOpen) {
		String question ,answer;
		question = questionTextArea.getText().trim();
		answer = answerTextArea.getText().trim();
		
		if( !question.equals("") && !answer.equals("")) {
			QuickCard card = new QuickCard(question,answer);
			cardList.add(card);
			clearFields();
		}else {
			//put an alert when the fields are empty
			if(isOpen)
				JOptionPane.showMessageDialog(frame, "Please enter the card details.");
		}
		
	}

	private void clearFields() {
		questionTextArea.setText("");
		answerTextArea.setText("");
		//put the focus on the question textarea
		questionTextArea.requestFocusInWindow();
		//traverse the carslist
		//traverse(cardList);
	}

	private void traverse(ArrayList<QuickCard> cardList) {
		System.out.println("//////////////////////////");
		for(QuickCard card : cardList) {
			System.out.println("Question: "+card.getQuestion()+"\nAnswer: "+card.getAnswer());
		}
		
	}
	
	//action listener of the new button
	public class NewMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			clearFields();
			cardList.clear();
		}

	}
	
	//action listener of the save button
	public class SaveMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			addCard(false);
			JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int returnVal = fileChooser.showSaveDialog(frame);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
			      File file =  fileChooser.getSelectedFile();
			      System.out.println(file.getName());
			      saveInFile(file);
		    }

		}

		private void saveInFile(File file) {
			
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				for(QuickCard card : cardList) {
					String question = card.getQuestion();
					String answer = card.getAnswer();
					System.out.println(question+"/"+answer+"\n");
					writer.write(question+"/"+answer+"\n");
				}
				writer.close();
				clearFields();
				cardList.clear();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
	
	//action listener of the next button
	public class NextButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			addCard(true);
			
		}


		
	}

}
