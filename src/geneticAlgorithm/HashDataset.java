package geneticAlgorithm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import music.Chord;
import music.Note;
import music.Range;

public class HashDataset implements Dataset {

	public static final String filePath = "hashDatasets/";

	private int[] hashes;
	private Note[][] bassLines;
	private Chord[][] chords;
	private byte[][] byteChords;

	public HashDataset() {
		ArrayList<Integer> inputs = Organism.generateValidInputs();
		hashes = new int[inputs.size()];
		for (int i = 0; i < inputs.size(); i++) {
			hashes[i] = inputs.get(i);
		}
		initialize();
	}

	public HashDataset(int[] hashes) {
		this.hashes = hashes;
		initialize();
	}

	public HashDataset(ArrayList<Integer> hashArr) {
		hashes = new int[hashArr.size()];
		for (int i = 0; i < hashes.length; i++) {
			hashes[i] = hashArr.get(i);
		}
		initialize();
	}

	public HashDataset(String fileName) {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(filePath + new File(fileName)));

			hashes = new int[in.readInt()];

			for (int i = 0; i < hashes.length; i++) {
				hashes[i] = in.readInt();
			}

			in.close();
			initialize();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initialize() {
		bassLines = new Note[hashes.length][2];
		chords = new Chord[hashes.length][2];
		byteChords = new byte[hashes.length][2];

		for (int i = 0; i < hashes.length; i++) {
			int chordID = Organism.getChordFromHash(hashes[i]);
			int root = chordID <= 7 ? chordID : chordID - 7;
			boolean is7th = chordID > 7;

			byteChords[i][1] = (byte) chordID;
			chords[i][1] = Organism.orgKey.getChord(root, is7th);
			bassLines[i][1] = chords[i][1].getNotes().get(0);

			while (Range.voiceRanges[0].checkNote(bassLines[i][1]) < 0) {
				bassLines[i][1] = bassLines[i][1].shiftOctave(1);
			}
			while (Range.voiceRanges[0].checkNote(bassLines[i][1]) > 0) {
				bassLines[i][1] = bassLines[i][1].shiftOctave(-1);
			}

			Note[] prevNotes = Organism.getNotesFromHash(hashes[i]);
			bassLines[i][0] = prevNotes[0];

			chords[i][0] = new Chord(prevNotes);

			byte prevRoot = 1;
			boolean prevIs7th = false;
			for (byte j = 1; j <= 7; j++) {
				if (Organism.orgKey.getNote(j).equalsIgnoreOctave(prevNotes[0])) {
					prevRoot = j;

					int c7th = (j + 6 > 7) ? j - 1 : j + 6;
					if (chords[i][0].containsNoteIgnoreOctave(Organism.orgKey.getNote(c7th))) {
						prevIs7th = true;
					}
				}
			}

			byteChords[i][0] = (byte) (prevRoot + (prevIs7th ? 7 : 0));
		}
	}

	public void save(String fileName) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath + fileName)));

			out.writeInt(hashes.length);

			for (int i = 0; i < hashes.length; i++) {
				out.writeInt(hashes[i]);
			}

			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int[] getHashes() {
		return hashes;
	}

	public int getHash(int index) {
		return hashes[index];
	}

	public Note[][] getBassLines() {
		return bassLines;
	}

	public Note[] getBassLine(int index) {
		return bassLines[index];
	}

	public Chord[][] getChordProgs() {
		return chords;
	}

	public Chord[] getChordProg(int index) {
		return chords[index];
	}
	
	public byte[][] getByteProgs() {
		return byteChords;
	}

	public byte[] getByteProg(int index) {
		return byteChords[index];
	}
	
	public Note[] getInitialNotes(int index) {
		return chords[index][0].getNotesAsArr();
	}

	public int size() {
		return hashes.length;
	}

}
