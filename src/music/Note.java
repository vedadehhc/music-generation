package music;

public class Note {

	// Returns a Note with the given MIDI id. This method tries to use the preferred
	// shift, but if it can't, uses the closest shift instead.
	public static Note getNoteFromID(int id, Shift prefShift) {
		int shiftNum = prefShift.getShiftID();

		int octave = (id / 12) - 1;
		int toneMod = (id - shiftNum + 12) % 12;

		Tone tone = Tone.notes.get(toneMod);
		int shiftID = shiftNum + 2;

		if (tone == null) {
			if (shiftNum <= 0) {
				tone = Tone.notes.get((toneMod + 11) % 12);
				shiftID++;
			} else {
				tone = Tone.notes.get((toneMod + 1) % 12);
				shiftID--;
			}
		}

		Note note = new Note(tone, Shift.shifts.get(shiftID), octave);
		return note;
	}

	public static Note getNoteFromString(String s) {
		return new Note(s);
	}

	private Tone tone;
	private Shift shift;
	private int octave;

	public Note(Tone tone, Shift shift, int octave) {
		this.tone = tone;
		this.shift = shift;
		this.octave = octave;
	}

	public Note(Tone tone, int octave) {
		this(tone, Shift.Natural, octave);
	}
	
	public Note(String s) {
		tone = Tone.getToneFromString(s.substring(0, 1));

		shift = Shift.Natural;
		int end = 1;

		if (s.length() >= 3 && Shift.getShiftFromString(s.substring(1, 3)) != null) {
			shift = Shift.getShiftFromString(s.substring(1, 3));
			end = 3;
		} else if (Shift.getShiftFromString(s.substring(1, 2)) != null) {
			shift = Shift.getShiftFromString(s.substring(1, 2));
			end = 2;
		}
		
		octave = Integer.parseInt(s.substring(end));
	}

	public Note shiftOctave(int amt) {
		return new Note(tone, shift, octave + amt);
	}

	// Returns the MIDI id for this note
	public int getID() {
		if (tone.getNoteID() < 0) {
			return -1;
		}

		return (tone.getNoteID() + shift.getShiftID()) + (12 * octave) + 12;
	}

	public Tone getTone() {
		return tone;
	}

	public Shift getShift() {
		return shift;
	}

	public int getOctave() {
		return octave;
	}

	public PlayedNote toPlayedNote() {
		return new PlayedNote(this);
	}

	public PlayedNote toPlayedNote(int duration, int volume) {
		return new PlayedNote(this, duration, volume);
	}

	public boolean equals(Note oth) {
		return getID() == oth.getID();
	}

	public boolean equalsIgnoreOctave(Note oth) {
		if (getID() < 0 || oth.getID() < 0) {
			return false;
		}
		int diff = Math.abs(getID() - oth.getID());
		return (diff % 12 == 0);
	}

	public String toString() {
		return tone.toString() + shift.toString() + octave;
	}

}
