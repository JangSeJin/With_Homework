package com.hour24.with.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.WindowManager;

import com.hour24.with.R;

/**
 * Progress Dialog
 */
public class ProgressDialog extends Dialog {

    public ProgressDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.progress_dialog);
        initDialog();
    }

    private void initDialog() {

        try {

//            setCancelable(false);
//
//            setCanceledOnTouchOutside(false);

            // 투명하게 세팅
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            // 배경 DIm 처리 제거
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}