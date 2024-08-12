from typing import List, Optional
from enum import Enum
from .Note import Note, Tone, Shift
from .Chord import Chord

class KeyType(Enum):
    Major = 1
    NaturalMinor = 2
    HarmonicMinor = 3

class Key:
    majorTemplate = [2, 2, 1, 2, 2, 2, 1]
    naturalMinorTemplate = [2, 1, 2, 2, 1, 2, 2]
    harmonicMinorTemplate = [2, 1, 2, 2, 1, 3, 1]

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
        current_note = self.tonic
        for interval in self.template:
            next_id = current_note.get_id() + interval
            next_note = Note.get_note_from_id(next_id, Shift.Natural)
            self.scale.append(next_note)
            current_note = next_note

    def _generate_chords(self):
        for i in range(7):
            chord = Chord([self.get_note(i + 1), self.get_note(i + 3), self.get_note(i + 5)])
            self.chords[0].append(chord)

            chord_7th = Chord([self.get_note(i + 1), self.get_note(i + 3), self.get_note(i + 5), self.get_note(i + 7)])
            self.chords[1].append(chord_7th)

    def get_note(self, scale_pos: int) -> Note:
        if scale_pos > len(self.scale):
            octaves = (scale_pos - 1) // len(self.scale)
            index = (scale_pos - 1) % len(self.scale)
            return self.scale[index].shift_octave(octaves)
        elif scale_pos <= 0:
            octaves = ((scale_pos - len(self.scale)) // len(self.scale)) - 1
            index = (scale_pos - 1) % len(self.scale)
            return self.scale[index].shift_octave(octaves)
        else:
            return self.scale[scale_pos - 1]

    def get_chord(self, root_scale_pos: int, is_7th: bool = False, inversion: int = 0) -> Chord:
        chord_type = 1 if is_7th else 0
        if root_scale_pos > len(self.chords[chord_type]):
            octaves = (root_scale_pos - 1) // len(self.chords[chord_type])
            index = (root_scale_pos - 1) % len(self.chords[chord_type])
            return self.chords[chord_type][index].shift_octave(octaves).invert(inversion)
        elif root_scale_pos <= 0:
            octaves = ((root_scale_pos - len(self.chords[chord_type])) // len(self.chords[chord_type])) - 1
            index = (root_scale_pos - 1) % len(self.chords[chord_type])
            return self.chords[chord_type][index].shift_octave(octaves).invert(inversion)
        else:
            return self.chords[chord_type][root_scale_pos - 1].invert(inversion)

    def get_chord_from_bass(self, bass_scale_pos: int, is_7th: bool, inversion: int) -> Optional[Chord]:
        root = bass_scale_pos
        if inversion == 0:
            root = bass_scale_pos
        elif inversion == 1:
            root = bass_scale_pos - 2
        elif inversion == 2:
            root = bass_scale_pos - 4
        elif inversion == 3:
            root = bass_scale_pos - 6
        else:
            return None
        return self.get_chord(root, is_7th, inversion)

    def get_secondary_chord(self, key_scale_pos: int, chord_type: KeyType, root_scale_pos: int) -> Chord:
        root = self.get_note(key_scale_pos)
        new_key = Key(root, chord_type)
        return new_key.get_chord(root_scale_pos)

    def get_scale_length(self) -> int:
        return len(self.scale)

    def get_scale(self) -> List[Note]:
        return self.scale

    def __str__(self) -> str:
        return f"{self.tonic} {self.template}"

if __name__ == "__main__":
    # Simple test
    c_major = Key(Note(Tone.C, Shift.Natural, 4))
    print(f"Key: {c_major}")
    print(f"Scale: {[str(note) for note in c_major.get_scale()]}")
    print(f"Chord I: {c_major.get_chord(1)}")
    print(f"Chord V7: {c_major.get_chord(5, is_7th=True)}")
