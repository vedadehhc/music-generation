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

    def __init__(self, *args):
        if len(args) == 1 and isinstance(args[0], str):
            s = args[0]
            self.tone = Tone.getToneFromString(s[0])
            self.shift = Shift.Natural
            end = 1

            if len(s) >= 2:
                shift_str = s[1:3] if len(s) >= 3 else s[1]
                shift = Shift.getShiftFromString(shift_str)
                if shift is not None:
                    self.shift = shift
                    end = 3 if len(shift_str) == 2 else 2

            try:
                self.octave = int(s[end:])
            except ValueError:
                raise ValueError(f"Invalid octave in note string: {s}")

        elif len(args) == 3:
            self.tone, self.shift, self.octave = args
        elif len(args) == 1 and isinstance(args[0], Tone):
            self.tone = args[0]
            self.shift = Shift.Natural
            self.octave = 0
        else:
            raise ValueError("Invalid arguments for Note constructor")

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
