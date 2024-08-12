from typing import List, Optional
from enum import Enum

class Tone(Enum):
    C = 0
    D = 2
    E = 4
    F = 5
    G = 7
    A = 9
    B = 11
    REST = -1

    @classmethod
    def get_notes(cls) -> List[Optional['Tone']]:
        return [cls.C, None, cls.D, None, cls.E, cls.F, None, cls.G, None, cls.A, None, cls.B]

    @classmethod
    def get_tones(cls) -> List['Tone']:
        return [cls.A, cls.B, cls.C, cls.D, cls.E, cls.F, cls.G]

    def get_note_id(self) -> int:
        return self.value

    @classmethod
    def get_tone_from_string(cls, s: str) -> Optional['Tone']:
        index = ord(s[0]) - ord('A')
        tones = cls.get_tones()
        return tones[index] if 0 <= index < len(tones) else None

class Shift(Enum):
    DoubleFlat = -2
    Flat = -1
    Natural = 0
    Sharp = 1
    DoubleSharp = 2

    @classmethod
    def get_shifts(cls) -> List['Shift']:
        return [cls.DoubleFlat, cls.Flat, cls.Natural, cls.Sharp, cls.DoubleSharp]

    @classmethod
    def get_shift_from_string(cls, s: str) -> Optional['Shift']:
        shift_map = {"bb": cls.DoubleFlat, "b": cls.Flat, "": cls.Natural, "#": cls.Sharp, "##": cls.DoubleSharp}
        return shift_map.get(s)

class Note:
    def __init__(self, tone: Tone, shift: Shift = Shift.Natural, octave: int = 4):
        self.tone = tone
        self.shift = shift
        self.octave = octave

    @classmethod
    def get_note_from_id(cls, id: int, pref_shift: Shift) -> 'Note':
        shift_num = pref_shift.value
        octave = (id // 12) - 1
        tone_mod = (id - shift_num + 12) % 12

        tone = Tone.get_notes()[tone_mod]
        shift_id = shift_num + 2

        if tone is None:
            if shift_num <= 0:
                tone = Tone.get_notes()[(tone_mod + 11) % 12]
                shift_id += 1
            else:
                tone = Tone.get_notes()[(tone_mod + 1) % 12]
                shift_id -= 1

        return cls(tone, Shift.get_shifts()[shift_id], octave)

    @classmethod
    def get_note_from_string(cls, s: str) -> 'Note':
        tone = Tone.get_tone_from_string(s[0])
        shift = Shift.Natural
        end = 1

        if len(s) >= 3 and Shift.get_shift_from_string(s[1:3]) is not None:
            shift = Shift.get_shift_from_string(s[1:3])
            end = 3
        elif Shift.get_shift_from_string(s[1:2]) is not None:
            shift = Shift.get_shift_from_string(s[1:2])
            end = 2

        octave = int(s[end:])
        return cls(tone, shift, octave)

    def shift_octave(self, amt: int) -> 'Note':
        return Note(self.tone, self.shift, self.octave + amt)

    def get_id(self) -> int:
        if self.tone.get_note_id() < 0:
            return -1
        return (self.tone.get_note_id() + self.shift.value) + (12 * self.octave) + 12

    def to_played_note(self, duration: int = 0, volume: int = 64):
        return PlayedNote(self, duration, volume)

    def equals(self, other: 'Note') -> bool:
        return self.get_id() == other.get_id()

    def equals_ignore_octave(self, other: 'Note') -> bool:
        if self.get_id() < 0 or other.get_id() < 0:
            return False
        diff = abs(self.get_id() - other.get_id())
        return (diff % 12 == 0)

    def __str__(self) -> str:
        return f"{self.tone.name}{self.shift.name}{self.octave}"

class PlayedNote:
    def __init__(self, note: Note, duration: int = 0, volume: int = 64):
        self.note = note
        self.duration = duration
        self.volume = volume

if __name__ == "__main__":
    # Simple test
    note = Note.get_note_from_string("C#4")
    print(f"Note: {note}")
    print(f"Note ID: {note.get_id()}")

    played_note = note.to_played_note(duration=500, volume=100)
    print(f"Played Note: {played_note.note}, Duration: {played_note.duration}, Volume: {played_note.volume}")

    note_from_id = Note.get_note_from_id(61, Shift.Natural)
    print(f"Note from ID 61: {note_from_id}")

    shifted_note = note.shift_octave(1)
    print(f"Shifted Note: {shifted_note}")

    print(f"Equals: {note.equals(Note.get_note_from_string('C#4'))}")
    print(f"Equals Ignore Octave: {note.equals_ignore_octave(Note.get_note_from_string('C#5'))}")
