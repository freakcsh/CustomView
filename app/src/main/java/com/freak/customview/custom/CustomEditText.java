package com.freak.customview.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.freak.customview.R;

/**
 * @author Freak
 * @date 2019/8/22.
 */
public class CustomEditText extends LinearLayout {
    private TextView text_view;
    private EditText edit_text;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.coustom_edit_text, this);
        text_view = findViewById(R.id.text_view);
        edit_text = findViewById(R.id.edit_text);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        if (typedArray.hasValue(R.styleable.CustomEditText_editTextHint)){
            edit_text.setHint(typedArray.getString(R.styleable.CustomEditText_editTextHint));
        }
        if (typedArray.hasValue(R.styleable.CustomEditText_editTextText)){
            edit_text.setText(typedArray.getString(R.styleable.CustomEditText_editTextText));
        }
        if (typedArray.hasValue(R.styleable.CustomEditText_titleText)){
            text_view.setText(typedArray.getString(R.styleable.CustomEditText_titleText));
        }
        typedArray.recycle();
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
