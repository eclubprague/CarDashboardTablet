package com.eclubprague.cardashboard.tablet.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IModuleContext;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.predefined.EmptyModule;
import com.eclubprague.cardashboard.tablet.R;
import com.eclubprague.cardashboard.tablet.fragments.SimplePageFragment;
import com.eclubprague.cardashboard.tablet.utils.CardSizeUtils;

import java.util.ArrayList;
import java.util.List;

abstract public class SimplePagerActivity extends Activity implements IModuleContext {

    private static final String TAG = SimplePagerActivity.class.getSimpleName();

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private int rowCount = -1;
    private int columnCount = -1;
    private List<IModule> modules;
    private IParentModule parentModule;

    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_pager);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // determine size
    }

    protected void initPager() {
        pagerAdapter = createPagerAdapter();
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

                adjustModules(parentModule, modules);

                int maxModules = getMaxModules();
                while (modules.size() < maxModules) {
                    addEmptyModule();
                }
//                Log.d(TAG, "modules size = " + modules.size() + ", pageCount = " + getPageCount() + ", per page = " + getModulesPerPageCount());

//                Log.d(TAG, "modules new size = " + modules.size());

                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(0);
            }
        });
    }

    protected abstract void adjustModules(IParentModule parentModule, List<IModule> modules);

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

        return new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return getPageFragment(i);
            }

            @Override
            public int getCount() {
                return getPageCount(getModulesPerPageCount());
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

    public int getPageCount(int modulesPerPage) {
        if (modules == null) {
            return 0;
        } else {
            return (int) Math.round(Math.ceil((double) modules.size() / modulesPerPage));
        }
    }

    private Fragment getPageFragment(int page) {
        if (modules == null) {
            return null;
        }
        int start = page * getModulesPerPageCount();
        int end = (modules.size() < start + getModulesPerPageCount()) ? modules.size() : start + getModulesPerPageCount();
        List<IModule> submodules = new ArrayList<>(modules.subList(start, end));
        return SimplePageFragment.newInstance(submodules, rowCount, columnCount);
    }

    protected void setModule(IParentModule parentModule) {
        getActionBar().setTitle(parentModule.getTitle().getString(this));
        getActionBar().setIcon(parentModule.getIcon().getIcon(this));
        this.parentModule = parentModule;
        this.modules = this.parentModule.getSubmodules(this);
        addEmptyModule();
        initPager();
    }

    private void addEmptyModule() {
        modules.add(new EmptyModule(this, parentModule, null, null));
    }

    @Override
    public void goToSubmodules(IParentModule parentModule) {
        Intent intent = new Intent(this, ModuleActivity.class);
        intent.putExtra(ModuleActivity.KEY_PARENT_MODULE, parentModule.getId());
        // DOES NOT WORK (problem with homescreen parent without a view, doesnt work anyway)
//        String transitionName = getString(R.string.transition_card);
//        Log.d(TAG, parentModule.toString());
//        ActivityOptionsCompat options =
//                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
//                        parentModule.getView(),   // The view which starts the transition
//                        transitionName    // The transitionName of the view we’re transitioning to
//                );
//        ActivityCompat.startActivity(this, intent, options.toBundle());
        startActivity(intent);
    }

    @Override
    public void toggleQuickMenu(IModule module, boolean activate) {
        if (activate) {
            ViewSwitcher holder = (ViewSwitcher) module.loadHolder();
            holder.showNext();
            Log.d(TAG, "Toggling quick menu: activating, content: " + holder.getChildCount());
            for (int i = 0; i < holder.getChildCount(); i++) {
                Log.d(TAG, "child at " + i + ": " + holder.getChildAt(i));
            }
        } else {
            ViewSwitcher holder = (ViewSwitcher) module.loadHolder();
            holder.showPrevious();
            Log.d(TAG, "Toggling quick menu: deactivating, content: " + holder.getChildCount());
        }
    }

    @Override
    public void launchIntent(Intent intent) {
        startActivity(intent);
    }

    abstract public int getMaxModules();

    @Override
    public void swapModules(IModule oldModule, IModule newModule, boolean animate) {

    }

    @Override
    public void goBackFromSubmodules(IParentModule parentModule) {
        finish();
    }
}
