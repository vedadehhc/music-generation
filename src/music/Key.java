package music;

import java.util.Arrays;
import java.util.List;

/**
 * The <tt>Key</tt> class is used to store and use musical keys. 
 * @author Dev Chheda
 */
public class Key {

	public static enum KeyType {
		Major, NaturalMinor, HarmonicMinor;
	}

	private final static List<Integer> majorTemplate = Arrays.asList(2, 2, 1, 2, 2, 2, 1);
	private final static List<Integer> naturalMinorTemplate = Arrays.asList(2, 1, 2, 2, 1, 2, 2);
	private final static List<Integer> harmonicMinorTemplate = Arrays.asList(2, 1, 2, 2, 1, 3, 1);
	
	private Note tonic;
	private Note[] scale;
	private Chord[][] chords;

	private List<Integer> template;

	public Key(Note tonic, KeyType type) {
		this.tonic = tonic;

		switch (type) {
		case Major:
			template = majorTemplate;
			break;
		case NaturalMinor:
			template = naturalMinorTemplate;
			break;
		case HarmonicMinor:
			template = harmonicMinorTemplate;
			break;
		default:
			template = majorTemplate;
		}

		int numNotes = 7;

		scale = new Note[numNotes];
		chords = new Chord[2][numNotes];

		scale[0] = tonic;
		int octave = tonic.getOctave();

		int startingPoint = Tone.tones.indexOf(tonic.getTone());
		for (int i = startingPoint + 1, j = 1; i != startingPoint && j < scale.length; i++, j++) {
			if (i >= Tone.tones.size()) {
				i -= Tone.tones.size();
			}
			if (Tone.tones.get(i) == Tone.C) {
				octave++;
			}

			Note note = new Note(Tone.tones.get(i), octave);
			Shift shift = Shift.Natural;

			int diff = (note.getID() - scale[j - 1].getID()) - template.get(j - 1);
			switch (diff) {
			case -2:
				shift = Shift.DoubleSharp;
				break;
			case -1:
				shift = Shift.Sharp;
				break;
			case 1:
				shift = Shift.Flat;
				break;
			case 2:
				shift = Shift.DoubleFlat;
				break;
			default:
				shift = Shift.Natural;
			}

			scale[j] = new Note(Tone.tones.get(i), shift, octave);
		}

		for (int i = 0; i < chords[0].length; i++) {
			Chord chord = new Chord();
			chord.addNote(getNote(i + 1));
			chord.addNote(getNote(i + 3));
			chord.addNote(getNote(i + 5));
			chords[0][i] = chord;

			Chord chord7th = new Chord();
			chord7th.addNote(getNote(i + 1));
			chord7th.addNote(getNote(i + 3));
			chord7th.addNote(getNote(i + 5));
			chord7th.addNote(getNote(i + 7));
			chords[1][i] = chord7th;
		}
	}

	public Key(Note tonic) {
		this(tonic, KeyType.Major);
	}

	// [1, 7]
	public Note getNote(int scalePos) {
		if (scalePos > scale.length) {
			int amt = (scalePos - 1) / scale.length;
			return scale[scalePos - 1 - scale.length * amt].shiftOctave(amt);
		}
		if (scalePos <= 0) {
			int amt = ((scalePos) / scale.length) - 1;
			return scale[scalePos - 1 - scale.length * amt].shiftOctave(amt);
		}

		return scale[scalePos - 1];
	}

	public Chord getChord(int rootScalePos) {
		return getChord(rootScalePos, false);
	}

	public Chord getChord(int rootScalePos, boolean is7th) {
		return getChord(rootScalePos, is7th, 0);
	}

	public Chord getChord(int rootScalePos, boolean is7th, int inversion) {
		int type = is7th ? 1 : 0;

		if (rootScalePos > chords[type].length) {
			int amt = (rootScalePos - 1) / chords[type].length;
			return chords[type][rootScalePos - 1 - chords[type].length * amt].shiftOctave(amt).invert(inversion);
		}

		if (rootScalePos <= 0) {
			int amt = ((rootScalePos) / chords[type].length) - 1;
			return chords[type][rootScalePos - 1 - chords[type].length * amt].shiftOctave(amt).invert(inversion);
		}

		return chords[type][rootScalePos - 1].invert(inversion);
	}

	// Returns an inverted chord based on the bass scale degree and the inversion of
	// the chord
	public Chord getChordFromBass(int bassScalePos, boolean is7th, int inversion) {

		int root = bassScalePos;

		switch (inversion) {
		case 0:
			root = bassScalePos;
			break;
		case 1:
			root = bassScalePos - 2;
			break;
		case 2:
			root = bassScalePos - 4;
			break;
		case 3:
			root = bassScalePos - 6;
			break;
		default:
			return null;
		}

		return getChord(root, is7th, inversion);
	}

	// Returns a secondary dominant chord in the key of the given scale degree,
	// with the given key type, and the given root position (in the new key)
	public Chord getSecondaryChord(int keyScalePos, KeyType chordType, int rootScalePos) {
		Note root = getNote(keyScalePos);
		Key newKey = new Key(root, chordType);
		return newKey.getChord(rootScalePos);
	}

	public int getScaleLength() {
		return scale.length;
	}

	public Note[] getScale() {
		return scale;
	}

	public String toString() {
		return tonic.toString() + " Major";
	}
}
