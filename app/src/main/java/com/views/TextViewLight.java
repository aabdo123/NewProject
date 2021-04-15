package com.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class TextViewLight extends AppCompatTextView {
    Context context;
    int defStyle;

    public TextViewLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this.defStyle = defStyle;
        init();
    }

    public TextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public TextViewLight(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        try {
            Typeface tf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/UniversNextArabic-Light.ttf");
//                    "fonts/TrasandinaRegular-Italic.otf");
            setTypeface(tf, this.defStyle);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}