from typing import List
from .Note import PlayedNote, Note, Tone, Shift
from .Chord import Chord

class PlayedChord:
    def __init__(self, duration: int = 500):
        self.notes: List[PlayedNote] = []
        self.duration: int = duration

    def shift_octave(self, amt: int) -> 'PlayedChord':
        chord = PlayedChord(self.duration)
        for note in self.notes:
            chord.add_note(note.note.shift_octave(amt).to_played_note(self.duration, note.volume))
        return chord

    def invert(self, inversions: int) -> 'PlayedChord':
        if inversions <= 0 or inversions >= len(self.notes):
            return self

        chord = PlayedChord(self.duration)
        for i in range(inversions, len(self.notes)):
            chord.add_note(self.notes[i])
        for i in range(inversions):
            chord.add_note(self.notes[i].note.shift_octave(1).to_played_note(self.duration, self.notes[i].volume))

        return chord

    def add_note(self, note: PlayedNote) -> None:
        self.notes.append(note)

    def get_notes(self) -> List[PlayedNote]:
        return self.notes

    def get_duration(self) -> int:
        return self.duration

    @classmethod
    def from_chord(cls, chord: Chord, duration: int, volume: int = 80) -> 'PlayedChord':
        played_chord = cls(duration)
        for note in chord.get_notes():
            played_chord.add_note(note.to_played_note(duration, volume))
        return played_chord

    def __str__(self) -> str:
        return f"PlayedChord(duration={self.duration}, notes={[str(note.note) for note in self.notes]})"

if __name__ == "__main__":
    # Simple test
    c_major = PlayedChord(500)
    c_major.add_note(Note(Tone.C, Shift.Natural, 4).to_played_note(500, 80))
    c_major.add_note(Note(Tone.E, Shift.Natural, 4).to_played_note(500, 80))
    c_major.add_note(Note(Tone.G, Shift.Natural, 4).to_played_note(500, 80))

    print(f"C Major Chord: {c_major}")

    inverted = c_major.invert(1)
    print(f"First inversion: {inverted}")

    shifted = c_major.shift_octave(1)
    print(f"Shifted up one octave: {shifted}")

    # Test from_chord class method
    c_major_chord = Chord([Note(Tone.C, Shift.Natural, 4), Note(Tone.E, Shift.Natural, 4), Note(Tone.G, Shift.Natural, 4)])
    played_c_major = PlayedChord.from_chord(c_major_chord, 500, 80)
    print(f"Played C Major from Chord: {played_c_major}")
