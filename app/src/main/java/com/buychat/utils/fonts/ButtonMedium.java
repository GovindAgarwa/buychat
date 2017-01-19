package com.buychat.utils.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Hos_Logicbox on 12/06/16.
 */
public class ButtonMedium extends Button {


    public ButtonMedium(Context context) {
        super(context);
        createFont();
    }

    public ButtonMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public ButtonMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),"HelveticaNeueLTStdMedium.otf");
        setTypeface(font);
    }
}
