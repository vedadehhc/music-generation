from typing import List, Optional
from Note import Note
from Key import Key

class Range:
    # Bass, Tenor, Alto, and Soprano voice Ranges
    voiceRanges: List['Range'] = [
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
        return cls(lowest_note.getID(), highest_note.getID())

    def check_note(self, note: Note) -> int:
        """
        Returns:
        -1 if note is below range
        0 if note is in range
        1 if note is above range
        """
        if note.getID() < self.min_id:
            return -1
        if note.getID() > self.max_id:
            return 1
        return 0

    def get_in_key(self, key: Key) -> List[Note]:
        lowest = 0
        while key.getNote(lowest).getID() > self.min_id:
            lowest -= 1
        while key.getNote(lowest).getID() < self.min_id:
            lowest += 1

        highest = 0
        while key.getNote(highest).getID() < self.max_id:
            highest += 1
        while key.getNote(highest).getID() > self.max_id:
            highest -= 1

        return [key.getNote(i) for i in range(lowest, highest + 1)]

    def get_min_id(self) -> int:
        return self.min_id

    def get_max_id(self) -> int:
        return self.max_id

# Initialize voiceRanges after class definition
Range.voiceRanges = [
    Range(40, 57),  # Bass
    Range(48, 67),  # Tenor
    Range(55, 72),  # Alto
    Range(60, 79)   # Soprano
]

# Small tests
if __name__ == "__main__":
    from Tone import Tone
    from Shift import Shift

    # Test Range creation and methods
    test_range = Range(48, 60)
    assert test_range.get_min_id() == 48
    assert test_range.get_max_id() == 60

    # Test note checking
    c4 = Note(Tone.C, Shift.Natural, 4)  # Middle C, ID 60
    assert test_range.check_note(c4) == 0

    b3 = Note(Tone.B, Shift.Natural, 3)  # B3, ID 59
    assert test_range.check_note(b3) == 0

    c3 = Note(Tone.C, Shift.Natural, 3)  # C3, ID 48
    assert test_range.check_note(c3) == 0

    b2 = Note(Tone.B, Shift.Natural, 2)  # B2, ID 47
    assert test_range.check_note(b2) == -1

    c5 = Note(Tone.C, Shift.Natural, 5)  # C5, ID 72
    assert test_range.check_note(c5) == 1

    # Test voice ranges
    assert len(Range.voiceRanges) == 4
    assert Range.voiceRanges[0].get_min_id() == 40  # Bass
    assert Range.voiceRanges[3].get_max_id() == 79  # Soprano

    print("All tests passed!")
