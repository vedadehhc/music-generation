package music;

import java.util.Arrays;
import java.util.List;

public enum Tone {
	C, D, E, F, G, A, B, REST;

	public final static List<Tone> notes = Arrays.asList(C, null, D, null, E, F, null, G, null, A, null, B);

	public final static List<Tone> tones = Arrays.asList(A, B, C, D, E, F, G);

	public int getNoteID() {
		if (this == REST) {
			return -1;
		}
		return notes.indexOf(this);
	}

	public String toString() {
		return "" + (char) (tones.indexOf(this) + 'A');
	}

	public static Tone getToneFromString(String s) {
		int index = (s.charAt(0) - 'A');
		if (index >= 0 && index < tones.size()) {
			return tones.get(index);
		} else {
			return null;
		}
	}
}