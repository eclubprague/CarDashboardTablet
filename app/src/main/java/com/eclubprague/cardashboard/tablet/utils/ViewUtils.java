package cz.blahami2.cardashboard.utils;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Michael on 14. 4. 2015.
 */
public class ViewUtils {

    /**
     * @param view
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
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
}
