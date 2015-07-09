package com.eclubprague.cardashboard.tablet.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import cz.blahami2.cardashboard.R;
import cz.blahami2.cardashboard.fragments.AppListFragment;
import cz.blahami2.cardashboard.model.helper.CardSizeCalculator;


public class AppListActivity extends ActionBarActivity implements AppListFragment.OnFragmentInteractionListener {

    private ViewPager mViewPager;
    private AppsPagerAdapter mPagerAdapter;

    private int mRows = 0;
    private int mColumns = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);


        Log.d(getClass().getName(), "creating activity");
        mPagerAdapter = new AppsPagerAdapter(getFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.app_list_pager);
        Log.d(getClass().getName(), "pager size = " + mViewPager.getHeight() + ", " + mViewPager.getWidth());


        final RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.app_list_activity_root);
        ViewTreeObserver observer = rootLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int height= mViewPager.getHeight();
                int width = mViewPager.getWidth();
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                else {
                    rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                CardSizeCalculator cardSizeCalculator = new CardSizeCalculator(getApplicationContext());
                CardSizeCalculator.Size tableSize = cardSizeCalculator.getTableSize(height, width);
                Log.d(getClass().getName(), "pager size = " + mViewPager.getHeight() + ", " + mViewPager.getWidth());

                mColumns = tableSize.width;
                mRows = tableSize.height;

                mViewPager.setAdapter(mPagerAdapter);
                mViewPager.setCurrentItem(0);
            }
        });

        Log.d(getClass().getName(), "pager size = " + mViewPager.getHeight() + ", " + mViewPager.getWidth());

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d(getClass().getName(), "getWindow.x = " + getWindow().getAttributes().x);
//        Log.d(getClass().getName(), "getWindow.y = " + getWindow().getAttributes().y);
//
////        Log.d(getClass().getName(), "sceneWidth = " + getCurrentFocus().getWidth());
////        Log.d(getClass().getName(), "sceneHeight = " + getCurrentFocus().getHeight());
//
//        int height = mViewPager.getHeight();
//        int width = mViewPager.getWidth();
//
//        CardSizeCalculator cardSizeCalculator = new CardSizeCalculator(getApplicationContext());
//        CardSizeCalculator.Size tableSize = cardSizeCalculator.getTableSize(height, width);
//
//        mColumns = tableSize.width;
//        mRows = tableSize.height;
//
//        mViewPager.setAdapter(mPagerAdapter);
//        mViewPager.setCurrentItem(0);
//
//        Log.d(getClass().getName(), "onStart: pager size = " + mViewPager.getHeight() + ", " + mViewPager.getWidth());
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class AppsPagerAdapter extends FragmentPagerAdapter {


        public AppsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return AppListFragment.newInstance(i, mRows, mColumns);
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}
