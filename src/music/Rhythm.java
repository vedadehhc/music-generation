package music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// Represents a musical rhythm as a list of note durations
// TODO: finish implementation in other SoundPlayer and other classes
public class Rhythm {
	private int bpm; // beats per minute
	private int beatDuration; // in milliseconds
	private ArrayList<Integer> durations; // duration of each note
	
	public Rhythm(int bpm) {
		this.bpm = bpm;
		this.beatDuration = (1000 * 60) / this.bpm;
	}

	public Rhythm() {
		this(120);
	}

	public void addNote(RhythmNote note) {
		durations.add(beatDuration * note.getBeats());
	}

	public void setBPM(int bpm) {
		this.bpm = bpm;
		this.beatDuration = (1000 * 60) / this.bpm;
	}

	static enum RhythmNote {
		Quarter, Half, Whole;

		public static final List<RhythmNote> notes = Arrays.asList(Quarter, Half, Whole);
		public static final List<Integer> beats = Arrays.asList(1, 2, 4);

		public int getBeats() {
			return beats.get(notes.indexOf(this));
		}
	}
}
