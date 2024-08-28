from enum import Enum
from typing import List, Optional
from Note import Note
from Chord import Chord
from Tone import Tone
from Shift import Shift

class KeyType(Enum):
    Major = 1
    NaturalMinor = 2
    HarmonicMinor = 3

class Key:
    majorTemplate: List[int] = [2, 2, 1, 2, 2, 2, 1]
    naturalMinorTemplate: List[int] = [2, 1, 2, 2, 1, 2, 2]
    harmonicMinorTemplate: List[int] = [2, 1, 2, 2, 1, 3, 1]

    def __init__(self, tonic: Note, key_type: KeyType = KeyType.Major):
        self.tonic = tonic
        self.scale: List[Note] = []
        self.chords: List[List[Chord]] = [[], []]

        if key_type == KeyType.Major:
            self.template = self.majorTemplate
        elif key_type == KeyType.NaturalMinor:
            self.template = self.naturalMinorTemplate
        elif key_type == KeyType.HarmonicMinor:
            self.template = self.harmonicMinorTemplate
        else:
            self.template = self.majorTemplate

        self._generate_scale()
        self._generate_chords()

    def _generate_scale(self):
        self.scale = [self.tonic]
        octave = self.tonic.getOctave()

        starting_point = Tone.tones.index(self.tonic.getTone())
        for i in range(1, 7):
            tone_index = (starting_point + i) % len(Tone.tones)
            if Tone.tones[tone_index] == Tone.C:
                octave += 1

            note = Note(Tone.tones[tone_index], Shift.Natural, octave)
            diff = (note.getID() - self.scale[i - 1].getID()) - self.template[i - 1]

            if diff == -2:
                shift = Shift.DoubleSharp
            elif diff == -1:
                shift = Shift.Sharp
            elif diff == 1:
                shift = Shift.Flat
            elif diff == 2:
                shift = Shift.DoubleFlat
            else:
                shift = Shift.Natural

            self.scale.append(Note(Tone.tones[tone_index], shift, octave))

    def _generate_chords(self):
        for i in range(7):
            triad = Chord()
            seventh = Chord()
            for j in range(3):
                triad.addNote(self.getNote(i + 1 + 2*j))
                seventh.addNote(self.getNote(i + 1 + 2*j))
            seventh.addNote(self.getNote(i + 7))
            self.chords[0].append(triad)
            self.chords[1].append(seventh)

    def getNote(self, scale_pos: int) -> Note:
        if scale_pos > 7:
            octaves = (scale_pos - 1) // 7
            return self.scale[scale_pos - 1 - 7 * octaves].shiftOctave(octaves)
        if scale_pos <= 0:
            octaves = ((scale_pos - 1) // 7) - 1
            return self.scale[scale_pos - 1 - 7 * octaves].shiftOctave(octaves)
        return self.scale[scale_pos - 1]

    def getChord(self, root_scale_pos: int, is_seventh: bool = False, inversion: int = 0) -> Chord:
        chord_type = 1 if is_seventh else 0
        if root_scale_pos > 7:
            octaves = (root_scale_pos - 1) // 7
            return self.chords[chord_type][root_scale_pos - 1 - 7 * octaves].shiftOctave(octaves).invert(inversion)
        if root_scale_pos <= 0:
            octaves = ((root_scale_pos - 1) // 7) - 1
            return self.chords[chord_type][root_scale_pos - 1 - 7 * octaves].shiftOctave(octaves).invert(inversion)
        return self.chords[chord_type][root_scale_pos - 1].invert(inversion)

    def getChordFromBass(self, bass_scale_pos: int, is_seventh: bool, inversion: int) -> Optional[Chord]:
        if inversion not in range(4):
            return None
        root = bass_scale_pos - 2 * inversion
        return self.getChord(root, is_seventh, inversion)

    def getSecondaryChord(self, key_scale_pos: int, chord_type: KeyType, root_scale_pos: int) -> Chord:
        root = self.getNote(key_scale_pos)
        new_key = Key(root, chord_type)
        return new_key.getChord(root_scale_pos)

    def getScaleLength(self) -> int:
        return len(self.scale)

    def getScale(self) -> List[Note]:
        return self.scale

    def __str__(self) -> str:
        return f"{self.tonic} Major"

# Small tests
if __name__ == "__main__":
    c_major = Key(Note.getNoteFromString("C4"))
    assert str(c_major) == "C4 Major"
    assert c_major.getNote(1) == Note.getNoteFromString("C4")
    assert c_major.getNote(8) == Note.getNoteFromString("C5")
    assert len(c_major.getScale()) == 7
    assert str(c_major.getChord(1)) == "[C4, E4, G4]"
    assert str(c_major.getChord(1, True)) == "[C4, E4, G4, B4]"
    assert str(c_major.getChordFromBass(3, False, 1)) == "[C4, F4, A4]"
    print("All tests passed!")
