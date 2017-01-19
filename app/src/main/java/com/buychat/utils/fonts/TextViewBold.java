package com.buychat.utils.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Hos_Logicbox on 12/06/16.
 */
public class TextViewBold extends TextView {


    public TextViewBold(Context context) {
        super(context);
        createFont();
    }

    public TextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public TextViewBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),"HelveticaNeueLTStdMedium.otf");
        setTypeface(font);
    }
}
