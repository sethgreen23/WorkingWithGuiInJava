import javax.sound.midi.*;
import java.util.*;
public class Simplify {

	public static void main(String[] args) {
		Simplify s = new Simplify();
		s.go();
	}
	public void go() {
		
		try {
			//make and open sequencer
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			
			//create sequence
			Sequence sq = new Sequence(Sequence.PPQ, 4);
			
			//make track from the sequencer
			Track track = sq.createTrack();
			for(int i=1;i<127;i+=4) {
				// Add Note On event 
                track.add(makeEvent(144, 1, i, 100, i)); 
                //make a controleEventListener
                //track.add(makeEvent(176,1,127,0,i));
                // Add Note Off event 
                track.add(makeEvent(128, 1, i, 100, i + 2)); 
			}

            // Setting our sequence so that the sequencer can 
            // run it on synthesizer
			sequencer.setSequence(sq);
			 // Specifies the beat rate in beats per minute. 
			sequencer.setTempoInBPM(220);
			//sequencer start play notes
			sequencer.start();
			
			//Exit program when sequencer stop playing
			while(true) {
				if(!sequencer.isRunning()) {
					sequencer.close();
					System.exit(0);
				}
			}
			
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(InvalidMidiDataException e) {
			e.printStackTrace();
		}

	}
	public static MidiEvent makeEvent(int command,int channel,int note,int velocity,int tick) throws InvalidMidiDataException {
		MidiEvent midiEvent = null;
		ShortMessage message = new ShortMessage();
		message.setMessage(command, channel, note, velocity);
		midiEvent = new MidiEvent(message,tick);
		
		return midiEvent;
	}
}
