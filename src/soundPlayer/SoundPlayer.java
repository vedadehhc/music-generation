package soundPlayer;

import java.util.ArrayList;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import music.Chord;
import music.Key;
import music.Note;
import music.PlayedChord;
import music.PlayedNote;
import music.Tone;
import progressionGenerator.ChordNode;

public class SoundPlayer {

	public static void main(String[] args) {

		// Example stuff
		Key ckey = new Key(new Note(Tone.C, 4), Key.KeyType.Major);
		
		// Shows list of all available instruments
		// System.out.println(Arrays.toString(SoundPlayer.getAvailableInstrumentNames()));

		// Load new Sound Player with specified instrument
		SoundPlayer sp = new SoundPlayer("piano 1");
		
		// Sample program - plays indefinitely
		while (true) {

			sp.playChord(ckey.getChord(1).toPlayedChord(750, 80));
			sp.playChord(ckey.getChord(1).toPlayedChord(750, 80));
			sp.playChord(ckey.getChord(1).toPlayedChord(500, 80));

			sp.playChord(ckey.getChord(6).shiftOctave(-1).toPlayedChord(750, 80));
			sp.playChord(ckey.getChord(6).shiftOctave(-1).toPlayedChord(750, 80));
			sp.playChord(ckey.getChord(6).shiftOctave(-1).toPlayedChord(500, 80));
			
			sp.playChord(ckey.getChord(-3).toPlayedChord(750, 80));
			sp.playChord(ckey.getChord(-3).toPlayedChord(750, 80));
			sp.playChord(ckey.getChord(-3).toPlayedChord(500, 80));

			sp.playChord(ckey.getChord(-2).toPlayedChord(750, 80));
			sp.playChord(ckey.getChord(-2).toPlayedChord(750, 80));
			sp.playChord(ckey.getChord(-2).toPlayedChord(500, 80));
		}
	}

	public static String[] getAvailableInstrumentNames() {
		try {
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();

			Soundbank bank = synth.getDefaultSoundbank();
			synth.loadAllInstruments(bank);

			Instrument instr[] = synth.getLoadedInstruments();
			String[] instrumentNames = new String[instr.length];
			for (int i = 0; i < instr.length; i++) {
				instrumentNames[i] = instr[i].getName().trim().toUpperCase();
			}

			synth.close();
			return instrumentNames;
		} catch (MidiUnavailableException e) {
			return new String[] { "Unable to load MIDI" };
		}
	}

	private MidiChannel[] channels;
	private Synthesizer synth;
	private String instrumentName;
	private Instrument instrument;

	// Create a SoundPlayer for the given instrument
	public SoundPlayer(String instrumentName) {
		instrumentName = instrumentName.trim().toUpperCase();
		this.instrumentName = instrumentName;
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();

			channels = synth.getChannels();

			Soundbank bank = synth.getDefaultSoundbank();
			synth.loadAllInstruments(bank);

			Instrument instr[] = synth.getLoadedInstruments();
			instrument = instr[0];
			for (int i = 0; i < instr.length; i++) {
				if (instrumentName.equals(instr[i].getName().trim().toUpperCase())) {
					instrument = instr[i];
					break;
				}
			}

			System.out.println(instrument.getName().trim().toUpperCase());
			
			Patch instrPatch = instrument.getPatch();
			synth.loadInstrument(instrument);
			channels[0].programChange(instrPatch.getBank(), instrPatch.getProgram());
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	// create a SoundPlayer for piano
	public SoundPlayer() {
		this("Piano 1");
	}

	// Return the name of instrument currently associated with this SoundPlayer
	public String getInstrumentName() {
		return instrumentName;
	}

	// close the SoundPlayer
	public void close() {
		synth.close();
	}

	// Play a note
	public void playNote(PlayedNote note) {

		// Rest
		if (note.getID() < 0) {
			try {
				Thread.sleep(note.getDuration());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}

		// Start playing note
		channels[0].noteOn(note.getID(), note.getVolume());

		// Wait for duration
		try {
			Thread.sleep(note.getDuration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Stop playing note
		channels[0].noteOff(note.getID(), note.getVolume());
	}

	// Play a chord
	public void playChord(PlayedChord chord) {
		// Start playing notes
		for (PlayedNote note : chord.getNotes()) {
			channels[0].noteOn(note.getID(), note.getVolume());
		}

		// Wait for duration
		try {
			Thread.sleep(chord.getDuration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Stop playing notes
		for (PlayedNote note : chord.getNotes()) {
			channels[0].noteOff(note.getID(), note.getVolume());
		}
	}

	// Play a progression with chords 4 - 7 being shifted down an octave
	public void playProgression(ArrayList<ChordNode> progression, Key key, int duration, int volume) {
		for (ChordNode node : progression) {
			System.out.print(node.toString() + " -> ");
			Chord chord = node.chordFromNode(key);
			if(node.getChordRoot() >= 4) {
				chord = chord.shiftOctave(-1);
			}
			playChord(chord.toPlayedChord(duration, volume));
		}
		System.out.println("END");
	}

}
