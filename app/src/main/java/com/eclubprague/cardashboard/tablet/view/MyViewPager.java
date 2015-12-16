package com.eclubprague.cardashboard.tablet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by michael on 12/16/15.
 */
public class MyViewPager extends android.support.v4.view.ViewPager {
    private static final String TAG = MyViewPager.class.getName();

    private float mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    private boolean mIsScrolling = false;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onTouchEvent will be called and we do the actual
         * scrolling there.
         */


        //final int action = MotionEventCompat.getActionMasked(ev);
        final int action = ev.getAction();

        // Always handle the case of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the scroll.
            mIsScrolling = false;
            return false; // Do not intercept touch event, let the child handle it
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                if (mIsScrolling) {
                    return true;
                }

                // If the user has dragged her finger horizontally more than
                // the touch slop, start the scroll
                float xDiff = 0;
                if (ev.getHistorySize() > 0) {
                    xDiff = Math.abs(ev.getHistoricalX(ev.getHistorySize() - 1) - ev.getX());
                }

                // Touch slop should be calculated using ViewConfiguration
                // constants.
                if (xDiff > mTouchSlop) {
                    // Start scrolling!
                    mIsScrolling = true;
                    return true;
                }
                return false;
            }
            default:
                return super.onInterceptTouchEvent(ev);
        }
    }

}
