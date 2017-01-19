package com.buychat.pojos;

import android.animation.TimeInterpolator;

public class FAQItemModel {
    public final String description;
    public final String description2;
    public final int colorId1;
    public final int colorId2;
    public final TimeInterpolator interpolator;

    public FAQItemModel(String description,String desc2, int colorId1, int colorId2, TimeInterpolator interpolator) {
        this.description = description;
        this.colorId1 = colorId1;
        this.colorId2 = colorId2;
        this.interpolator = interpolator;
        this.description2=desc2;
    }
}