package com.eclubprague.cardashboard.tablet.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.eclubprague.cardashboard.core.modules.base.IActivityStateChangeListener;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IModuleContext;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.base.ModuleEvent;
import com.eclubprague.cardashboard.core.modules.base.models.resources.StringResource;
import com.eclubprague.cardashboard.core.modules.predefined.EmptyModule;
import com.eclubprague.cardashboard.core.preferences.SettingsActivity;
import com.eclubprague.cardashboard.core.views.ModuleListDialogFragment;
import com.eclubprague.cardashboard.tablet.R;
import com.eclubprague.cardashboard.tablet.adapters.ModuleFragmentAdapter;
import com.eclubprague.cardashboard.tablet.model.modules.IModuleContextTabletActivity;
import com.eclubprague.cardashboard.tablet.utils.CardSizeUtils;

import java.util.ArrayList;
import java.util.List;

abstract public class SimplePagerActivity extends Activity implements IModuleContextTabletActivity {

    private static final String TAG = SimplePagerActivity.class.getSimpleName();
    private static final String KEY_PAGE = SimplePagerActivity.class.getName() + ".keyPage";
    private static final int DEFAULT_PAGE = 0;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private List<IActivityStateChangeListener> stateChangeListeners = new ArrayList<>();
    private List<IModule> modules;
    private IParentModule parentModule;

    private int page = DEFAULT_PAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_pager);
//        viewPager = new ViewPager(this);
//        ViewGroup root = (ViewGroup) findViewById(R.id.simplepager_root_layout);
//        viewPager.setId(R.id.viewpager);
//        root.addView(viewPager);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        Log.d(TAG, "recreating activity");
        if (savedInstanceState != null) {
            page = savedInstanceState.getInt(KEY_PAGE, DEFAULT_PAGE);
        } else if (getIntent() != null) {
            Intent intent = getIntent();
            page = intent.getIntExtra(KEY_PAGE, DEFAULT_PAGE);
        } else {
            page = DEFAULT_PAGE;
        }
        // determine size
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        restart();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_PAGE, viewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    protected void initPager() {
        final IModuleContext thisModuleContext = this;
        viewPager.setAdapter(null);
//        Log.d(TAG, "recreating pager adapter");
        final RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.simplepager_root_layout);
        ViewTreeObserver observer = rootLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int availableHeight = viewPager.getHeight();
                int availableWidth = viewPager.getWidth();
                rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                CardSizeUtils.Size tableSize = CardSizeUtils.getTableSize(getApplicationContext(), availableHeight, availableWidth);

                int columnCount = tableSize.width;
                int rowCount = tableSize.height;

                adjustModules(parentModule, modules);
//                Log.d(TAG, "modules size = " + modules.size() + ", pageCount = " + getPageCount() + ", per page = " + getModulesPerPageCount());

//                Log.d(TAG, "modules new size = " + modules.size());

                pagerAdapter = new ModuleFragmentAdapter(getFragmentManager(), rowCount, columnCount, modules, thisModuleContext, parentModule);
                Log.d(TAG, "recreating activity layout");
                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(page);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        turnQuickMenusOff();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "starting  - listeners");
        for(IActivityStateChangeListener listener : stateChangeListeners){
            listener.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "resuming  - listeners");
        for(IActivityStateChangeListener listener : stateChangeListeners){
            listener.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "pausing  - listeners");
        for(IActivityStateChangeListener listener : stateChangeListeners){
            listener.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stopping  - listeners");
        for(IActivityStateChangeListener listener : stateChangeListeners){
            listener.onStop();
            Log.d(TAG, "stopping  - " + listener.getClass().getSimpleName());
        }
    }

    protected IParentModule getParentModule() {
        return parentModule;
    }

    protected void setModule(IParentModule parentModule) {
        getActionBar().setTitle(parentModule.getTitle().getString(this));
        getActionBar().setIcon(parentModule.getIcon().getIcon(this));
        this.parentModule = parentModule;
        this.parentModule.removeTailEmptyModules();
        this.modules = this.parentModule.getSubmodules(this);
//        parentModule.removeTailEmptyModules();
        initPager();
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
            ViewSwitcher holder = (ViewSwitcher) module.getHolder();
//            holder.showNext();
            if (holder.getDisplayedChild() != 1) {
                holder.setDisplayedChild(1);
            }
//            Log.d(TAG, "Toggling quick menu: activating, content: " + holder.getChildCount());
//            for (int i = 0; i < holder.getChildCount(); i++) {
//                Log.d(TAG, "child at " + i + ": " + holder.getChildAt(i));
//            }
        } else {
            ViewSwitcher holder = (ViewSwitcher) module.getHolder();
//            holder.showPrevious();
            if (holder.getDisplayedChild() != 0) {
                holder.setDisplayedChild(0);
            }
//            Log.d(TAG, "Toggling quick menu: deactivating, content: " + holder.getChildCount());
        }
    }

    @Override
    public void turnQuickMenusOff() {
        for (IModule module : modules) {
            module.onCancel(this);
        }
    }

    @Override
    public void launchIntent(Intent intent, StringResource errorMessage) {
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(errorMessage.getString(this)).create().show();
        }
    }

    @Override
    public void swapModules(IModule oldModule, IModule newModule, boolean animate) {

    }

    @Override
    public void goBackFromSubmodules(IParentModule previousParentModule) {
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void addListener(IActivityStateChangeListener listener) {
        stateChangeListeners.add(listener);
        Log.d(TAG, "adding listener: " + listener.getClass().getSimpleName());
    }

    @Override
    public void removeListener(IActivityStateChangeListener listener) {
        stateChangeListeners.remove(listener);
    }

    @Override
    public void onModuleEvent(final IModule module, ModuleEvent event) {
        switch (event) {
            case CANCEL:
                toggleQuickMenu(module, false);
                break;
            case DELETE:
//                for(IModule m : modules){
//                    Log.d(TAG, "module: " + m.getId() + ", title = " + m.getTitle().getString(this));
//                }
//                Log.d(TAG, "removing module {" + module.getTitle().getString(this) + "}: " + module.toString());
                modules.set(modules.indexOf(module), new EmptyModule(this));
                restart();
                break;
            case MOVE:
//                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(module.getView());
//                module.getHolder().startDrag(null, myShadow, null, 0);
                toggleQuickMenu(module, false);
                break;
            case ADD:
                ModuleListDialogFragment dialog = ModuleListDialogFragment.newInstance(this, new ModuleListDialogFragment.OnAddModuleListener() {
                    @Override
                    public void addModule(IModule newModule) {
                        modules.set(modules.indexOf(module), newModule);
                        restart();
                    }
                });
                dialog.show(getFragmentManager(), "Applist");
//                restart();
                break;

        }
    }

    private void restart() {
        Intent intent = new Intent(this, ModuleActivity.class);
        intent.putExtra(ModuleActivity.KEY_PARENT_MODULE, parentModule.getId());
        intent.putExtra(KEY_PAGE, viewPager.getCurrentItem());
        startActivity(intent);
        finish();
    }
}
