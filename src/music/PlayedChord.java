package music;

import java.util.ArrayList;

public class PlayedChord {
	
	private ArrayList<PlayedNote> notes;
	private int duration;
	
	public PlayedChord(int duration) {
		notes = new ArrayList<PlayedNote>();
		this.duration = duration;
	}
	
	public PlayedChord() {
		this(500);
	}
	
	public PlayedChord shiftOctave(int amt) {
		PlayedChord chord = new PlayedChord(duration);
		for(PlayedNote note : notes) {
			chord.addNote(note.shiftOctave(amt));
		}
		return chord;
	}
	
	// TODO: implement recursively
	public PlayedChord invert(int inversions) {
		if(inversions <= 0) {
			return this;
		}
		if(inversions >= notes.size()) {
			return this;
		}
		
		PlayedChord chord = new PlayedChord(this.duration);
		
		for(int i = inversions; i < notes.size(); i++) {
			chord.addNote(notes.get(i));
		}
		for(int i =0; i < inversions; i++) {
			chord.addNote(notes.get(i).shiftOctave(1));
		}
		
		return chord;
	}
	
	public void addNote(PlayedNote note) {
		notes.add(note);
	}
	
	public ArrayList<PlayedNote> getNotes() {
		return notes;
	}

	public int getDuration() {
		return duration;
	}
}
