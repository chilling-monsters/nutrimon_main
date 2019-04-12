package chillingMonsters;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utility {
	public static final int SPOILAGE_WARNING_DAYS = 5;
	public static final int TEXTFIELD_MAX_LENGTH = 9;
	public static final int CACHE_MAX_SIZE = 10;
	public static final float STD_TRANSITION_TIME = 0.25F;
	public static final double MIN_TOP_ANCHOR = 65;
	public static final double MAX_TOP_ANCHOR = 295;

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
		put("FLAV", "FLAVOR");
		put("RTD", "READY TO DRINK");
		put("RTE", "READY TO EAT");
		put("RTS", "READY TO SERVE");
		put("COCNT", "COCONUT");
		put("BF", "BEEF");
		put("PRK", "PORK");
		put("DRK", "DRINK");
		put("FRSTD", "FROSTED");
		put("MARSHMLLW", "MARSHMALLOW");
		put("UNCKD", "UNCOOKED");
		put("SPRD", "SPREAD");
		put("WHL", "WHOLE");
		put("SAU", "SAUCE");
		put("NOVLT", "NOVELTY");
		put("VAN", "VANILLA");
		put("SWT", "SWEET");
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
		String[] bySpace = str.toLowerCase().split("-|\\ ");
		String rtn = "";

		for (String s : bySpace) {
			rtn += toCapitalizedWord(s) + " ";
		}

		return rtn.trim();
	}

	public static String toCapitalizedWord(String s) {
		String str = knownAcronyms.get(s.toUpperCase());
		str = str == null ? s : str;

		StringBuilder strB = new StringBuilder(str);
		int i;
		for (i = 0; i < strB.length(); i++) {
			Character c = strB.charAt(i);
			if (Character.isLetter(c)) {
				strB.setCharAt(i, Character.toUpperCase(c));
				break;
			}
		}

		if (i < strB.length() - 1) {
			return strB.substring(0, i + 1) + strB.substring(i + 1).toLowerCase();
		} else {
			return strB.toString();
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
