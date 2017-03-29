package com.suminjin.calendar;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by parkjisun on 2017. 3. 28..
 */

public class CalendarUtils {

    public static int[] getNewYearMonth(int currentYear, int currentMonth, int position) {
        int index = position - CalendarPagerAdapter.START_POSITION;
        int newMonth = currentMonth + index;
        int newYear = currentYear;
        if (newMonth >= 12) {
            newYear += newMonth / 12;
            newMonth %= 12;
        } else if (newMonth < 0) {
            int yearIndex = 0;
            while (newMonth < 0) {
                newMonth += 12;
                ++yearIndex;
            }
            newYear -= yearIndex;
            newMonth %= 12;
        }
        return new int[]{newYear, newMonth};
    }

    public static int getPosition(int selectedYear, int selectedMonth) {
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);

        int index = year * 12 + month;
        int selectedIndex = selectedYear * 12 + selectedMonth;
        return CalendarPagerAdapter.START_POSITION - index + selectedIndex;
    }

}
