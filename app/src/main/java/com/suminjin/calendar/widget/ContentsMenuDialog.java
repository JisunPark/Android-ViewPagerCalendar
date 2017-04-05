package com.suminjin.calendar.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.suminjin.calendar.R;


/**
 * Created by jspark on 2016-03-14.
 */
public class ContentsMenuDialog extends Dialog {

    public ContentsMenuDialog(Context context) {
        super(context);
        initViews();
    }

    private void initViews() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        setContentView(R.layout.dialog_contents_menu);

        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}