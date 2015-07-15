package com.eclubprague.cardashboard.tablet.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.eclubprague.cardashboard.core.modules.IModule;
import com.eclubprague.cardashboard.tablet.R;
import com.eclubprague.cardashboard.tablet.fragments.SimplePageFragment;
import com.eclubprague.cardashboard.tablet.utils.CardSizeUtils;

import java.util.List;

abstract public class SimplePagerActivity extends FragmentActivity implements SimplePageFragment.OnPageFragmentInteractionListener {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private int rowCount;
    private int columnCount;
    private List<IModule> modules;

    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_pager);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = createPagerAdapter();
        // determine size
        final RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.simplepager_root_layout);
        ViewTreeObserver observer = rootLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int availableHeight = viewPager.getHeight();
                int availableWidth = viewPager.getWidth();
                rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                CardSizeUtils.Size tableSize = CardSizeUtils.getTableSize(getApplicationContext(), availableHeight, availableWidth);

                columnCount = tableSize.width;
                rowCount = tableSize.height;

                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(0);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_pager, menu);
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


    private PagerAdapter createPagerAdapter() {

        return new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return getPageFragment(i);
            }

            @Override
            public int getCount() {
                return getPageCount();
            }
        };
    }

    protected int getRowCount() {
        return rowCount;
    }

    protected int getColumnCount() {
        return columnCount;
    }

    protected int getModulesPerPageCount() {
        return getRowCount() * getColumnCount();
    }

    private int getPageCount() {
        if (modules == null) {
            return 0;
        } else {
            return (int) Math.round(Math.ceil((double) modules.size() / getModulesPerPageCount()));
        }
    }

    private Fragment getPageFragment(int page) {
        if (modules == null) {
            return null;
        }
        int start = page * getModulesPerPageCount();
        int end = (modules.size() < start + getModulesPerPageCount()) ? modules.size() : start + getModulesPerPageCount();
        return SimplePageFragment.newInstance(modules.subList(start, end), rowCount, columnCount);
    }

    public void setModules(List<IModule> modules) {
        this.modules = modules;
        if (pagerAdapter != null) {
            pagerAdapter.notifyDataSetChanged();
        }
        if (viewPager != null) {
            viewPager.setCurrentItem(0);
        }
    }
}
