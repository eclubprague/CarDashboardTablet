package cz.blahami2.cardashboard.model.helper;

import android.content.Context;
import android.util.Log;

import cz.blahami2.cardashboard.R;

/**
 * Created by Michael on 15. 3. 2015.
 */
public class CardSizeCalculator {
    private Context mContext;
    private int mMinSize;
    private int mSpace;

    public CardSizeCalculator(Context context) {
        this.mContext = context;
        this.mMinSize = context.getResources().getDimensionPixelSize(R.dimen.app_list_card_width_and_height);
        this.mSpace = context.getResources().getDimensionPixelSize(R.dimen.app_list_card_spacing);
    }

    /**
     * Returns Size containing number of columns (as width) and number of rows (as height)
     * @param availableHeight available vertical space
     * @param availableWidth available horizontal space
     * @return Size(columns, rows)
     */
    public Size getTableSize(int availableHeight, int availableWidth){
        availableHeight -= mSpace; // top spacing
        availableWidth -= mSpace; // left spacing
        int minSpaceRequiredPerCard = mMinSize + mSpace;
        return new Size(availableHeight / minSpaceRequiredPerCard,// rows
                availableWidth / minSpaceRequiredPerCard); // columns
    }

    /**
     * Returns Size containing maximum size of a card
     * @param availableHeight available vertical space
     * @param availableWidth available horizontal space
     * @return Size(width, height)
     */
    public Size getOptimalCardSize(int availableHeight, int availableWidth){
        Size tableSize = getTableSize(availableHeight, availableWidth);
        availableHeight -= mSpace * (tableSize.height + 1);
        availableWidth -= mSpace * (tableSize.width + 1);
        Log.d( getClass().getName(), "card height = " + availableHeight / tableSize.height + ", card width = " + availableWidth / tableSize.width );
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
