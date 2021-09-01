package music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// TODO: Add key transposition
/**
 * The <tt>Chord</tt> class is used to store and manipulate chords, or groups of notes. 
 * Each chord is represented by a list of <tt>Note</tt> objects.
 * <tt>Chord</tt> objects cannot be played directly using <tt>SoundPlayer</tt> but should be converted 
 * to <tt>PlayedChord</tt> objects instead.
 * @author Dev Chheda
 * @see music.Note Note 
 * @see music.PlayedChord PlayedChord
 * @see soundPlayer.SoundPlayer SoundPlayer
 */
public class Chord {
	
	/**
	 * A permanent list of the Roman numerals for diatonic chords in the Major scale.
	 */
	public static final List<String> romanNumeralChords = Arrays.asList("I", "ii", "iii", "IV", "V", "vi", "viio");
	
	/**
	 * The <tt>Note</tt> objects stored in this <tt>Chord</tt> object.
	 * @see music.Note Note
	 */
	private ArrayList<Note> notes;
	
	
	/**
	 * The default constructor for the <tt>Chord</tt> class. This initializes the <tt>Chord</tt> with no notes.
	 * @see #Chord(ArrayList)
	 * @see #Chord(Note[])
	 */
	public Chord() {
		notes = new ArrayList<Note>();
	}
	
	/**
	 * A constructor for the <tt>Chord</tt> class which initializes the chord with a given list of <tt>Note</tt>.
	 * @param noteArr - the list of <tt>Note</tt> to be stored in this <tt>Chord</tt>
	 * @see #Chord()
	 * @see #Chord(Note[])
	 * @see music.Note Note
	 */
	public Chord(ArrayList<Note> noteArr) {
		this();
		for(Note n: noteArr) {
			notes.add(n);
		}
	}
	
	/**
	 * A constructor for the <tt>Chord</tt> class which initializes the chord with a given <tt>Note</tt> array.
	 * @param noteArr - the list of <tt>Note</tt> to be stored in this <tt>Chord</tt>
	 * @see #Chord()
	 * @see #Chord(ArrayList)
	 * @see music.Note Note
	 */
	public Chord(Note[] noteArr) {
		this();
		for(Note n : noteArr) {
			notes.add(n);
		}
	}
	
	/**
	 * Returns a new <tt>Chord</tt> identical to this one, but with each of its notes shifted by <tt>amt</tt> 
	 * octaves. Positive valules of <tt>amt</tt> shift the chord up, and negative values shift the chord down.
	 * @param amt - the number of octaves to shift the chord by.
	 * @return A new <tt>Chord</tt> with all of its notes shifted by some number of octaves.
	 */
	public Chord shiftOctave(int amt) {
		Chord chord = new Chord();
		for(Note note : notes) {
			chord.addNote(note.shiftOctave(amt));
		}
		return chord;
	}
	
	// TODO: implement recursively
	/**
	 * Returns a new <tt>Chord</tt> by inverting the current <tt>Chord<\tt>. 
	 * This method does not accept negative inversions, or inversions greater than the size of the chord.
	 * @param inversions - the number of inversions to make on the current <tt>Chord</tt>
	 * @return A new <tt>Chord</tt> with inverted notes.
	 */
	public Chord invert(int inversions) {
		if(inversions <= 0) {
			return this;
		}
		if(inversions >= notes.size()) {
			return this;
		}
		
		Chord chord = new Chord();
		
		for(int i = inversions; i < notes.size(); i++) {
			chord.addNote(notes.get(i));
		}
		for(int i =0; i < inversions; i++) {
			chord.addNote(notes.get(i).shiftOctave(1));
		}
		
		return chord;
	}
	
	
	/**
	 * Add a note to this <tt>Chord</tt>.
	 * @param note - the <tt>Note</tt> to add to this chord.
	 * @see #getNotes getNotes()
	 * @see #getNotesAsArr getNotesAsArr()
	 */
	public void addNote(Note note) {
		notes.add(note);
	}
	
	/**
	 * Get the notes of this <tt>Chord</tt>.
	 * @return An <tt>ArrayList&lt;Note&gt;</tt> containing the notes in this chord.
	 * @see #getNotesAsArr getNotesAsArr()
	 * @see #addNote addNote()
	 */
	public ArrayList<Note> getNotes() {
		return notes;
	}
	
	/**
	 * Get the notes of this <tt>Chord</tt> as a <tt>Note</tt> array.
	 * @return A <tt>Note[]</tt> containing the notes in this chord.
	 * @see #getNotes getNotes()
	 * @see #addNote addNote()
	 */
	public Note[] getNotesAsArr() {
		Note[] arr=  new Note[notes.size()];
		arr = notes.toArray(arr);
		return arr;
	}
	
	/**
	 * Check if this <tt>Chord</tt> contains the given <tt>Note</tt>.
	 * @param note - the <tt>Note</tt> to check for.
	 * @return <tt>True</tt> if <tt>note</tt> is in this chord, and <tt>False</tt> otherwise.
	 * @see #containsNoteIgnoreOctave(Note)
	 * @see #containsChord(Chord)
	 * @see #containsChordIgnoreOctave(Chord)
	 */
	public boolean containsNote(Note note) {
		for(Note n: notes) {
			if(n.equals(note)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if this <tt>Chord</tt> contains the given <tt>Note</tt>, ignoring octaves.
	 * @param note - the <tt>Note</tt> to check for.
	 * @return <tt>True</tt> if <tt>note</tt> at some octave is in this chord, and <tt>False</tt> otherwise.
	 * @see #containsNote(Note)
	 * @see #containsChord(Chord)
	 * @see #containsChordIgnoreOctave(Chord)
	 */
	public boolean containsNoteIgnoreOctave(Note note) {
		for(Note n: notes) {
			if(n.equalsIgnoreOctave(note)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Converts this <tt>Chord</tt> to a <tt>PlayedChord</tt>, which can be played using the <tt>SoundPlayer</tt>.
	 * Equivalent to {@link #toPlayedChord(int, int) toPlayedChord(duration, 80)}.
	 * @param duration - how long, in milliseconds the chord should be played.
	 * @return A <tt>PlayedChord</tt> with the same notes as this <tt>Chord</tt> 
	 * and the specified <tt>duration</tt>.
	 * @see #toPlayedChord(int, int)
	 * @see PlayedChord
	 * @see soundPlayer.SoundPlayer
	 */
	public PlayedChord toPlayedChord(int duration) {
		PlayedChord chord = new PlayedChord(duration);
		for(Note note: notes) {
			chord.addNote(note.toPlayedNote(duration, 80));
		}
		return chord;
	}
	
	/**
	 * Converts this <tt>Chord</tt> to a <tt>PlayedChord</tt>, which can be played using the <tt>SoundPlayer</tt>.
	 * @param duration - how long, in milliseconds the chord should be played.
	 * @param volume - how loud, on a scale from 0 to 127, the chord should be played. 
	 * @return A <tt>PlayedChord</tt> with the same notes as this <tt>Chord</tt> 
	 * and the specified <tt>duration</tt>and <tt>volume</tt>.
	 * @see #toPlayedChord(int)
	 * @see PlayedChord
	 * @see soundPlayer.SoundPlayer
	 */
	public PlayedChord toPlayedChord(int duration, int volume) {
		PlayedChord chord = new PlayedChord(duration);
		for(Note note: notes) {
			chord.addNote(note.toPlayedNote(duration, volume));
		}
		return chord;
	}
	
	// Are all notes in other also in this chord?
	/**
	 * Checks if all the notes in a given <tt>Chord</tt> are also in this chord.
	 * @param oth - the other <tt>Chord</tt> to compare with.
	 * @return <tt>True</tt> if all the notes in <tt>oth</tt> are also in this chord, and <tt>False</tt> otherwise.
	 * @see #containsChordIgnoreOctave(Chord)
	 * @see #containsNote(Note)
	 * @see #containsNoteIgnoreOctave(Note)
	 */
	public boolean containsChord(Chord oth) {
		for(Note n1: oth.notes) {
			boolean pres = false;
			for(Note n2 : notes) {
				if(n1.equals(n2)) {
					pres = true;
					break;
				}
			}
			if(!pres) {
				return false;
			}
		}
		
		return true;
	}

	// Are all notes in other also in this chord (ignoring octaves)?
	/**
	 * Checks if all the notes in a given <tt>Chord</tt> are also in this chord, ignoring octaves.
	 * @param oth - the other <tt>Chord</tt> to compare with.
	 * @return <tt>True</tt> if all the notes in <tt>oth</tt> are also in this chord at some octave,
	 *  and <tt>False</tt> otherwise.
	 * @see #containsChord(Chord)
	 * @see #containsNote(Note)
	 * @see #containsNoteIgnoreOctave(Note)
	 */
	public boolean containsChordIgnoreOctave(Chord oth) {
		for(Note n1: oth.notes) {
			boolean pres = false;
			for(Note n2 : notes) {
				if(n1.equalsIgnoreOctave(n2)) {
					pres = true;
					break;
				}
			}
			if(!pres) {
				return false;
			}
		}
		
		return true;
	}
	
	
	public String toString() {
		return notes.toString();
	}
}
