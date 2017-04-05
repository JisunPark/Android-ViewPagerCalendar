package com.suminjin.calendar.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.suminjin.calendar.R;

import java.util.ArrayList;

/**
 * Created by parkjisun on 2017. 3. 27..
 */

public class CalendarScrollView extends ScrollView {
    private Context context;
    private LinearLayout layoutCalendar;
    private ArrayList<View> contentsViewList;
    private View prevDayCell;
    private View prevContentsView;

    private int x = -1, y = -1;

    public CalendarScrollView(Context context) {
        this(context, null);
    }

    public CalendarScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    private void initViews(Context context) {
        this.context = context;

        // ScrollView 설정
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setFillViewport(true);
        setLayoutParams(p);
        setVerticalScrollBarEnabled(false);

        // ScrollView내의 LinearLayout 설정
        layoutCalendar = new LinearLayout(context);
        ScrollView.LayoutParams paramsLayout = new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
        layoutCalendar.setLayoutParams(paramsLayout);
        layoutCalendar.setOrientation(LinearLayout.VERTICAL);
        addView(layoutCalendar);

        if (isInEditMode()) {
            setDayCellLayout(1000, 2017, 3, 2, 31);
        }
    }

    public CalendarScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
    }

    public void setDayCellLayout(int height, int year, int month, int dayOfWeek, int lastDay) {
        // contents view의 리스트
        contentsViewList = new ArrayList<>();

        // 달력 그리기 위한 week count와 layout 높이 계산
        double days = dayOfWeek - 1 + lastDay;
        int weekCount = (int) Math.ceil(days / 7);
        int weekLayoutHeight = height / weekCount;

        LayoutInflater inflater = LayoutInflater.from(context);
        for (int i = 0; i < weekCount; i++) {
            // week layout 생성
            LinearLayout layoutWeek = new LinearLayout(context);
            LinearLayout.LayoutParams paramsWeek = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, weekLayoutHeight);
            layoutWeek.setLayoutParams(paramsWeek);
            for (int j = 0; j < 7; j++) {
                final LinearLayout layoutDayCell = (LinearLayout) inflater.inflate(R.layout.layout_calendar_day_cell, null);
                LinearLayout.LayoutParams paramsDayCell = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
                paramsDayCell.weight = 1;
                layoutDayCell.setLayoutParams(paramsDayCell);
                TextView txtDay = (TextView) layoutDayCell.findViewById(R.id.txtDay);
                int cellPosition = (i * 7) + j + 1;
                int day = cellPosition - (dayOfWeek - 1);
                if (day > 0 && day <= lastDay) {
                    txtDay.setText(Integer.toString(day));
                    // 토일요일은 다른 색깔로 표시
                    if (j == 0) {
                        txtDay.setTextColor(Color.rgb(0xff, 0x00, 0x00));
                    } else if (j == 6) {
                        txtDay.setTextColor(Color.rgb(0x00, 0x00, 0xff));
                    }
                } else {
                    txtDay.setText("");
                }
                layoutDayCell.setTag(new CellData(i, j, year, month, day));
                if (!txtDay.getText().toString().isEmpty()) {
                    layoutDayCell.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final CellData tag = (CellData) v.getTag();
                            View contentsView = contentsViewList.get(tag.rowIndex);

                            v.setSelected(!v.isSelected());

                            if (prevContentsView == null) {
                                showContentsView(contentsView, tag);
                                prevDayCell = v;
                                prevContentsView = contentsView;
                            } else {
                                hideContentsView(prevContentsView);
                                if (prevDayCell == v) {
                                    hideContentsView(contentsView);
                                    prevContentsView = null;
                                } else {
                                    prevDayCell.setSelected(false);
                                    prevDayCell = v;
                                    showContentsView(contentsView, tag);
                                    prevContentsView = contentsView;
                                }
                            }
                        }
                    });
                }
                layoutWeek.addView(layoutDayCell);
            }
            layoutCalendar.addView(layoutWeek);

            // 선택된 날짜에 대한 contents 영역
            View viewContents = inflater.inflate(R.layout.layout_calendar_day_contents, null);
            // 메뉴 팝업 띄우기
            viewContents.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x = (int) event.getRawX();
                            y = (int) event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            ContentsMenuDialog dialog = new ContentsMenuDialog(context);
                            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                            if (x >= 0 && y >= 0) {
                                params.gravity = Gravity.TOP | Gravity.LEFT;
                                params.x = x;
                                params.y = y;
                            }
                            dialog.show();
                            break;
                        default:
                    }

                    return true;
                }
            });
            LinearLayout.LayoutParams paramsContents = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.calendar_contents_height));
            viewContents.setLayoutParams(paramsContents);
            viewContents.setBackgroundResource(R.drawable.bg_day_contents);
            viewContents.setVisibility(View.GONE);
            contentsViewList.add(viewContents);
            layoutCalendar.addView(viewContents);
        }
    }

    private void hideContentsView(final View v) {
        v.setVisibility(View.GONE);
    }

    private void showContentsView(final View v, final CellData tag) {
        v.setVisibility(View.VISIBLE);
        TextView txtContents = (TextView) v.findViewById(R.id.txtContents);
        txtContents.setText((tag.month + 1) + "." + tag.day);

        post(new Runnable() {
            @Override
            public void run() {
                if (tag.rowIndex > 2) {
                    fullScroll(FOCUS_DOWN);
                } else {
                    fullScroll(FOCUS_UP);
                }
            }
        });
    }

    /**
     * 특정 날짜 선택 상태 해제
     */
    public void closeContents() {
        if (prevDayCell != null) {
            prevDayCell.setSelected(false);
            prevDayCell = null;
        }
        if (prevContentsView != null) {
            hideContentsView(prevContentsView);
            prevContentsView = null;
        }
    }

    public class CellData {
        public int rowIndex;
        public int colIndex;
        public int year;
        public int month;
        public int day;

        public CellData(int rowIndex, int colIndex, int year, int month, int day) {
            this.rowIndex = rowIndex;
            this.colIndex = colIndex;
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }
}
