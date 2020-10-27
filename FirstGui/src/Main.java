import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		//make a frame
		JFrame frame = new JFrame();
		//create and add a button to frame
		JButton button = new JButton("Hello");
		frame.getContentPane().add(button);
		//add listener to the closing button
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//give a size to the frame
		frame.setSize(300,300);
		//make the frame visible
		frame.setVisible(true);
	}
}
