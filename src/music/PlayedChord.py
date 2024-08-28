from typing import List
from PlayedNote import PlayedNote

class PlayedChord:
    def __init__(self, duration: int = 500):
        self.notes: List[PlayedNote] = []
        self.duration: int = duration

    def shiftOctave(self, amt: int) -> 'PlayedChord':
        chord = PlayedChord(self.duration)
        for note in self.notes:
            chord.addNote(note.shiftOctave(amt))
        return chord

    def invert(self, inversions: int) -> 'PlayedChord':
        if inversions <= 0 or inversions >= len(self.notes):
            return self

        chord = PlayedChord(self.duration)
        for i in range(inversions, len(self.notes)):
            chord.addNote(self.notes[i])
        for i in range(inversions):
            chord.addNote(self.notes[i].shiftOctave(1))

        return chord

    def addNote(self, note: PlayedNote) -> None:
        self.notes.append(note)

    def getNotes(self) -> List[PlayedNote]:
        return self.notes

    def getDuration(self) -> int:
        return self.duration

# Small tests
if __name__ == "__main__":
    from Note import Note
    from Tone import Tone
    from Shift import Shift

    # Create a C major chord
    c_note = Note(Tone.C, Shift.Natural, 4)
    e_note = Note(Tone.E, Shift.Natural, 4)
    g_note = Note(Tone.G, Shift.Natural, 4)

    played_c = PlayedNote(c_note, 500, 80)
    played_e = PlayedNote(e_note, 500, 80)
    played_g = PlayedNote(g_note, 500, 80)

    c_major = PlayedChord(500)
    c_major.addNote(played_c)
    c_major.addNote(played_e)
    c_major.addNote(played_g)

    assert len(c_major.getNotes()) == 3
    assert c_major.getDuration() == 500

    # Test inversion
    inverted = c_major.invert(1)
    assert inverted.getNotes()[0].getNote().getTone() == Tone.E
    assert inverted.getNotes()[1].getNote().getTone() == Tone.G
    assert inverted.getNotes()[2].getNote().getTone() == Tone.C
    assert inverted.getNotes()[2].getNote().getOctave() == 5

    # Test octave shift
    shifted = c_major.shiftOctave(1)
    assert all(note.getNote().getOctave() == 5 for note in shifted.getNotes())

    print("All tests passed!")
