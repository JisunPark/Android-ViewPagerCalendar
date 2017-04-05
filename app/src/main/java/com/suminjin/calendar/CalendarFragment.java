package com.suminjin.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suminjin.calendar.utils.CalendarUtils;
import com.suminjin.calendar.widget.CalendarScrollView;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by parkjisun on 2017. 3. 28..
 */

public class CalendarFragment extends Fragment {

    private static final String ARG_KEY_POSITION = "position";
    private CalendarScrollView scrollViewCalendar;

    public static CalendarFragment newInstance(int position) {
        CalendarFragment f = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_KEY_POSITION, position);
        f.setArguments(args);
        return f;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (scrollViewCalendar != null) {
                scrollViewCalendar.closeContents();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // position에 맞는 년,월 계산
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        int position = getArguments().getInt(ARG_KEY_POSITION);
        int[] temp = CalendarUtils.getNewYearMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), position);

        // 1일로 설정하고 시작하는 요일 위치와 마지막 날짜 가져오기
        final int year = temp[0];
        final int month = temp[1];
        cal.set(year, month, 1);
        final int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        final int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 달력 설정
        scrollViewCalendar = (CalendarScrollView) view.findViewById(R.id.scrollViewCalendar);
        scrollViewCalendar.post(new Runnable() {
            @Override
            public void run() {
                scrollViewCalendar.setDayCellLayout(scrollViewCalendar.getMeasuredHeight(), year, month, dayOfWeek, lastDay);
            }
        });
    }

}
