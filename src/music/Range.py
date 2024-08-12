from typing import List
from .Note import Note
from .Key import Key  # This import might need to be adjusted based on the Key class implementation

class Range:
    # Bass, Tenor, Alto, and Soprano voice Ranges
    voice_ranges: List['Range'] = [
        None,  # Placeholder, will be initialized after class definition
        None,
        None,
        None
    ]

    def __init__(self, min_id: int, max_id: int):
        if min_id > max_id:
            self.min_id = max_id
            self.max_id = min_id
        else:
            self.min_id = min_id
            self.max_id = max_id

    @classmethod
    def from_notes(cls, lowest_note: Note, highest_note: Note) -> 'Range':
        return cls(lowest_note.get_id(), highest_note.get_id())

    def check_note(self, note: Note) -> int:
        """
        Returns:
        -1 if note is below range
        0 if note is in range
        1 if note is above range
        """
        if note.get_id() < self.min_id:
            return -1
        if note.get_id() > self.max_id:
            return 1
        return 0

    def get_in_key(self, key: Key) -> List[Note]:
        lowest = 0
        while key.get_note(lowest).get_id() > self.min_id:
            lowest -= 1
        while key.get_note(lowest).get_id() < self.min_id:
            lowest += 1

        highest = 0
        while key.get_note(highest).get_id() < self.max_id:
            highest += 1
        while key.get_note(highest).get_id() > self.max_id:
            highest -= 1

        return [key.get_note(i) for i in range(lowest, highest + 1)]

    def get_min_id(self) -> int:
        return self.min_id

    def get_max_id(self) -> int:
        return self.max_id

# Initialize voice_ranges after class definition
Range.voice_ranges = [
    Range(40, 57),  # Bass
    Range(48, 67),  # Tenor
    Range(55, 72),  # Alto
    Range(60, 79)   # Soprano
]

if __name__ == "__main__":
    # Simple test
    test_range = Range(50, 70)
    print(f"Range: {test_range.get_min_id()} to {test_range.get_max_id()}")

    test_note = Note.get_note_from_id(60, Note.Shift.Natural)
    print(f"Note {test_note} in range: {test_range.check_note(test_note)}")

    # This part might need adjustment based on the Key class implementation
    # from .Key import Key
    # test_key = Key("C")
    # notes_in_key = test_range.get_in_key(test_key)
    # print(f"Notes in key within range: {[str(note) for note in notes_in_key]}")

    print(f"Voice ranges: {[f'{r.get_min_id()}-{r.get_max_id()}' for r in Range.voice_ranges]}")
