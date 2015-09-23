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
        int margin = context.getResources().getDimensionPixelSize(R.dimen.card_margin);
        int borderMargin = context.getResources().getDimensionPixelSize(R.dimen.card_margin_side);
        int height = context.getResources().getDimensionPixelSize(R.dimen.card_size);
        int width = context.getResources().getDimensionPixelSize(R.dimen.card_size);

        availableHeight -= margin + 2 * borderMargin; // top spacing + top and bottom outer margin
        availableWidth -= margin + 2 * borderMargin; // left spacing + left and right outer margin
        int minHeightRequiredPerCard = height + margin;
        int minWidthRequiredPerCard = width + margin;
        return new Size(availableHeight / minHeightRequiredPerCard,// rows
                availableWidth / minWidthRequiredPerCard); // columns
    }

    public static int getCardMargin(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.card_margin);
    }

    public static Size getCardSize(Context context) {
        int height = context.getResources().getDimensionPixelSize(R.dimen.card_size);
        int width = context.getResources().getDimensionPixelSize(R.dimen.card_size);
        return new Size(height, width);
    }

    /**
     * Returns Size containing maximum size of a card
     *
     * @param context         application context for resources access
     * @param availableHeight available vertical space
     * @param availableWidth  available horizontal space
     * @return Size(width, height)
     */
    public static Size getOptimalCardSize(Context context, int availableHeight, int availableWidth) {
        int margin = context.getResources().getDimensionPixelSize(R.dimen.card_margin);
        int height = context.getResources().getDimensionPixelSize(R.dimen.card_size);
        int width = context.getResources().getDimensionPixelSize(R.dimen.card_size);

        Size tableSize = getTableSize(context, availableHeight, availableWidth);
        availableHeight -= margin * (tableSize.height + 1);
        availableWidth -= margin * (tableSize.width + 1);
//        Log.d( getClass().getName(), "card height = " + availableHeight / tableSize.height + ", card width = " + availableWidth / tableSize.width );
        return new Size(availableHeight / tableSize.height, availableWidth / tableSize.width);
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
