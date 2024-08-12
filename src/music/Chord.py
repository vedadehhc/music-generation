from typing import List, Optional
from .Note import Note, Tone, Shift, PlayedNote

class Chord:
    roman_numeral_chords = ["I", "ii", "iii", "IV", "V", "vi", "viio"]

    def __init__(self, notes: Optional[List[Note]] = None):
        self.notes: List[Note] = notes if notes is not None else []

    def shift_octave(self, amt: int) -> 'Chord':
        return Chord([note.shift_octave(amt) for note in self.notes])

    def invert(self, inversions: int) -> 'Chord':
        if inversions <= 0 or inversions >= len(self.notes):
            return self

        chord = Chord()
        for i in range(inversions, len(self.notes)):
            chord.add_note(self.notes[i])
        for i in range(inversions):
            chord.add_note(self.notes[i].shift_octave(1))

        return chord

    def add_note(self, note: Note) -> None:
        self.notes.append(note)

    def get_notes(self) -> List[Note]:
        return self.notes

    def get_notes_as_arr(self) -> List[Note]:
        return self.notes.copy()

    def contains_note(self, note: Note) -> bool:
        return any(n.equals(note) for n in self.notes)

    def contains_note_ignore_octave(self, note: Note) -> bool:
        return any(n.equals_ignore_octave(note) for n in self.notes)

    def to_played_chord(self, duration: int, volume: int = 80) -> 'PlayedChord':
        chord = PlayedChord(duration)
        for note in self.notes:
            chord.add_note(note.to_played_note(duration, volume))
        return chord

    def contains_chord(self, other: 'Chord') -> bool:
        return all(self.contains_note(note) for note in other.notes)

    def contains_chord_ignore_octave(self, other: 'Chord') -> bool:
        return all(self.contains_note_ignore_octave(note) for note in other.notes)

    def __str__(self) -> str:
        return str(self.notes)

class PlayedChord:
    def __init__(self, duration: int):
        self.duration = duration
        self.notes: List[PlayedNote] = []

    def add_note(self, note: PlayedNote) -> None:
        self.notes.append(note)

if __name__ == "__main__":
    # Simple test
    c_major = Chord([
        Note(Tone.C, Shift.Natural, 4),
        Note(Tone.E, Shift.Natural, 4),
        Note(Tone.G, Shift.Natural, 4)
    ])
    print(f"C Major Chord: {c_major}")

    inverted = c_major.invert(1)
    print(f"First inversion: {inverted}")

    shifted = c_major.shift_octave(1)
    print(f"Shifted up one octave: {shifted}")

    played_chord = c_major.to_played_chord(500, 100)
    print(f"Played Chord: {played_chord.notes}")

    contains_note = c_major.contains_note(Note(Tone.E, Shift.Natural, 4))
    print(f"Contains E4: {contains_note}")

    contains_chord = c_major.contains_chord(Chord([Note(Tone.C, Shift.Natural, 4), Note(Tone.E, Shift.Natural, 4)]))
    print(f"Contains C4 and E4: {contains_chord}")
