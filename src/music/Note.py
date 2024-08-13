from enum import Enum
from typing import List, Optional
from Shift import Shift
from Tone import Tone

class Note:
    @staticmethod
    def getNoteFromID(id: int, prefShift: Shift) -> 'Note':
        shiftNum = prefShift.getShiftID()

        octave = (id // 12) - 1
        toneMod = (id - shiftNum + 12) % 12

        tone = Tone.notes[toneMod]
        shiftID = shiftNum + 2

        if tone is None:
            if shiftNum <= 0:
                tone = Tone.notes[(toneMod + 11) % 12]
                shiftID += 1
            else:
                tone = Tone.notes[(toneMod + 1) % 12]
                shiftID -= 1

        return Note(tone, Shift.shifts[shiftID], octave)

    @staticmethod
    def getNoteFromString(s: str) -> 'Note':
        return Note(s)

    def __init__(self, tone: Tone, shift: Shift = Shift.Natural, octave: int = 0):
        self.tone = tone
        self.shift = shift
        self.octave = octave

    def __init__(self, s: str):
        self.tone = Tone.getToneFromString(s[0])

        self.shift = Shift.Natural
        end = 1

        if len(s) >= 3 and Shift.getShiftFromString(s[1:3]) is not None:
            self.shift = Shift.getShiftFromString(s[1:3])
            end = 3
        elif Shift.getShiftFromString(s[1:2]) is not None:
            self.shift = Shift.getShiftFromString(s[1:2])
            end = 2

        self.octave = int(s[end:])

    def shiftOctave(self, amt: int) -> 'Note':
        return Note(self.tone, self.shift, self.octave + amt)

    def getID(self) -> int:
        if self.tone.getNoteID() < 0:
            return -1
        return (self.tone.getNoteID() + self.shift.getShiftID()) + (12 * self.octave) + 12

    def getTone(self) -> Tone:
        return self.tone

    def getShift(self) -> Shift:
        return self.shift

    def getOctave(self) -> int:
        return self.octave

    def toPlayedNote(self, duration: int = 0, volume: int = 0):
        # Placeholder for PlayedNote class, which hasn't been migrated yet
        return None

    def __eq__(self, other: 'Note') -> bool:
        return self.getID() == other.getID()

    def equalsIgnoreOctave(self, other: 'Note') -> bool:
        if self.getID() < 0 or other.getID() < 0:
            return False
        diff = abs(self.getID() - other.getID())
        return (diff % 12 == 0)

    def __str__(self) -> str:
        return f"{self.tone}{self.shift}{self.octave}"

# Small tests
if __name__ == "__main__":
    assert str(Note(Tone.C, Shift.Sharp, 4)) == "C#4"
    assert Note.getNoteFromString("D#3").getID() == 51
    assert Note(Tone.A, Shift.Flat, 4) == Note(Tone.G, Shift.Sharp, 4)
    assert Note(Tone.C, Shift.Natural, 4).equalsIgnoreOctave(Note(Tone.C, Shift.Natural, 5))
    print("All tests passed!")
