package com.suminjin.calendar.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;

import com.suminjin.calendar.R;

/**
 * Created by parkjisun on 2017. 3. 28..
 */

public class MonthPickerDialog extends Dialog {
    private OnConfirmClickListener onConfirmClickListener;
    private NumberPicker numberPickerYear;
    private NumberPicker numberPickerMonth;

    public MonthPickerDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_month_picker);

        numberPickerYear = (NumberPicker) findViewById(R.id.numberPickerYear);
        numberPickerYear.setMinValue(1000);
        numberPickerYear.setMaxValue(3000);

        numberPickerMonth = (NumberPicker) findViewById(R.id.numberPickerMonth);
        numberPickerMonth.setMaxValue(12);
        numberPickerMonth.setMinValue(1);

        findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onConfirmClickListener != null) {
                    onConfirmClickListener.onClickedConfirm(numberPickerYear.getValue(), numberPickerMonth.getValue() - 1);
                }
                dismiss();
            }
        });
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setMonth(int year, int month) {
        numberPickerYear.setValue(year);
        numberPickerMonth.setValue(month + 1);
    }

    public void setOnConfirmClickListener(OnConfirmClickListener listener) {
        onConfirmClickListener = listener;
    }

    public interface OnConfirmClickListener {
        void onClickedConfirm(int year, int month);
    }
}
