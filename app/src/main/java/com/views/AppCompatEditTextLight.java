package com.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class AppCompatEditTextLight extends AppCompatEditText {

    Context context;
    int defStyle;

    public AppCompatEditTextLight(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AppCompatEditTextLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public AppCompatEditTextLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        try {
            Typeface tf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/UniversNextArabic-Light.ttf");
//                    "fonts/TrasandinaBold-Italic.otf");
            setTypeface(tf, this.defStyle);

        } catch (Exception e) {
            e.getMessage();
        }
    }
}
