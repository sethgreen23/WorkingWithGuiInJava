import java.awt.event.*;

import javax.swing.*;

public class FirstEventHandler implements ActionListener{
	JFrame frame ;
	JButton button;
	boolean flag = false;
	public static void main(String[] args) {
		FirstEventHandler m = new FirstEventHandler();
		m.go();

	}
	public void go() {
		// create frame
		frame = new JFrame();
		//create and add button
		button = new JButton("Click me?");
		//add even listener to the button
		button.addActionListener(this);
		//add the button to the pane
		frame.getContentPane().add(button);
		//set a size to the frame
		frame.setSize(300,300);
		//make the frame visible
		frame.setVisible(true);
}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(flag == false) {
			button.setText("You clicked Lad ;)");
			flag = true;
		}else {
			button.setText("Click me ?");
			flag = false;
		}
		
	}

}
