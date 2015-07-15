package com.eclubprague.cardashboard.tablet.utils;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Utility class for views.
 *
 * Created by Michael on 14. 4. 2015.
 */
public class ViewUtils {

    /**
     * @param view view to be adjusted
     * @param left left margin in DP
     * @param top top margin in DP
     * @param right right margin in DP
     * @param bottom bottom margin in DP
     * @return adjusted view
     */
    public static View addMarginsInDp( View view, int left, int top, int right, int bottom ) {
        DisplayMetrics dm = view.getResources().getDisplayMetrics();
        ViewGroup.LayoutParams viewLp = view.getLayoutParams();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( viewLp.width, viewLp.height );
        lp.setMargins( left, top, right, bottom );
        view.setLayoutParams( lp );
        return view;
    }

    public static View addMarginsInDp( View view, int vertical, int horizontal ) {
        return addMarginsInDp( view, horizontal, vertical, horizontal, vertical );
    }

    public static View addMarginsInDp( View view, int margin ) {
        return addMarginsInDp( view, margin, margin );
    }

    public static <T extends ViewGroup> View setSize(View view, int width, int height) {
        T.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
        return view;
    }
}
