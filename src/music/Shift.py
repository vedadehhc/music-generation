from enum import Enum
from typing import List, Optional

class Shift(Enum):
    DoubleFlat = -2
    Flat = -1
    Natural = 0
    Sharp = 1
    DoubleSharp = 2

    names: List[str] = ["bb", "b", "", "#", "##"]
    shifts: List['Shift'] = [DoubleFlat, Flat, Natural, Sharp, DoubleSharp]

    def getShiftID(self) -> int:
        return self.value

    def __str__(self) -> str:
        return self.names[self.value + 2]

    @classmethod
    def getShiftFromString(cls, s: str) -> Optional['Shift']:
        try:
            index = cls.names.index(s)
            return cls.shifts[index]
        except ValueError:
            return None

# Small tests
if __name__ == "__main__":
    assert Shift.Sharp.getShiftID() == 1
    assert str(Shift.Flat) == "b"
    assert Shift.getShiftFromString("#") == Shift.Sharp
    assert Shift.getShiftFromString("x") is None
    print("All tests passed!")
