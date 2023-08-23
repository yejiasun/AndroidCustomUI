package com.tengteng.ui;

import android.content.Context;
import android.util.TypedValue;

/**
 * @author yejiasun
 * @date Create on 12/14/22
 */

public class DpUtils {

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}
