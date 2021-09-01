package progressionGenerator;

import java.util.ArrayList;
import java.util.List;

import music.Chord;
import music.Key;
import music.Note;
import music.Range;
import music.Tone;
import soundPlayer.SoundPlayer;

public class ProgressionGenerator {

	public static final ProgressionGenerator pg = new ProgressionGenerator();
	
	public static void main(String[] args) {
		ProgressionGenerator pg = new ProgressionGenerator();

		Note tonic = new Note(Tone.C, 3);
		Key ckey = new Key(tonic);

		// System.out.println(Arrays.toString(SoundPlayer.getAvailableInstrumentNames()));
		SoundPlayer sp = new SoundPlayer("piano 1");

		ArrayList<ChordNode> progression = pg.generateProgression(10); // adjust this number to determine the minimum
																		// length of your chain
		sp.playNote(tonic.toPlayedNote(1000, 80));
		sp.playProgression(progression, ckey, 1000, 80);
		sp.playChord(ckey.getChord(1).toPlayedChord(2000, 80));
	}

	private ChordNode[][] nodes; // stores the different types of chords we can play in a progression

	// Create a new progression generator based on the default Markov Chain
	public ProgressionGenerator() {
		nodes = new ChordNode[2][8]; // is7th, chordRoot

		nodes[0][0] = new ChordNode(0, false);
		nodes[1][0] = new ChordNode(0, true);
		for (int i = 1; i < nodes[0].length; i++) {
			nodes[0][i] = new ChordNode(i, false);
			nodes[1][i] = new ChordNode(i, true);
		}

		nodes[0][1].addOutNode(nodes[0][2], 1);
		nodes[0][1].addOutNode(nodes[1][2], 1);
		nodes[0][1].addOutNode(nodes[0][3], 1);
		nodes[0][1].addOutNode(nodes[0][4], 1);
		nodes[0][1].addOutNode(nodes[0][5], 1);
		nodes[0][1].addOutNode(nodes[1][5], 1);
		nodes[0][1].addOutNode(nodes[0][6], 1);
		nodes[0][1].addOutNode(nodes[0][7], 1);
		nodes[0][1].addOutNode(nodes[1][7], 1);

		nodes[0][2].addOutNode(nodes[0][5], 14);
		nodes[0][2].addOutNode(nodes[1][5], 7);
		nodes[0][2].addOutNode(nodes[0][7], 6);
		nodes[0][2].addOutNode(nodes[1][7], 3);

		nodes[1][2].addOutNode(nodes[0][5], 14);
		nodes[1][2].addOutNode(nodes[1][5], 7);
		nodes[1][2].addOutNode(nodes[0][7], 6);
		nodes[1][2].addOutNode(nodes[1][7], 3);

		nodes[0][3].addOutNode(nodes[0][4], 2);
		nodes[0][3].addOutNode(nodes[0][6], 8);

		nodes[0][4].addOutNode(nodes[0][1], 2);
		nodes[0][4].addOutNode(nodes[0][5], 14);
		nodes[0][4].addOutNode(nodes[1][5], 7);
		nodes[0][4].addOutNode(nodes[0][7], 6);
		nodes[0][4].addOutNode(nodes[1][7], 3);

		nodes[0][5].addOutNode(nodes[0][1], 9);
		nodes[0][5].addOutNode(nodes[0][6], 1);

		nodes[1][5].addOutNode(nodes[0][1], 9);
		nodes[1][5].addOutNode(nodes[0][6], 1);

		nodes[0][6].addOutNode(nodes[0][2], 1);
		nodes[0][6].addOutNode(nodes[0][4], 1);

		nodes[0][7].addOutNode(nodes[0][1], 16);
		nodes[0][7].addOutNode(nodes[0][5], 1);
		nodes[0][7].addOutNode(nodes[1][5], 1);
		nodes[0][7].addOutNode(nodes[0][6], 2);

		nodes[1][7].addOutNode(nodes[0][1], 16);
		nodes[1][7].addOutNode(nodes[0][5], 1);
		nodes[1][7].addOutNode(nodes[1][5], 1);
		nodes[0][7].addOutNode(nodes[0][6], 2);
	}

	public ChordNode[][] getChordNodes() {
		return nodes;
	}

	/**
	 * Generate a Major chord progression that is at least <tt>minSize</tt> chords long.
	 * This method always starts and ends with a I chord
	 * @param minSize - the minimum size of the chord progression.
	 * @return the Major chord progression as an <tt>ArrayList&lt;ChordNode&gt;</tt>.
	 */
	public ArrayList<ChordNode> generateProgression(int minSize) {
		ArrayList<ChordNode> progression = new ArrayList<ChordNode>();

		ChordNode curNode = nodes[0][1];

		while (curNode.getChordRoot() != 0) {
			progression.add(curNode);
			if (progression.size() >= minSize && curNode.getChordRoot() == 1) {
				curNode = nodes[0][0];
				break;
			}

			curNode = curNode.nextNode();
			if (curNode == null) {
				curNode = nodes[0][0];
			}
		}

		return progression;
	}

	
	// Note: use C2 for key to get best results
	public Note[] generateBassLine(Key key, List<ChordNode> progression) {
		Note[] bassLine = new Note[progression.size()];

		for (int i = 0; i < progression.size(); i++) {
			
			Chord chord = progression.get(i).chordFromNode(key);
			Note bass = chord.getNotes().get(0);
			
			while(Range.voiceRanges[0].checkNote(bass) < 0) {
				bass = bass.shiftOctave(1);
			}
			while(Range.voiceRanges[0].checkNote(bass) > 0) {
				bass = bass.shiftOctave(-1);
			}
			
			bassLine[i] = bass;
		}

		return bassLine;
	}

	// TODO: finish implementation
	public Note[] generateSmoothBassLine(Key key, ArrayList<ChordNode> progression) {
		Note[] bassLine = new Note[progression.size()];

		Note prevNote = progression.get(0).chordFromNode(key).getNotes().get(0);
		for (int i = 0; i < progression.size(); i++) {
			
			Chord chord = progression.get(i).chordFromNode(key);
			Note bass = chord.getNotes().get(0);
			
			while(Range.voiceRanges[0].checkNote(bass) < 0) {
				bass.shiftOctave(1);
			}
			while(Range.voiceRanges[0].checkNote(bass) > 0) {
				bass.shiftOctave(-1);
			}
			
			int distance = bass.getID() - prevNote.getID();
			System.out.println(distance);
			
			bassLine[i] = bass;

			prevNote = bass;
		}

		return bassLine;
	}

}
