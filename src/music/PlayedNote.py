from typing import Optional
from .Note import Note
from .Tone import Tone
from .Shift import Shift

class PlayedNote:
    """
    Represents a musical note with duration and volume.

    Constructors:
    - PlayedNote(note: Note, duration: int = 500, volume: int = 80)
    - PlayedNote.from_components(tone: Tone, shift: Shift, octave: int, duration: int = 500, volume: int = 80)
    - PlayedNote.from_tone_octave(tone: Tone, octave: int, duration: int = 500, volume: int = 80)
    """

    def __init__(self, note: Note, duration: int = 500, volume: int = 80):
        self.note = note
        self.duration = duration
        self.volume = volume

    @classmethod
    def from_components(cls, tone: Tone, shift: Shift, octave: int, duration: int = 500, volume: int = 80) -> 'PlayedNote':
        note = Note(tone, shift, octave)
        return cls(note, duration, volume)

    @classmethod
    def from_tone_octave(cls, tone: Tone, octave: int, duration: int = 500, volume: int = 80) -> 'PlayedNote':
        return cls.from_components(tone, Shift.Natural, octave, duration, volume)

    def shiftOctave(self, amt: int) -> 'PlayedNote':
        new_note = self.note.shiftOctave(amt)
        return PlayedNote(new_note, self.duration, self.volume)

    def getNote(self) -> Note:
        return self.note

    def getID(self) -> int:
        return self.note.getID()

    def getDuration(self) -> int:
        return self.duration

    def getVolume(self) -> int:
        return self.volume

# Small tests
if __name__ == "__main__":
    # Test creation and basic properties
    note = Note(Tone.C, Shift.Sharp, 4)
    played_note = PlayedNote(note, 600, 90)
    assert played_note.getNote() == note
    assert played_note.getDuration() == 600
    assert played_note.getVolume() == 90

    # Test alternative constructors
    played_note2 = PlayedNote.from_components(Tone.D, Shift.Flat, 3)
    assert played_note2.getNote().getTone() == Tone.D
    assert played_note2.getNote().getShift() == Shift.Flat
    assert played_note2.getNote().getOctave() == 3
    assert played_note2.getDuration() == 500  # default duration
    assert played_note2.getVolume() == 80  # default volume

    played_note3 = PlayedNote.from_tone_octave(Tone.E, 5)
    assert played_note3.getNote().getTone() == Tone.E
    assert played_note3.getNote().getShift() == Shift.Natural
    assert played_note3.getNote().getOctave() == 5

    # Test constructor with only Note
    played_note4 = PlayedNote(Note(Tone.F, Shift.Sharp, 4))
    assert played_note4.getNote().getTone() == Tone.F
    assert played_note4.getNote().getShift() == Shift.Sharp
    assert played_note4.getNote().getOctave() == 4
    assert played_note4.getDuration() == 500  # default duration
    assert played_note4.getVolume() == 80  # default volume

    # Test octave shifting
    shifted_note = played_note.shiftOctave(1)
    assert shifted_note.getNote().getOctave() == 5
    assert shifted_note.getDuration() == played_note.getDuration()
    assert shifted_note.getVolume() == played_note.getVolume()

    print("All tests passed!")
