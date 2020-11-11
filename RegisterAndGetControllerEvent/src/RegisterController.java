import javax.sound.midi.*;

public class RegisterController implements ControllerEventListener{
	static int value = 1;
	public static void main(String[] args) {
		RegisterController rc = new RegisterController();
		rc.go();
	}
	public void go() {
		try {
			//create and add sequencer
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			int[] numIWant = {127};
			int value=numIWant[0];
			sequencer.addControllerEventListener(this, numIWant);
			//create sequence
			Sequence sq = new Sequence(Sequence.PPQ,4);
			//create track
			Track track = sq.createTrack();
			for(int i=5;i<125;i+=4) {
				track.add(makeEvent(144,1,i,100,i));
				track.add(makeEvent(176,1,127,100,0));
				track.add(makeEvent(128,1,i,100,i+2));
			}
			sequencer.setSequence(sq);
			sequencer.setTempoInBPM(220);
			sequencer.start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static MidiEvent makeEvent(int command,int channel, int note, int velocity, int tick) throws InvalidMidiDataException {
		MidiEvent event = null;
		
		ShortMessage message = new ShortMessage();
		message.setMessage(command, channel, note, velocity);
		event = new MidiEvent(message,tick);
		return event;
	}

	@Override
	public void controlChange(ShortMessage event) {
		System.out.println("la "+ value++);
		
	}

}
