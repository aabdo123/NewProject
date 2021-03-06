package com.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class TextViewRegular extends AppCompatTextView {
    Context context;
    int defStyle;

    public TextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this.defStyle = defStyle;
        init();
    }

    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public TextViewRegular(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        try {
            Typeface tf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/universnextarabic_regular.ttf");
            setTypeface(tf, Typeface.BOLD);

        } catch (Exception e) {
            e.getMessage();
        }
    }
}
