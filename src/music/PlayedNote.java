package music;

public class PlayedNote {
	private Note note;
	private int duration, volume; // Milliseconds, [0, 127]

	public PlayedNote(Tone tone, Shift shift, int octave, int duration, int volume) {
		note = new Note(tone, shift, octave);
		this.duration = duration;
		this.volume = volume;
	}
	
	public PlayedNote(Tone tone, Shift shift, int octave) {
		this(tone, shift, octave, 500, 80);
	}
	
	public PlayedNote(Tone tone, int octave) {
		this(tone, Shift.Natural, octave);
	}
	
	public PlayedNote(Note note, int duration, int volume) {
		this(note.getTone(), note.getShift(), note.getOctave(), duration, volume);
	}
	
	public PlayedNote(Note note) {
		this(note, 500, 80);
	}
	
	public PlayedNote shiftOctave(int amt) {
		PlayedNote playedNote = new PlayedNote(note.shiftOctave(amt), duration, volume);
		return playedNote;
	}
	
	public Note getNote() {
		return note;
	}
	
	public int getID() {
		return note.getID();
	}

	public int getDuration() {
		return duration;
	}

	public int getVolume() {
		return volume;
	}
}
