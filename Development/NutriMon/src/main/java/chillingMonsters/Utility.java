package chillingMonsters;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utility {
	public static int SPOILAGE_WARNING_DAYS = 5;
	public static int TEXTFIELD_MAX_LENGTH = 9;

	static String[] suffixes = {
		//0     1     2     3     4     5     6     7     8     9
		"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
		//10    11    12    13    14    15    16    17    18    19
		"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
		//20    21    22    23    24    25    26    27    28    29
		"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
		//30    31
		"th", "st" };

	static Map<String, String> knownAcronyms = new HashMap<String, String>() {{
		put("WITH SALT", "SALTED");
		put("LRG", "LARGE");
		put("SML", "SMALL");
		put("LO", "LOW");
		put("HI", "HIGH");
		put("W/", "WITH");
		put("WO/", "WITHOUT");
		put("LT", "LIGHT");
		put("HVY", "HEAVY");
		put("DRSNG", "DRESSING");
		put("FRZ", "FROZEN");
		put("PDR", "POWDERED");
		put("CRM", "CREAM");
		put("CKD", "COOKED");
		put("BLD", "BOILED");
		put("DRND", "DRAINED");
		put("BTLD", "BOTTLED");
		put("PLN", "PLAIN");
		put("FRSH", "FRESH");
		put("SCRMBLD", "SCRAMBLED");
		put("VIT", "VITAMIN");
		put("CND", "CANNED");
		put("CRM", "CREAM");
		put("RSTD", "ROASTED");
		put("SKN", "SKIN");
		put("CHOC", "CHOCOLATE");
		put("HLTHY", "HEALTHY");
		put("XCPT", "EXCEPT");
		put("JUC", "JUICE");
	}};

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

	public static String toCapitalized(String str) {
		String[] bySpace = str.toLowerCase().split(" ");
		String rtn = "";

		for (String s : bySpace) {
			rtn += toCapitalizedWord(s) + " ";
		}

		return rtn.trim();
	}

	public static String toCapitalizedWord(String s) {
		String str = knownAcronyms.get(s.toUpperCase());
		str = str == null ? s : str;

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

	public static Timestamp today() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static double parseQuantity(String s, double defaultValue) {
		double result;
		try {
			result = Double.parseDouble(s);
			if (result < 0) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			result = defaultValue;
		}

		return result;
	}
}
