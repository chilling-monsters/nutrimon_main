package chillingMonsters;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	static String[] suffixes = {
		//0     1     2     3     4     5     6     7     8     9
		"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
		//10    11    12    13    14    15    16    17    18    19
		"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
		//20    21    22    23    24    25    26    27    28    29
		"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
		//30    31
		"th", "st" };

	public static String parseFoodName(String name) {
		String[] byComma = name.split(",");
		String label = "";

		for (int i = 0; i < byComma.length; i++) {
			byComma[i] = toCapitalized(byComma[i]);
		}

		if (byComma.length > 1) {
			label += String.join(" ", byComma[1], byComma[0]);
		} else {
			label = byComma[0];
		}

		for (int i = 2; i < byComma.length; i++) {
			label += ", " + byComma[i];
		}

		return label;
	}

	private static String toCapitalized(String str) {
		String[] bySpace = str.toLowerCase().split(" ");
		String rtn = "";

		for (String s : bySpace) {
			rtn += toCapitalizedWord(s) + " ";
		}

		return rtn.trim();
	}

	private static String toCapitalizedWord(String str) {
		if (str.length() > 0) {
			return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
		} else {
			return str.toUpperCase();
		}
	}

	public static String parseDate(Timestamp dateTime) {
		Date dt = new Date((long) dateTime.getTime());
		int day = Integer.parseInt(new SimpleDateFormat("d").format(dt));
		String month = new SimpleDateFormat("MMM").format(dt);

		return month + " " + day + suffixes[day];
	}
}
