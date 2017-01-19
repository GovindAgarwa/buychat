package com.buychat.utils.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Hos_Logicbox on 12/06/16.
 */
public class TextViewVariane extends TextView {


    public TextViewVariane(Context context) {
        super(context);
        createFont();
    }

    public TextViewVariane(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public TextViewVariane(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),"Variane Script.ttf");
        setTypeface(font);
    }
}
