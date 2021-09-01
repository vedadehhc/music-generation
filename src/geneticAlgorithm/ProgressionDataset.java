package geneticAlgorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import music.Chord;
import music.Note;
import music.Tone;
import progressionGenerator.ChordNode;
import progressionGenerator.ProgressionGenerator;

@SuppressWarnings("unchecked")
public class ProgressionDataset implements Dataset {

	public static final String filePath = "progressionDatasets/";

	public static final Note[] initialNotes = { new Note(Tone.C, 3), new Note(Tone.G, 3), new Note(Tone.E, 4),
			new Note(Tone.C, 5) };

	private byte[][] byteProgs; // uses 1-7 / 8-14 convention,
	private ArrayList<ChordNode>[] progs;
	private Note[][] bassLines;
	private Chord[][] chordProgs;

	public ProgressionDataset(int numProgs, int minSize) {
		byteProgs = new byte[numProgs][];
		progs = new ArrayList[numProgs];
		bassLines = new Note[numProgs][];
		chordProgs = new Chord[numProgs][];

		ProgressionGenerator pg = new ProgressionGenerator();

		for (int i = 0; i < byteProgs.length; i++) {
			progs[i] = pg.generateProgression(minSize);

			bassLines[i] = pg.generateBassLine(Organism.orgKey, progs[i]);

			byteProgs[i] = new byte[progs[i].size()];
			chordProgs[i] = new Chord[progs[i].size()];

			for (int j = 0; j < byteProgs[i].length; j++) {
				ChordNode cn = progs[i].get(j);
				int id = cn.getChordRoot() + (cn.is7th() ? 7 : 0);

				chordProgs[i][j] = Organism.orgKey.getChord(cn.getChordRoot(), cn.is7th());

				byteProgs[i][j] = (byte) id;
			}
		}
	}

	public ProgressionDataset(byte[][] byteProgs) {
		this.byteProgs = byteProgs;
		initialize();
	}

	public ProgressionDataset(String fileName) {
		try {
			FileInputStream in = new FileInputStream(new File(filePath + fileName));

			int numProgs = (in.read() << 24) + (in.read() << 16) + (in.read() << 8) + (in.read() << 0);
			byteProgs = new byte[numProgs][];

			for (int i = 0; i < numProgs; i++) {
				int size =  (in.read() << 24) + (in.read() << 16) + (in.read() << 8) + (in.read() << 0);
				byteProgs[i] = new byte[size];
				in.read(byteProgs[i]);
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
		progs = new ArrayList[byteProgs.length];
		bassLines = new Note[byteProgs.length][];
		chordProgs = new Chord[byteProgs.length][];

		ProgressionGenerator pg = new ProgressionGenerator();
		ChordNode[][] nodes = pg.getChordNodes();

		for (int i = 0; i < byteProgs.length; i++) {
			progs[i] = new ArrayList<ChordNode>();
			chordProgs[i] = new Chord[byteProgs[i].length];

			for (int j = 0; j < byteProgs[i].length; j++) {
				int cRoot = byteProgs[i][j];
				int type = 0;

				if (cRoot > 7) {
					cRoot -= 7;
					type = 1;
				}

				progs[i].add(nodes[type][cRoot]);
				chordProgs[i][j] = Organism.orgKey.getChord(cRoot, type == 1);
			}

			bassLines[i] = pg.generateBassLine(Organism.orgKey, progs[i]);
		}
	}

	public void save(String fileName) {
		try {
			FileOutputStream out = new FileOutputStream(new File(filePath + fileName));

			out.write(this.size() >> 24);
			out.write(this.size() >> 16);
			out.write(this.size() >> 8);
			out.write(this.size() >> 0);

			for (int i = 0; i < this.size(); i++) {
				out.write(this.getByteProg(i).length >> 24);
				out.write(this.getByteProg(i).length >> 16);
				out.write(this.getByteProg(i).length >> 8);
				out.write(this.getByteProg(i).length >> 0);
				out.write(this.getByteProg(i));
			}

			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int[] getHashes() {
		return Organism.hashes;
	}

	public int getHash(int index) {
		return Organism.hashes[index];
	}

	public ArrayList<ChordNode>[] getProgs() {
		return progs;
	}

	public ArrayList<ChordNode> getProg(int index) {
		return progs[index];
	}

	public byte[][] getByteProgs() {
		return byteProgs;
	}

	public byte[] getByteProg(int index) {
		return byteProgs[index];
	}

	public Note[][] getBassLines() {
		return bassLines;
	}

	public Note[] getBassLine(int index) {
		return bassLines[index];
	}

	public Chord[][] getChordProgs() {
		return chordProgs;
	}

	public Chord[] getChordProg(int index) {
		return chordProgs[index];
	}

	public Note[] getInitialNotes(int index) {
		return initialNotes;
	}

	public int size() {
		return progs.length;
	}

	public String toString() {
		return Arrays.deepToString(progs);
	}
}
