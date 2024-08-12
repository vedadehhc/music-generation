from enum import Enum
from typing import List, Optional

class Tone(Enum):
    C = 0
    D = 2
    E = 4
    F = 5
    G = 7
    A = 9
    B = 11
    REST = -1

    notes: List[Optional['Tone']] = [C, None, D, None, E, F, None, G, None, A, None, B]
    tones: List['Tone'] = [A, B, C, D, E, F, G]

    def getNoteID(self) -> int:
        return self.value

    def __str__(self) -> str:
        if self == Tone.REST:
            return "REST"
        return chr(self.tones.index(self) + ord('A'))

    @classmethod
    def getToneFromString(cls, s: str) -> Optional['Tone']:
        if s == "REST":
            return cls.REST
        index = ord(s[0]) - ord('A')
        if 0 <= index < len(cls.tones):
            return cls.tones[index]
        return None

# Small tests
if __name__ == "__main__":
    assert Tone.C.getNoteID() == 0
    assert Tone.REST.getNoteID() == -1
    assert str(Tone.A) == "A"
    assert str(Tone.REST) == "REST"
    assert Tone.getToneFromString("C") == Tone.C
    assert Tone.getToneFromString("REST") == Tone.REST
    assert Tone.getToneFromString("X") is None
    print("All tests passed!")
