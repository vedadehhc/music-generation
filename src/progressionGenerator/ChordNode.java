package progressionGenerator;

import java.util.ArrayList;

import music.Chord;
import music.Key;

// Each node in the Network represents a Chord. We don't use the chord class since that is based off of actual keys 
// while we only care about the scale degree numbers for these chords
// TODO: Add compatibility for secondary dominance and for inverted chords
public class ChordNode {

	private int chordRoot; // This is the root of the chord based off of the key tonic i.e. C is I in C
						// major, D is ii in C Major, etc.
	private boolean is7th; // Whether this is a 7th chord or not

	private ArrayList<ChordNode> outNodes; // The nodes that the progression can go to after this node
	private ArrayList<Integer> weights; // The weights for each out node
										// probability of choosing an out node = weight / total weight
	private int totalWeight; // the total weight of all out nodes

	// Create a new node with given parameters
	public ChordNode(int chordID, boolean is7th) {
		this.chordRoot = chordID;
		this.is7th = is7th;
		outNodes = new ArrayList<ChordNode>();
		weights = new ArrayList<Integer>();
	}

	// returns chord root
	public int getChordRoot() {
		return chordRoot;
	}
	
	// returns whether chord is 7th
	public boolean is7th() {
		return is7th;
	}
	
	public int getChordID() {
		return chordRoot + (is7th ? 7 : 0);
	}
	
	// Adds and out node with the given weight
	public void addOutNode(ChordNode node, int w) {
		outNodes.add(node);
		weights.add(w);
		totalWeight += w;
	}
	
	public ArrayList<ChordNode> getOutNodes() {
		return outNodes;
	}
	
	// Generates the next node in the progression from this node -- based on probability and inputed weights
	public ChordNode nextNode() {
		double rand = Math.random() * totalWeight;

		int total = 0;
		for (int i = 0; i < outNodes.size(); i++) {
			total += weights.get(i);
			if (rand < total) {
				return outNodes.get(i);
			}
		}

		return null;
	}
	
	public Chord chordFromNode(Key key) {
		return chordFromNode(key, 0);
	}
	
	public Chord chordFromNode(Key key, int inversion) {
		return key.getChord(chordRoot, is7th, inversion);
	}
	
	
	// checks is this node stores the same chord as another node
	public boolean equals(ChordNode oth) {
		return (this.chordRoot == oth.chordRoot && this.is7th == oth.is7th);
	}
	
	
	// Returns Roman numeral form of chord
	public String toString() {
		if (is7th) {
			return Chord.romanNumeralChords.get(chordRoot - 1) + "7";
		}
		return Chord.romanNumeralChords.get(chordRoot - 1);
	}
}
