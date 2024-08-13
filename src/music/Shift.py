from enum import Enum
from typing import List, Optional

class Shift(Enum):
    DoubleFlat = -2
    Flat = -1
    Natural = 0
    Sharp = 1
    DoubleSharp = 2

    @classmethod
    def names(cls) -> List[str]:
        return ["bb", "b", "", "#", "##"]

    @classmethod
    def shifts(cls) -> List['Shift']:
        return [cls.DoubleFlat, cls.Flat, cls.Natural, cls.Sharp, cls.DoubleSharp]

    def getShiftID(self) -> int:
        return self.value

    def __str__(self) -> str:
        index = self.value + 2
        if 0 <= index < len(self.names()):
            return self.names()[index]
        return ""  # or raise an exception if preferred

    @classmethod
    def getShiftFromString(cls, s: str) -> Optional['Shift']:
        if s in cls.names():
            index = cls.names().index(s)
            return cls.shifts()[index]
        elif s in ["b", "#"]:
            return cls(1 if s == "#" else -1)
        elif s in ["##", "bb"]:
            return cls(2 if s == "##" else -2)
        else:
            return None

# Small tests
if __name__ == "__main__":
    for shift in Shift:
        print(f"Testing {shift.name}:")
        print(f"  {shift.name}.value:", shift.value)
        print(f"  str({shift.name}):", str(shift))

    print("Shift.names():", Shift.names())
    print("len(Shift.names()):", len(Shift.names()))

    assert Shift.Sharp.getShiftID() == 1
    assert str(Shift.Flat) == "b"
    assert Shift.getShiftFromString("#") == Shift.Sharp
    assert Shift.getShiftFromString("x") is None

    # Additional tests
    assert str(Shift.DoubleFlat) == "bb"
    assert str(Shift.Natural) == ""
    assert str(Shift.DoubleSharp) == "##"
    assert Shift.getShiftFromString("bb") == Shift.DoubleFlat
    assert Shift.getShiftFromString("") == Shift.Natural
    assert Shift.getShiftFromString("##") == Shift.DoubleSharp

    print("All tests passed!")
