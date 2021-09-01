package geneticAlgorithm;

import music.Chord;
import music.Note;

public interface Dataset {
	
	public int[] getHashes();
	
	public int getHash(int index);

	public byte[][] getByteProgs();

	public byte[] getByteProg(int index);

	public Note[][] getBassLines();

	public Note[] getBassLine(int index);

	public Chord[][] getChordProgs();

	public Chord[] getChordProg(int index);
	
	public Note[] getInitialNotes(int index);
	
	public void save(String fileName);
	
	public int size();
}
