package geneticAlgorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import music.Chord;
import music.Key;
import music.Key.KeyType;
import music.Note;
import music.Range;
import music.Tone;
import progressionGenerator.ChordNode;
import progressionGenerator.ProgressionGenerator;

public class Organism implements Comparable<Organism> {

	public static final String filePath = "organsims/";
	
	public static final Key orgKey = new Key(new Note(Tone.C, 2), KeyType.Major);
	public static final Note[][] ranges = { Range.voiceRanges[0].getInKey(orgKey),
			Range.voiceRanges[1].getInKey(orgKey), Range.voiceRanges[2].getInKey(orgKey),
			Range.voiceRanges[3].getInKey(orgKey) }; // B, T, A, S
	public static final int[] hashes = generateAllHashes();

	private static int[] generateAllHashes() {
		int[] hashes = new int[getInputSpace()];
		for(int i =0; i <hashes.length; i++) {
			hashes[i] = i;
		}
		return hashes;
	}
	
	// curChord : 1-7 (normal chords); 8-14 (7th chords)
	// Ignoring inversions
	public static int generateHash(Note[] prevNotes, int curChord) {

		int mult = 1;
		for (int i = 0; i < ranges.length; i++) {
			mult *= ranges[i].length;
		}

		int hashVal = 0;
		hashVal += (curChord - 1) * mult; // lengths of other ranges, DOUBLE CHECK THIS

		for (int i = 0; i < ranges.length; i++) { // iterate through all ranges

			mult /= ranges[i].length;

			int index = 0; // holds the index of the note within the array
			for (; index < ranges[i].length; index++) { // search for note in array using for loop
				if (prevNotes[i].equals(ranges[i][index])) {
					break;
				}
			}

			hashVal += mult * index;
		}

		// find index of each voice using ranges array
		// add up the lengths of the other ranges
		return hashVal;
	}

	public static int generateHash(int[] prevNotes, int curChord) {
		int mult = 1;
		for (int i = 0; i < ranges.length; i++) {
			mult *= ranges[i].length;
		}

		int hashVal = 0;
		hashVal += (curChord - 1) * mult; // lengths of other ranges, DOUBLE CHECK THIS

		for (int i = 0; i < ranges.length; i++) { // iterate through all ranges
			mult /= ranges[i].length;

			hashVal += mult * prevNotes[i];
		}

		// find index of each voice using ranges array
		// add up the lengths of the other ranges
		return hashVal;
	}

	public static Note[] getNotesFromHash(int hash) {
		Note[] notes = new Note[4];

		for (int i = ranges.length - 1; i >= 0; i--) {
			notes[i] = ranges[i][hash % ranges[i].length];
			hash /= ranges[i].length;
		}

		return notes;
	}

	public static int getChordFromHash(int hash) {
		int mult = 1;
		for (int i = 0; i < ranges.length; i++) {
			mult *= ranges[i].length;
		}

		return (hash / mult) + 1;
	}

	public static ArrayList<Integer> generateValidInputsForChord(int chord) {
		int root = chord <= 7 ? chord : chord - 7;
		ArrayList<ChordNode> sampleProgression = new ArrayList<ChordNode>();
		sampleProgression.add(ProgressionGenerator.pg.getChordNodes()[0][root]);
		Note[] bassLine = ProgressionGenerator.pg.generateBassLine(orgKey, sampleProgression);
		
		return generateValidInputsForChord(chord, bassLine[0]);
	}

	private static ArrayList<Integer> generateValidInputsForChord(int chord, Note bass) {
		ArrayList<Integer> inputs = new ArrayList<Integer>();

		int root = chord <= 7 ? chord : chord - 7;
		boolean is7th = chord > 7;

		Chord c = orgKey.getChord(root, is7th);
		ArrayList<ChordNode> out = ProgressionGenerator.pg.getChordNodes()[is7th ? 1 : 0][root].getOutNodes();

		for (int b = 0; b < ranges[0].length; b++) {
			if (bass.equals(ranges[0][b])) {
				for (int t = 0; t < ranges[1].length; t++) {
					if (c.containsNoteIgnoreOctave(ranges[1][t])) {
						for (int a = 0; a < ranges[2].length; a++) {
							if (c.containsNoteIgnoreOctave(ranges[2][a])) {
								for (int s = 0; s < ranges[3].length; s++) {
									if (c.containsNoteIgnoreOctave(ranges[3][s])) {
										for (ChordNode cn : out) {
											inputs.add(generateHash(new int[] { b, t, a, s }, cn.getChordID()));
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return inputs;
	}
	
	
	// 
	public static ArrayList<Integer> generateValidInputs() {

		ArrayList<ChordNode> sampleProgression = new ArrayList<ChordNode>();
		for (int c = 1; c <= 7; c++) {
			sampleProgression.add(ProgressionGenerator.pg.getChordNodes()[0][c]);
		}
		Note[] bassLine = ProgressionGenerator.pg.generateBassLine(orgKey, sampleProgression);

		ArrayList<Integer> inputs = new ArrayList<Integer>();
		for (int c = 1; c <= 7; c++) {
			inputs.addAll(generateValidInputsForChord(c, bassLine[c - 1]));
			if (c == 2 || c == 5 || c == 7) {
				inputs.addAll(generateValidInputsForChord(c + 7, bassLine[c - 1]));
			}
		}
		return inputs;
	}

	public static int getInputSpace() {
		int inputSpace = 14;
		for (int i = 0; i < ranges.length; i++) {
			inputSpace *= ranges[i].length;
		}
		return inputSpace;
	}

	private byte[][] genome;

	private int fitness;

	public Organism() {
		int inputSpace = getInputSpace();

		genome = new byte[3][inputSpace];

		for (int i = 0; i < genome.length; i++) {
			for (int j = 0; j < genome[i].length; j++) {
				byte randInd = (byte) (Math.random() * ranges[i + 1].length);
				// genome[i][j] = ranges[j + 1][randInd];
				genome[i][j] = randInd;
			}
		}
		fitness = 0;
	}

	public Organism(byte[][] genome) {
		this.genome = genome;
		fitness = 0;
	}
	
	public Organism(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(new File(filePath + fileName));

			genome = new byte[3][Organism.getInputSpace()];
			fitness = 0;

			for (int i = 0; i < genome.length; i++) {
				fis.read(genome[i]);
			}

			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save(String fileName) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(filePath + fileName));

			byte[][] org = this.getGenome();
			for (int i = 0; i < org.length; i++) {
				fos.write(org[i]);
			}

			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Should only be used for targeted training/testing
	public Note[] getNextNotes(int hash, Note bassNote) {
		Note[] nextNotes = new Note[genome.length + 1];
		nextNotes[0] = bassNote;

		for (int i = 0; i < genome.length; i++) {
			nextNotes[i + 1] = ranges[i + 1][genome[i][hash]];
		}

		return nextNotes;
	}

	// Returns array of next notes: {B, T, A, S}
	public Note[] getNextNotes(Note[] prevNotes, int curChord, Note bassNote) {
		return getNextNotes(generateHash(prevNotes, curChord), bassNote);
	}

	// Requires initial notes for all 4 voice parts.
	public Note[][] generateHarmony(Note[] initialNotes, byte[] progression, Note[] bassLine) {
		Note[][] harmony = new Note[progression.length][4];
		harmony[0] = Arrays.copyOf(initialNotes, 4);

		for (int i = 1; i < progression.length; i++) {
			harmony[i] = getNextNotes(harmony[i - 1], progression[i], bassLine[i]);
		}

		return harmony;
	}

	public int getFitness() {
		return fitness;
	}

	public void addFitness(int amt) {
		this.fitness += amt;
	}

	public byte[][] getGenome() {
		return genome;
	}

	public String toString() {
		return "Organism";
	}

	@Override
	public int compareTo(Organism o) {
		return this.fitness - o.fitness;
	}

}
