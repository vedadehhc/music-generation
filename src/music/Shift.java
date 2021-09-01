package music;

import java.util.Arrays;
import java.util.List;

public enum Shift {
	DoubleFlat, Flat, Natural, Sharp, DoubleSharp;

	public final static List<String> names = Arrays.asList("bb", "b", "", "#", "##");
	public final static List<Shift> shifts = Arrays.asList(DoubleFlat, Flat, Natural, Sharp, DoubleSharp);

	public int getShiftID() {
		return shifts.indexOf(this) - 2;
	}

	public String toString() {
		return names.get(shifts.indexOf(this));
	}

	public static Shift getShiftFromString(String s) {
		int index = names.indexOf(s);
		if (index >= 0 && index < shifts.size()) {
			return shifts.get(index);
		} else {
			return null;
		}
	}
}