package chillingMonsters;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class Utility {
	public static final int SPOILAGE_WARNING_DAYS = 5;
	public static final int TEXTFIELD_MAX_LENGTH = 9;
	public static final int CACHE_MAX_SIZE = 10;
	public static final float STD_TRANSITION_TIME = 0.2F;
	public static final double MIN_TOP_ANCHOR = 85;
	public static final double MAX_TOP_ANCHOR = 295;

	private static String[] suffixes = {
		//0     1     2     3     4     5     6     7     8     9
		"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
		//10    11    12    13    14    15    16    17    18    19
		"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
		//20    21    22    23    24    25    26    27    28    29
		"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
		//30    31
		"th", "st" };

	private static Map<String, String> knownAcronyms = new HashMap<String, String>() {{
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

	private static String[] recipeCategoryOrder = {"CREATED BY YOU", "BREAKFAST", "LUNCH", "DINNER", "SNACK"};

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
		int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(dt));

		return month + " " + day + suffixes[day];
	}

	public static String parseProperDate(String s) {
		LocalDate dateAdded = LocalDate.parse(s);
		long dayDiff = DAYS.between(dateAdded, LocalDate.now());

		if (dayDiff == 0) return "Today";
		else if (dayDiff == 1) return "Yesterday";

		DayOfWeek wkDay = dateAdded.getDayOfWeek();
		DayOfWeek todayWeekday = LocalDate.now().getDayOfWeek();
		String weekDayStr = wkDay.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
		if (0 < dayDiff && dayDiff < todayWeekday.getValue()) {
			return  "This " + weekDayStr;
		} else if (0 < dayDiff && dayDiff < todayWeekday.getValue() + 7){
			return  "Last " + weekDayStr;
		} else {
			return parseDate(Timestamp.valueOf(dateAdded.atStartOfDay()));
		}
	}

	public static Timestamp today() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static double parseQuantity(String s, double defaultValue) {
		if (s == null) return defaultValue;

		double result;
		try {
			result = Double.parseDouble(s);
			if (result < 0) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			result = defaultValue;
		}

		return result;
	}

	public static long parseID(String s, long defaultValue) {
		if (s == null) return defaultValue;

		long result;
		try {
			result = Long.parseLong(s);
			if (result < 0) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			result = defaultValue;
		}

		return result;
	}

	public static Comparator<String> parseRecipeComparator() {
		Comparator<String> recipeComparator = new Comparator<String>() {
			@Override public int compare(String s1, String s2) {
				for (String s : recipeCategoryOrder) {
					if (s.equals(s1.toUpperCase()) && !s.equals(s2.toUpperCase())) return -1;
					if (s.equals(s2.toUpperCase()) && !s.equals(s1.toUpperCase())) return 1;
				}

				return s1.compareTo(s2);
			}
		};

		return recipeComparator;
	}
}
