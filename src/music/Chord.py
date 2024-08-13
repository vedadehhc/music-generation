from typing import List, Optional
from .Note import Note
from .Shift import Shift

class Chord:
    romanNumeralChords: List[str] = ["I", "ii", "iii", "IV", "V", "vi", "viio"]

    def __init__(self, notes: Optional[List[Note]] = None):
        self.notes: List[Note] = notes if notes is not None else []

    def shiftOctave(self, amt: int) -> 'Chord':
        return Chord([note.shiftOctave(amt) for note in self.notes])

    def invert(self, inversions: int) -> 'Chord':
        if inversions <= 0 or inversions >= len(self.notes):
            return self

        chord = Chord()
        for i in range(inversions, len(self.notes)):
            chord.addNote(self.notes[i])
        for i in range(inversions):
            chord.addNote(self.notes[i].shiftOctave(1))

        return chord

    def addNote(self, note: Note) -> None:
        self.notes.append(note)

    def getNotes(self) -> List[Note]:
        return self.notes

    def getNotesAsArr(self) -> List[Note]:
        return self.notes.copy()

    def containsNote(self, note: Note) -> bool:
        return any(n == note for n in self.notes)

    def containsNoteIgnoreOctave(self, note: Note) -> bool:
        return any(n.equalsIgnoreOctave(note) for n in self.notes)

    def toPlayedChord(self, duration: int, volume: int = 80):
        # Placeholder for PlayedChord class, which hasn't been migrated yet
        return None

    def containsChord(self, other: 'Chord') -> bool:
        return all(self.containsNote(note) for note in other.notes)

    def containsChordIgnoreOctave(self, other: 'Chord') -> bool:
        return all(self.containsNoteIgnoreOctave(note) for note in other.notes)

    def __str__(self) -> str:
        return str(self.notes)

# Small tests
if __name__ == "__main__":
    c_note = Note.getNoteFromString("C4")
    e_note = Note.getNoteFromString("E4")
    g_note = Note.getNoteFromString("G4")

    c_major = Chord([c_note, e_note, g_note])

    assert len(c_major.getNotes()) == 3
    assert c_major.containsNote(c_note)
    assert c_major.containsNoteIgnoreOctave(Note.getNoteFromString("C5"))

    inverted = c_major.invert(1)
    assert inverted.getNotes()[0] == e_note
    assert inverted.getNotes()[1] == g_note
    assert inverted.getNotes()[2].equalsIgnoreOctave(c_note)

    shifted = c_major.shiftOctave(1)
    assert all(note.getOctave() == 5 for note in shifted.getNotes())

    print("All tests passed!")
