package com.buychat.utils.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Hos_Logicbox on 12/06/16.
 */
public class EditTextRegular extends EditText {


    public EditTextRegular(Context context) {
        super(context);
        createFont();
    }

    public EditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public EditTextRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),"Helvetica-Light.otf");
        setTypeface(font);
    }
}
