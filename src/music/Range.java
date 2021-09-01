package music;

// Represents a range of musical notes. Useful for ensuring that a part stays within its range.
public class Range {

	
	// Bass, Tenor, Alto, and Soprano voice Ranges
	public static final Range[] voiceRanges = { new Range(40, 57), new Range(48, 67), new Range(55, 72),
			new Range(60, 79) };

	private int minID, maxID;

	public Range(int minID, int maxID) {
		if (minID > maxID) {
			this.minID = maxID;
			this.maxID = minID;
		} else {
			this.minID = minID;
			this.maxID = maxID;
		}
	}

	public Range(Note lowestNote, Note highestNote) {
		this(lowestNote.getID(), highestNote.getID());
	}
	
	// returns -1 if note is below range, 0 if note is in range, and 1 if note is above range
	public int checkNote(Note note) {
		if(note.getID() < minID) return -1;
		if(note.getID() > maxID) return 1;
		return 0;
	}

	public Note[] getInKey(Key key) {

		int lowest = 0;
		while (key.getNote(lowest).getID() > minID) {
			lowest -= 1;
		}
		while (key.getNote(lowest).getID() < minID) {
			lowest += 1;
		}

		int highest = 0;
		while (key.getNote(highest).getID() < maxID) {
			highest += 1;
		}
		while (key.getNote(highest).getID() > maxID) {
			highest -= 1;
		}

		Note[] range = new Note[highest - lowest + 1];

		for (int i = lowest; i <= highest; i++) {
			range[i - lowest] = key.getNote(i);
		}

		return range;
	}

	public int getMinID() {
		return minID;
	}

	public int getMaxID() {
		return maxID;
	}

}
