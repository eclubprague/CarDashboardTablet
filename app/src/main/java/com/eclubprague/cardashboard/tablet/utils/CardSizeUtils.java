package com.eclubprague.cardashboard.tablet.utils;

import android.content.Context;

import com.eclubprague.cardashboard.tablet.R;

/**
 * Utility class for card size computing and stuff.
 *
 * Created by Michael on 14. 7. 2015.
 */
public class CardSizeUtils {

    /**
     * Returns Size containing number of columns (as width) and number of rows (as height)
     * @param context application context for resources access
     * @param availableHeight available vertical space
     * @param availableWidth available horizontal space
     * @return Size(columns, rows)
     */
    public static Size getTableSize(Context context, int availableHeight, int availableWidth){
        int margin = context.getResources().getDimensionPixelSize(R.dimen.module_card_margin);
        int height = context.getResources().getDimensionPixelSize(R.dimen.module_card_height);
        int width = context.getResources().getDimensionPixelSize(R.dimen.module_card_width);

        availableHeight -= margin; // top spacing
        availableWidth -= margin; // left spacing
        int minHeightRequiredPerCard = height + margin;
        int minWidthRequiredPerCard = width + margin;
        return new Size(availableHeight / minHeightRequiredPerCard,// rows
                availableWidth / minWidthRequiredPerCard); // columns
    }


    public static class Size {
        public final int height;
        public final int width;

        public Size(int height, int width) {
            this.height = height;
            this.width = width;
        }
    }
}
