package com.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * Created by malikabuqauod on 7/16/17.
 */

public class ButtonBold extends Button {

    Context context;
    int defStyle;

    public ButtonBold(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ButtonBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ButtonBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.defStyle = defStyleAttr;
        init();
    }

    private void init() {
        try {
            Typeface tf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/UniversNextArabic-Regular.ttf");
            setTypeface(tf, Typeface.BOLD);

        } catch (Exception e) {
            e.getMessage();
        }
    }
}
