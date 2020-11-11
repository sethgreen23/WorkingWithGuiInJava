import java.awt.*;
import javax.sound.midi.*;
import javax.swing.*;
public class GraphicsWithMusic{
	//make a static frame that we can
	static JFrame frame = new JFrame("Graphics with Music");
	static DrawGraphicsMusic gm;
	public static void main(String[] args) {
		//create instance from the Main class and execute the go() method
		GraphicsWithMusic gwm = new GraphicsWithMusic();
		gwm.go();
	}
	

	private void go() {
		setup();
		try {
			Sequencer sequencer;
			//create and open sequencer
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			//add a controller event listener how listen to 
			//the playing of every note
			sequencer.addControllerEventListener(gm, new int[] {127});
			Sequence sq = new Sequence(Sequence.PPQ, 4);
			
			Track track = sq.createTrack();
			int note =0;
			for(int i=5;i<127;i+=4) {
				note = (int)(Math.random()*127);
				track.add(makeEvent(144,1,note,100,i));
				//we incorporate the controle event commande "176" in the beggining
				//of every note that we can listen to it we the note is played and
				//with that we can send notification to the DrawGraphicsMusic instance
				//after that we repaint the panel with a random rectangle
				track.add(makeEvent(176,1,127,0,i));
				track.add(makeEvent(128,1,note,100,i+2));
			}
			
			sequencer.setSequence(sq);
			sequencer.setTempoInBPM(120);
			sequencer.start();
			while(true) {
				if(!sequencer.isRunning()) {
					sequencer.close();
					//System.exit(1);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private MidiEvent makeEvent(int cmd, int channel, int note, int velocity, int tick) {
		MidiEvent event =null;
		//this is the process of creation of midiEvent
		try {
			ShortMessage msg = new ShortMessage();
			msg.setMessage(cmd, channel, note, velocity);
			event = new MidiEvent(msg,tick);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return event;
	}


	private void setup() {
		//add the even that close the frame
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//create a drawing panel and add it to the frame in the center
		gm=new DrawGraphicsMusic();
		frame.getContentPane().add(gm,BorderLayout.CENTER);
		//set the bounderies of the frame and make it visible
		frame.setBounds(30, 30, 600, 600);
		frame.setVisible(true);
		
	}


	public class DrawGraphicsMusic extends JPanel implements ControllerEventListener{
		// boolean varible to controle the drawing of rectangle 
		//related to the playing of every note
		boolean msg = false;
		//boolean variable how to controle the filling 
		//of the frame just one time in the begging just 
		boolean flag = true;
		public void paintComponent(Graphics e) {
			Graphics2D g= (Graphics2D) e;
			//fill the frame with white color just on time
			if(flag) {
				g.setColor(Color.white);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				flag=false;
			}
			//everytime the sequencer read a note we create a rectangle in the same time
			if(msg) {
				int r = (int)(Math.random()*255);
				int gr = (int)(Math.random()*255);
				int b = (int)(Math.random()*255);
				g.setColor(new Color(r,gr,b));
				int width = (int)(Math.random()*120);
				int height = (int)(Math.random()*120);
				int x = (int)(Math.random()*400);
				int y = (int)(Math.random()*400);
				g.fillRect(x, y, width, height);
			}

		}

		@Override
		public void controlChange(ShortMessage event) {
			//here we repaint the drawing pannel everytime we listen to a note reading by the sequencer
			msg = true;
			repaint();
		}

	}
}