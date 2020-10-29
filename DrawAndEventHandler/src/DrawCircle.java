import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class DrawCircle {
	JFrame frame;
	JButton changeText;
	JButton changeColorShape;
	JLabel lable;
	boolean flag;
	public static void main(String[] args) {
		DrawCircle drawCircle = new DrawCircle();
		drawCircle.go();

	}

	private void go() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		changeText = new JButton("Change Text:");
		changeText.addActionListener(new ChangeTextListener());
		frame.getContentPane().add(changeText,BorderLayout.SOUTH);

		changeColorShape = new JButton("Change Color");
		changeColorShape.addActionListener(new ChangeColorShapeListener());
		frame.getContentPane().add(changeColorShape,BorderLayout.EAST);
		
		lable = new JLabel("Change me :p");
		frame.getContentPane().add(lable,BorderLayout.WEST);
		
		JPanel ds = new JPanel() {
		    protected void paintComponent(Graphics g) {
		        Graphics2D g2 = (Graphics2D) g;
		        g.fillRect(0, 0, this.getWidth(), this.getHeight());
		        Color c = new Color(0,0,0);
		        g.setColor(c);
		        
		        int red = (int)(Math.random()*255);
		        int green = (int)(Math.random()*255);
		        int blue = (int)(Math.random()*255);
		        
		        Color color = new Color(red,green,blue);
		        g.setColor(color);
		        g.fillOval(this.getWidth()/2-50, this.getHeight()/2-50,100 , 100);
		        
		    }
		};
		frame.getContentPane().add(ds,BorderLayout.CENTER);
		
		Dimension d = new Dimension(600,600);
		frame.setSize(d);
		frame.setVisible(true);
		
	}
	
	public class ChangeTextListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(flag == false) {
				lable.setText("Oh fuck you changed me");
				flag = true;
			}else {
				lable.setText("Change me :p");
				flag = false;
			}
			

		}

	}
	
	public class ChangeColorShapeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.repaint();

		}

	}
	

}
/*
class DrawShapes extends JPanel{
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        Color c = new Color(0,0,0);
        g.setColor(c);
        
        int red = (int)(Math.random()*255);
        int green = (int)(Math.random()*255);
        int blue = (int)(Math.random()*255);
        
        Color color = new Color(red,green,blue);
        g.setColor(color);
        g.fillOval(this.getWidth()/2-50, this.getHeight()/2-50,100 , 100);
        
    }
}
*/
