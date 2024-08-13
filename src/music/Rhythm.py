from enum import Enum
from typing import List

class Rhythm:
    class RhythmNote(Enum):
        Quarter = 1
        Half = 2
        Whole = 4

        @classmethod
        def get_beats(cls, note: 'RhythmNote') -> int:
            return note.value

    def __init__(self, bpm: int = 120):
        self.bpm = bpm
        self.beat_duration = (1000 * 60) // self.bpm
        self.durations: List[int] = []

    def add_note(self, note: RhythmNote):
        self.durations.append(self.beat_duration * note.value)

    def set_bpm(self, bpm: int):
        self.bpm = bpm
        self.beat_duration = (1000 * 60) // self.bpm

# Small tests
if __name__ == "__main__":
    rhythm = Rhythm()
    assert rhythm.bpm == 120
    assert rhythm.beat_duration == 500

    rhythm.add_note(Rhythm.RhythmNote.Quarter)
    rhythm.add_note(Rhythm.RhythmNote.Half)
    rhythm.add_note(Rhythm.RhythmNote.Whole)
    assert rhythm.durations == [500, 1000, 2000]

    rhythm.set_bpm(60)
    assert rhythm.beat_duration == 1000

    print("All tests passed!")
