package com.freak.customview.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * @author Freak
 * @date 2019/8/22.
 */
public class CustomProgressBar extends LinearLayout {
    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
