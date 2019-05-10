package com.freak.customview.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.freak.customview.R;

/**
 * Created by Administrator on 2019/5/10.
 */

public class CustomToolbar extends LinearLayout {
    private TextView mTextViewLeft;
    private TextView mTextViewTitle;
    private TextView mTextViewRight;

    public CustomToolbar(Context context) {
        super(context);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.coustom_toolbar, this);
        mTextViewLeft = findViewById(R.id.text_view_left);
        mTextViewTitle = findViewById(R.id.text_view_title);
        mTextViewRight = findViewById(R.id.text_view_right);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar);
        if (typedArray.hasValue(R.styleable.CustomToolbar_customTitle)) {
            mTextViewTitle.setText(typedArray.getString(R.styleable.CustomToolbar_customTitle));
            if (typedArray.hasValue(R.styleable.CustomToolbar_customTitleColor)) {
                mTextViewTitle.setTextColor(typedArray.getColor(R.styleable.CustomToolbar_customTitleColor, 0));
            }
        }

        if (typedArray.hasValue(R.styleable.CustomToolbar_customLeftTitle)) {
            mTextViewLeft.setText(typedArray.getString(R.styleable.CustomToolbar_customLeftTitle));
            if (typedArray.hasValue(R.styleable.CustomToolbar_customLeftColor)) {
                mTextViewLeft.setTextColor(typedArray.getColor(R.styleable.CustomToolbar_customLeftColor, 0));
            }
        }

        if (typedArray.hasValue(R.styleable.CustomToolbar_customRightTitle)) {
            mTextViewRight.setText(typedArray.getString(R.styleable.CustomToolbar_customRightTitle));
            if (typedArray.hasValue(R.styleable.CustomToolbar_customRightColor)) {
                mTextViewRight.setTextColor(typedArray.getColor(R.styleable.CustomToolbar_customRightColor, 0));
            }
        }
        int customLeftTitlePaddingLeft = (int) typedArray.getDimension(R.styleable.CustomToolbar_customLeftTitlePaddingLeft, 0);
        int customLeftTitlePaddingRight = (int) typedArray.getDimension(R.styleable.CustomToolbar_customLeftTitlePaddingRight, 0);
        int customLeftTitlePaddingBottom = (int) typedArray.getDimension(R.styleable.CustomToolbar_customLeftTitlePaddingBottom, 0);
        int customLeftTitlePaddingTop = (int) typedArray.getDimension(R.styleable.CustomToolbar_customLeftTitlePaddingTop, 0);
        mTextViewLeft.setPadding(customLeftTitlePaddingLeft, customLeftTitlePaddingTop, customLeftTitlePaddingRight, customLeftTitlePaddingBottom);
        int customRightTitlePaddingLeft = (int) typedArray.getDimension(R.styleable.CustomToolbar_customRightTitlePaddingLeft, 0);
        int customRightTitlePaddingRight = (int) typedArray.getDimension(R.styleable.CustomToolbar_customRightTitlePaddingRight, 0);
        int customRightTitlePaddingBottom = (int) typedArray.getDimension(R.styleable.CustomToolbar_customRightTitlePaddingBottom, 0);
        int customRightTitlePaddingTop = (int) typedArray.getDimension(R.styleable.CustomToolbar_customRightTitlePaddingTop, 0);
        mTextViewRight.setPadding(customRightTitlePaddingLeft, customRightTitlePaddingTop, customRightTitlePaddingRight, customRightTitlePaddingBottom);
        if (typedArray.hasValue(R.styleable.CustomToolbar_customLeftBackground)) {
            mTextViewLeft.setText("");
            mTextViewLeft.setPadding(customLeftTitlePaddingLeft, customLeftTitlePaddingTop, customLeftTitlePaddingRight, customLeftTitlePaddingBottom);
            mTextViewLeft.setBackground(typedArray.getDrawable(R.styleable.CustomToolbar_customLeftBackground));
        }
        if (typedArray.hasValue(R.styleable.CustomToolbar_customRightBackground)) {
            mTextViewRight.setText("");
            mTextViewRight.setPadding(customLeftTitlePaddingLeft, customLeftTitlePaddingTop, customLeftTitlePaddingRight, customLeftTitlePaddingBottom);
            mTextViewRight.setBackground(typedArray.getDrawable(R.styleable.CustomToolbar_customRightBackground));
        }
        typedArray.recycle();
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
