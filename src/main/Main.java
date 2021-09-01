package main;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import geneticAlgorithm.Organism;
import grader.Grader;
import music.Chord;
import music.Key;
import music.Note;
import music.Tone;
import progressionGenerator.ChordNode;
import progressionGenerator.ProgressionGenerator;
import soundPlayer.SoundPlayer;

public class Main {
	public static Scanner sc;

	public static void main(String[] args) {

		// System.out.println(Arrays.deepToString(SoundPlayer.getAvailableInstrumentNames()));

		System.out.println("AI Music Generation: Composing Four-Part Harmonies with the Genetic Algorithm");

		sc = new Scanner(System.in);

		List<ChordNode> p1 = Arrays.asList(new ChordNode(1, false), new ChordNode(2, true), new ChordNode(5, false),
				new ChordNode(1, false), new ChordNode(2, true), new ChordNode(7, false), new ChordNode(1, false),
				new ChordNode(2, true), new ChordNode(5, false), new ChordNode(1, false));

		List<ChordNode> p2 = Arrays.asList(new ChordNode(1, false), new ChordNode(3, false), new ChordNode(4, false),
				new ChordNode(5, false), new ChordNode(1, false), new ChordNode(3, false), new ChordNode(6, false),
				new ChordNode(2, false), new ChordNode(5, false), new ChordNode(1, false));

		List<ChordNode> p3 = Arrays.asList(new ChordNode(1, false), new ChordNode(5, true), new ChordNode(1, false),
				new ChordNode(5, false), new ChordNode(1, false), new ChordNode(2, true), new ChordNode(5, true),
				new ChordNode(6, false), new ChordNode(4, false), new ChordNode(5, false), new ChordNode(1, false));

		play(p1);
		play(p2);
		play(p3);
	}

	public static void play(List<ChordNode> prog) {
		sc.nextLine();

		ProgressionGenerator pg = new ProgressionGenerator();
		// ArrayList<ChordNode> prog = pg.generateProgression(10);

		System.out.println("Random Chord Progression generated");

		Note tonic = new Note(Tone.C, 2);
		Key ckey = new Key(tonic);

		Note[] bassLine = pg.generateBassLine(ckey, prog);

		// System.out.println(Arrays.toString(bassLine));

		Chord[] chords = new Chord[prog.size()];
		byte[] progInt = new byte[prog.size()];
		String[] progName = new String[prog.size()];

		for (int i = 0; i < prog.size(); i++) {
			chords[i] = ckey.getChord(prog.get(i).getChordRoot(), prog.get(i).is7th());
			progInt[i] = (byte) (prog.get(i).getChordID());
			if (progInt[i] > 7) {
				progName[i] = Chord.romanNumeralChords.get(progInt[i] - 8) + "-7";
			} else {
				progName[i] = Chord.romanNumeralChords.get(progInt[i] - 1);
			}
		}

		Note[] initialNotes = { new Note(Tone.C, 3), new Note(Tone.G, 3), new Note(Tone.E, 4), new Note(Tone.C, 5) };

		System.out.println(Arrays.toString(progName));
//		System.out.println("Chord Progression processed");

		sc.nextLine();

		Organism org = new Organism("ab-cd/ab-cd0.org");

		Note[][] harmony = org.generateHarmony(initialNotes, progInt, bassLine);

		System.out.println("Four-Part Harmony generated");

		for (int i = 0; i < harmony.length; i++) {
			harmony[i][0] = bassLine[i];
		}
		System.out.println(Arrays.deepToString(harmony));

		int score = Grader.gradeBasic(harmony, chords, progInt);
		System.out.println("Four-Part Harmony Score: " + score);

		sc.nextLine();

		SoundPlayer sp = new SoundPlayer("piano 1");
		System.out.println("Playing Harmony...");
		// sp.playChord((new Chord(harmony[0])).toPlayedChord(1000, 80));

		for (int i = 0; i < harmony.length; i++) {
			Chord c = new Chord(harmony[i]);
			if (i == harmony.length - 1) {
				sp.playChord(c.toPlayedChord(2000, 80));
			} else {
				sp.playChord(c.toPlayedChord(1000, 80));
			}
		}
		System.out.println("Finished Playing Harmony.");
	}

}

// reference harmony
// Key akey = new Key(new Note(Tone.A, 2));
// Note[][] harmony = {
//{ new Note(Tone.A, 3), new Note(Tone.A, 3), new Note(Tone.E, 4), new Note(Tone.C, Shift.Sharp, 5) },
//{ new Note(Tone.F, Shift.Sharp, 3), new Note(Tone.A, 3), new Note(Tone.D, 4), new Note(Tone.D, Shift.Natural, 5) },
//{ new Note(Tone.C, Shift.Sharp, 3), new Note(Tone.A, 3), new Note(Tone.E, 4), new Note(Tone.A, Shift.Natural, 4) },
//{ new Note(Tone.E, Shift.Natural, 3), new Note(Tone.B, 3), new Note(Tone.D, 4), new Note(Tone.G, Shift.Sharp, 4) },
//{ new Note(Tone.F, Shift.Sharp, 3), new Note(Tone.A, 3), new Note(Tone.C, Shift.Sharp, 4), new Note(Tone.A, Shift.Natural, 4) },
//{ new Note(Tone.D, 3), new Note(Tone.A, 3), new Note(Tone.B, 3), new Note(Tone.F, Shift.Sharp, 4) },
//{ new Note(Tone.E, 3), new Note(Tone.G, Shift.Sharp, 3), new Note(Tone.B, 3), new Note(Tone.E, Shift.Natural, 4) }};
//Chord[] chords = { akey.getChord(1), akey.getChord(4), akey.getChord(1), akey.getChord(5, true),
//		akey.getChord(6), akey.getChord(2, true), akey.getChord(5) };
