package com.suminjin.calendar;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

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
        txtYear.setText(year + "/" + (month + 1));
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
                txtYear.setText(temp[0] + "/" + (temp[1] + 1));
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
                viewPager.setCurrentItem(CalendarPagerAdapter.START_POSITION);
            }
        });

        final View layoutTitle = findViewById(R.id.layoutTitle);
        // 메뉴 버튼
        findViewById(R.id.txtMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.isSelected()) {
                    removeFragment();

                    ObjectAnimator animator = ObjectAnimator.ofFloat(layoutTitle, "rotationX", 180, 360);
                    animator.setDuration(300);
                    animator.start();
                } else {
                    setFragment();

                    ObjectAnimator animator = ObjectAnimator.ofFloat(layoutTitle, "rotationX", 0, 180);
                    animator.setDuration(300);
                    animator.start();
                }

                view.setSelected(!view.isSelected());
            }
        });
    }

    private void setFragment() {
        String className = TestFragment.class.getName();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(className);
        String tag = className;
        if (fragment == null) {
            fragment = Fragment.instantiate(this, className, null);
        } else {
            tag += "Extra";
            fragment = Fragment.instantiate(this, className, null);
        }

        if (fragment == null) {
            try {
                throw new Exception("Check " + className + " is exist Fragment");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int enter = R.anim.slide_in_down;
        int exit = R.anim.slide_out_up;
        if (enter != 0 && exit != 0) {
            ft.setCustomAnimations(enter, exit, enter, exit);
        }

        ft.add(R.id.layoutFragment, fragment, tag);

        ft.commit();
    }

    private void removeFragment() {
        final String className = TestFragment.class.getName();
        FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        final Fragment fragment = fm.findFragmentByTag(className);
//        ft.remove(fragment);
//        ft.commit();

        AnimationUtils.move(fragment.getView(), 0, 0, 0, -1, getResources().getInteger(R.integer.anim_duration), new AccelerateInterpolator(), new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ft.remove(fragment);
                ft.commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
