package com.hour24.with.view.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoClickableToolbar extends Toolbar {

    public NoClickableToolbar(Context context) {
        super(context);
    }

    public NoClickableToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoClickableToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
