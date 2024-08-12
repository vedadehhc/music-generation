from typing import List, Tuple
from enum import Enum

class RhythmNote(Enum):
    Quarter = 1
    Half = 2
    Whole = 4

    @classmethod
    def get_notes(cls) -> List['RhythmNote']:
        return [cls.Quarter, cls.Half, cls.Whole]

    @classmethod
    def get_beats(cls) -> List[int]:
        return [note.value for note in cls.get_notes()]

class Rhythm:
    def __init__(self, bpm: int = 120):
        self.bpm = bpm
        self.beat_duration = (1000 * 60) // self.bpm
        self.durations: List[int] = []

    def add_note(self, note: RhythmNote) -> None:
        self.durations.append(self.beat_duration * note.value)

    def set_bpm(self, bpm: int) -> None:
        self.bpm = bpm
        self.beat_duration = (1000 * 60) // self.bpm

if __name__ == "__main__":
    # Simple test
    rhythm = Rhythm(120)
    rhythm.add_note(RhythmNote.Quarter)
    rhythm.add_note(RhythmNote.Half)
    rhythm.add_note(RhythmNote.Whole)
    print(f"Rhythm durations: {rhythm.durations}")

    rhythm.set_bpm(60)
    print(f"New beat duration after BPM change: {rhythm.beat_duration}")
