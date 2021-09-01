package grader;

import music.Note;
import java.util.Arrays;
import music.Chord;

public class Grader {

	// possible 7th chords = 2, 5, 7
	public static int gradeBasic(Note[][] notes, Chord[] chords, byte[] byteChords) {
		// variables in defects[], in the order they appear:
		// defects[0] --> correct notes
		// defects[1] --> mixing
		// defects[2] --> all notes in chord are included
		int[] defects = new int[3];
		Arrays.fill(defects, 0);
		int[] weights = { 4, 1, 2 };

		gradeCorrectNotes(notes, chords, defects, 0);
		gradeVoiceMixing(notes, chords, defects, 1);
		gradeNotesIncluded(notes, chords, defects, 2);
		
		return calcNorm(defects, weights);

	}

	public static int gradeAdvanced(Note[][] notes, Chord[] chords, byte[] byteChords) {

		// variables in defects[], in the order they appear:
		// defects[0] --> mixing
		// defects[1] --> octaves
		// defects[2] --> fifths
		// defects[3] --> correct notes
		// defects[4] --> voice spacing
		// defects[5] --> leading tone to dominant
		// defects[6] --> all notes in chord are included
		int[] defects = new int[7];
		Arrays.fill(defects, 0);
		int[] weights = { 1, 1, 1, 4, 1, 1, 2 };

		// searches for voice mixing
		gradeVoiceMixing(notes, chords, defects, 0);

		for(int i =0; i < notes.length; i++) {
			
			if(i < notes.length - 1) {
				
			}
		}
		
		// search for parallel octaves
		for (int i = 0; i < notes.length - 1; i++) {
			for (int j = 0; j < notes[i].length; j++) {
				for (int k = j + 1; k < notes[i].length; k++) {
					if (notes[i][j].equalsIgnoreOctave(notes[i][k])
							&& notes[i + 1][j].equalsIgnoreOctave(notes[i + 1][k])) {
						defects[1]++;
					}
				}
			}
		}

		// search for parallel fifths
		for (int i = 0; i < notes.length - 1; i++) {
			for (int j = 0; j < notes[i].length; j++) {
				for (int k = j + 1; k < notes[i].length; k++) {
					if (Math.abs(notes[i][j].getID() - notes[i][k].getID()) % 7 == 0) {
						if (Math.abs(notes[i + 1][j].getID() - notes[i + 1][k].getID()) % 7 == 0) {
							defects[2]++;
						}
					}
				}
			}
		}

		gradeCorrectNotes(notes, chords, defects, 3);

		// voice spacing
		for (int i = 0; i < notes.length; i++) {
			if (Math.abs(notes[i][2].getID() - notes[i][1].getID()) > 12) {
				defects[4]++;
			}
			if (Math.abs(notes[i][3].getID() - notes[i][2].getID()) > 12) {
				defects[4]++;
			}
		}

		// leading tone to dominant
		for (int i = 0; i < notes.length - 1; i++) {
			for (int j = 1; j < notes[i].length; j++) {
				if (notes[i][j].getTone().toString().equals("B")) {
					if (!notes[i + 1][j].getTone().toString().equals("C")) {
						defects[5]++;
					}
				}
			}
		}
		
		gradeNotesIncluded(notes, chords, defects, 6);

		return calcNorm(defects, weights);
	}

	public static void gradeCorrectNotes(Note[][] notes, Chord[] chords, int[] output, int place) {
		for (int i = 0; i < notes.length; i++) {
			for (int j = 0; j < notes[i].length; j++) {
				// if this note is in the chord
				boolean inChord = false;

				for (Note k : chords[i].getNotes()) {
					if (k.equalsIgnoreOctave(notes[i][j])) {
						inChord = true;
						break;
					}
				}

				if (!inChord) {
					output[place]++;
				}
			}
		}
	}

	public static void gradeVoiceMixing(Note[][] notes, Chord[] chords, int[] output, int place) {
		for (int i = 0; i < notes.length; i++) {
			for (int j = 0; j < notes[i].length - 1; j++) {
				// voice mixing
				if (notes[i][j].getID() > notes[i][j + 1].getID()) {
					output[place]++;
				}
			}
		}
	}

	public static void gradeNotesIncluded(Note[][] notes, Chord[] chords, int[] output, int place) {
		// all notes in chord are included
		for (int i = 0; i < notes.length; i++) {
			boolean containsAll = true;

			for (Note k : chords[i].getNotes()) {
				boolean found = false;
				for (Note n : notes[i]) {
					if (n.equalsIgnoreOctave(k)) {
						found = true;
						break;
					}
				}
				if (!found) {
					containsAll = false;
					break;
				}
			}

			if (!containsAll) {
				output[place]++;
			}
		}
	}

	public static int calcNorm(int[] defects, int[] weights) {
		// gets dot product of two vectors
		int overallFitness = 0;
		for (int i = 0; i < defects.length; i++) {
			overallFitness += (defects[i] * weights[i]);
		}
		return overallFitness;
	}
}