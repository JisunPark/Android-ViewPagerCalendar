package com.suminjin.calendar;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.suminjin.calendar.utils.CalendarUtils;
import com.suminjin.calendar.widget.MonthPickerDialog;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by parkjisun on 2017. 3. 27..
 */

public class CalendarActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Calendar cal = Calendar.getInstance(Locale.KOREA);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);

        final TextView txtYear = (TextView) findViewById(R.id.txtYear);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        // 년월 표시
        txtYear.setText(getTitleString(year, month));
        txtYear.setTag(new int[]{year, month});
        txtYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final int[] temp = (int[]) view.getTag();
                MonthPickerDialog dialog = new MonthPickerDialog(CalendarActivity.this);
                dialog.setMonth(temp[0], temp[1]);
                dialog.setOnConfirmClickListener(new MonthPickerDialog.OnConfirmClickListener() {
                    @Override
                    public void onClickedConfirm(int selectedYear, int selectedMonth) {
                        viewPager.setCurrentItem(CalendarUtils.getPosition(selectedYear, selectedMonth));
                    }
                });
                dialog.show();
            }
        });

        // 달력 viewPager 설정
        viewPager.setAdapter(new CalendarPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int[] temp = CalendarUtils.getNewYearMonth(year, month, position);
                txtYear.setText(getTitleString(temp[0], temp[1]));
                txtYear.setTag(temp);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 양쪽 방향으로 다 넘길 수 있도록 중간에서 시작
        viewPager.setCurrentItem(CalendarPagerAdapter.START_POSITION);
        viewPager.setOffscreenPageLimit(10);

        // 오늘 버튼
        findViewById(R.id.txtToday).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(CalendarPagerAdapter.START_POSITION, true);
            }
        });

        final int menuFragmentHeight = getResources().getDimensionPixelSize(R.dimen.menu_fragment_height);
        final View layoutCalendar = findViewById(R.id.layoutCalendar);
        final View layoutTitle = findViewById(R.id.layoutTitle);

        // 메뉴 버튼
        final int menuAnimDuration = getResources().getInteger(R.integer.menu_anim_duration);
        findViewById(R.id.txtMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.isSelected()) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(layoutTitle, "rotationX", 180, 360);
                    animator.setDuration(menuAnimDuration);
                    animator.start();

                    ObjectAnimator mover = ObjectAnimator.ofFloat(layoutCalendar, "translationY", 0);
                    mover.setDuration(menuAnimDuration);
                    mover.start();
                } else {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(layoutTitle, "rotationX", 0, 180);
                    animator.setDuration(menuAnimDuration);
                    animator.start();

                    ObjectAnimator mover = ObjectAnimator.ofFloat(layoutCalendar, "translationY", menuFragmentHeight);
                    mover.setDuration(menuAnimDuration);
                    mover.start();
                }
                view.setSelected(!view.isSelected());
                layoutTitle.setBackgroundResource(view.isSelected() ? R.color.menu_bg : R.color.title_bg);
            }
        });
    }

    private String getTitleString(int year, int month) {
        return year + "/" + (month + 1);
    }
}
