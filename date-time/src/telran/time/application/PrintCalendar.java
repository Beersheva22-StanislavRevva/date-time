package telran.time.application;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;

public class PrintCalendar {

	private static final String LANGUAGE_TAG = "en";
	private static final int YEAR_OFFSET = 10;
	private static final int WIDTH_FIELD = 4;
	private static Locale locale = Locale.forLanguageTag(LANGUAGE_TAG);
	private static DayOfWeek[] daysOfWeek = DayOfWeek.values();
	public static void main(String[] args)  {
		try {
			int monthYear[] = getMonthYear(args);
			setFirstDay(args);
			printCalendar(monthYear[0], monthYear[1]);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private static void setFirstDay(String[] args) throws Exception {
		DayOfWeek[] sourceDays = DayOfWeek.values();
		int daysOnWeek = sourceDays.length;
		DayOfWeek firstDay = sourceDays[0];
		if (args.length > 2) {
			try {
				firstDay = DayOfWeek.valueOf(args[2].toUpperCase());

			} catch (Exception e) {
				throw new Exception("wrong name of week day " + args[2]);
			}
		}
		if (firstDay != sourceDays[0]) {
			{
				
				int dayNumber = firstDay.getValue();
				for (int i = 0; i < daysOfWeek.length; i++) {
					int ind = dayNumber <= daysOnWeek ? dayNumber : dayNumber - daysOnWeek;
					daysOfWeek[i] = sourceDays[ind - 1];
					dayNumber++;
				}
			}
		}

	}
		
	

	private static void printCalendar(int month, int year) {
		printTitle(month, year);
		printWeekDays();
		printDates(month, year);
		
	}

	private static void printDates(int month, int year) {
		 int weekDayNumber = getFirstDay(month, year);
		int offset = getOffset(weekDayNumber);
		
		int nDays = YearMonth.of(year, month).lengthOfMonth();
		System.out.printf("%s", " ".repeat(offset));
		for(int date = 1; date <= nDays ; date++) {
			System.out.printf("%4d",date);
			if (++weekDayNumber > 7) {
				System.out.println();
				weekDayNumber = 1;
			}
		}
		
	}

	private static int getOffset(int weekDayNumber) {
		
		return (weekDayNumber - 1) * WIDTH_FIELD;
	}

	private static int getFirstDay(int month, int year) {
		
		LocalDate firstDateMonth = LocalDate.of(year, month, 1);
		int firstWeekDay = firstDateMonth.getDayOfWeek().getValue();
		int firstValue = daysOfWeek[0].getValue();
		int delta = firstWeekDay - firstValue + 1;

		return delta >= 0 ? delta : delta + daysOfWeek.length ;
	}

	private static void printWeekDays() {
		System.out.print("  ");
		Arrays.stream(daysOfWeek)
		.forEach(dw -> System.out.printf("%s ", dw.getDisplayName(TextStyle.SHORT, locale)));
		System.out.println();
		
	}

	private static void printTitle(int month, int year) {
		
		System.out.printf("%s%d, %s\n"," ".repeat(YEAR_OFFSET), year, Month.of(month).getDisplayName(TextStyle.FULL,
				locale ));
		
		
	}

	private static int[] getMonthYear(String[] args) throws Exception {
		
		return args.length == 0 ? getCurrentMonthYear() : getMonthYearArgs(args);
	}

	private static int[] getMonthYearArgs(String[] args) throws Exception{
		
		return new int[] {getMonthArgs(args), getYearArgs(args)};
	}

	private static int getYearArgs(String[] args) throws Exception{
		int res = LocalDate.now().getYear();
		if (args.length > 1) {
			try {
				res = Integer.parseInt(args[1]);
				if (res < 0) {
					throw new Exception("year must be a positive number");
				}
			} catch (NumberFormatException e) {
				throw new Exception("year must be a number");
			}
		}
		return res;
	}

	private static int getMonthArgs(String[] args) throws Exception{
		try {
			int res = Integer.parseInt(args[0]);
			if (res < 1 || res > 12) {
				throw new Exception("Month should be a number in the range [1-12]");
			}
			return res;
		} catch (NumberFormatException e) {
			throw new Exception("Month should be a number");
		}
		
		
	}

	private static int[] getCurrentMonthYear() {
		LocalDate current = LocalDate.now();
		return new int[] {current.getMonth().getValue(), current.getYear()};
	}

}