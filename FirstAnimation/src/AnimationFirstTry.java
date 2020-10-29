import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class AnimationFirstTry {
	
	JFrame frame;
	int height=30;
	int width=30;
	int x=0;
	int y=0;
	JPanel p;
	public static void main(String[] args) {
		AnimationFirstTry first= new AnimationFirstTry();
		first.go();
	}

	private void go() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p = new JPanel() {
			public void paintComponent(Graphics e) {
				Graphics2D g = (Graphics2D) e;
				Paint p = new GradientPaint(0, 0, Color.red, this.getWidth(), this.getHeight(), Color.pink, true); 
//				g.setColor(Color.pink);  //we can use it too
				g.setPaint(p); 
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				Paint p1 = new GradientPaint(0,0,Color.green,width,height,Color.yellow,true);
				g.setPaint(p1);
				g.fillOval(x, y, width, height);
				
			}
		};
		frame.getContentPane().add(p,BorderLayout.CENTER);

		
		frame.setSize(400,400);
		frame.setVisible(true);

		for(int i=0;i<frame.getHeight();i++) {
			x++;
			y++;
			p.repaint();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
	

}
