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
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.eclubprague.cardashboard.core.application.GlobalDataProvider;
import com.eclubprague.cardashboard.core.data.ModuleSupplier;
import com.eclubprague.cardashboard.core.data.database.ModuleDAO;
import com.eclubprague.cardashboard.core.data.modules.ModuleEnum;
import com.eclubprague.cardashboard.core.data.modules.ModuleType;
import com.eclubprague.cardashboard.core.fragments.ModuleListDialogFragment;
import com.eclubprague.cardashboard.core.model.resources.StringResource;
import com.eclubprague.cardashboard.core.modules.base.IActivityStateChangeListener;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IModuleContext;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.base.ModuleEvent;
import com.eclubprague.cardashboard.core.modules.base.models.ModuleId;
import com.eclubprague.cardashboard.core.modules.predefined.BackModule;
import com.eclubprague.cardashboard.core.preferences.SettingsActivity;
import com.eclubprague.cardashboard.core.utils.ErrorReporter;
import com.eclubprague.cardashboard.core.utils.ModuleUtils;
import com.eclubprague.cardashboard.core.utils.TextToSpeech;
import com.eclubprague.cardashboard.core.views.ModuleView;
import com.eclubprague.cardashboard.tablet.R;
import com.eclubprague.cardashboard.tablet.adapters.ModuleFragmentAdapter;
import com.eclubprague.cardashboard.tablet.model.modules.IModuleContextTabletActivity;
import com.eclubprague.cardashboard.tablet.utils.CardSizeUtils;

import java.io.IOException;
import java.util.List;

public class SimplePagerActivity extends Activity implements IModuleContextTabletActivity {

    private static final String TAG = SimplePagerActivity.class.getSimpleName();
    private static final String KEY_PAGE = SimplePagerActivity.class.getName() + ".KEY_PAGE";
    public static final String KEY_PARENT_MODULE = SimplePagerActivity.class.getName() + ".KEY_PARENT_MODULE";
    public static final String KEY_PREVIOUS_PARENT_MODULE = SimplePagerActivity.class.getName() + ".KEY_PREVIOUS_PARENT_MODULE";
    private static final int DEFAULT_PAGE = 0;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private List<IModule> modules;
    private IParentModule parentModule;
    private IParentModule topParent;
    private ModuleView toggledView;

    private int page = DEFAULT_PAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_pager);
        GlobalDataProvider.getInstance().setModuleContext(this);
        TextToSpeech.speak(null);
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


//        try {
//            ModuleDAO moduleDAO = new ModuleDAO(this);
//            String data = moduleDAO.writeParentModule(ModuleSupplier.getBaseInstance().getHomeScreenModule(this));
//            IParentModule parentModule = moduleDAO.readParentModule(data);
//            goToSubmodules(parentModule);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // determine size
        IParentModule module = getParentModule(savedInstanceState, KEY_PARENT_MODULE);
        topParent = getParentModule(savedInstanceState, KEY_PREVIOUS_PARENT_MODULE);
//        GlobalDataProvider.getInstance().setModuleContext(this);
//        for (IActivityStateChangeListener listener : module.getSubmodules()) {
//            listener.onStart(this);
//        }
        setModule(module);
    }

    private IParentModule getParentModule(Bundle savedInstanceState, String key) {
        IParentModule module;
        if (savedInstanceState != null && savedInstanceState.getSerializable(key) != null) {
            ModuleId parentModuleId = (ModuleId) savedInstanceState.getSerializable(key);
            module = ModuleSupplier.getPersonalInstance().findSubmenuModule(this, parentModuleId);
        } else if (getIntent() != null && getIntent().getSerializableExtra(key) != null) {
            Intent intent = getIntent();
            ModuleId parentModuleId = (ModuleId) intent.getSerializableExtra(key);
            module = ModuleSupplier.getPersonalInstance().findSubmenuModule(this, parentModuleId);
        } else {
            module = ModuleSupplier.getPersonalInstance().getHomeScreenModule(this);
        }
        return module;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) { // restart when changed orientation and stuff
        super.onConfigurationChanged(newConfig);
        restart();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_PAGE, viewPager.getCurrentItem());
        outState.putSerializable(KEY_PARENT_MODULE, getParentModule().getId());
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
        GlobalDataProvider.getInstance().setModuleContext(this);
        for (IActivityStateChangeListener listener : modules) {
            listener.onStart(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalDataProvider.getInstance().setModuleContext(this);
        for (IActivityStateChangeListener listener : modules) {
            listener.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


        for (IActivityStateChangeListener listener : modules) {
            listener.onPause(this);
        }
        try {
            IParentModule parentModule = ModuleSupplier.getPersonalInstance().getHomeScreenModule(this);
            // copy without empty modules
            parentModule = ModuleUtils.forEachDeepCopy(parentModule, new ModuleUtils.Action() {
                @Override
                public IModule performAction(IModule module) {
                    IParentModule p = (IParentModule) module;
                    p.removeTailEmptyModules();
                    return p;
                }
            }, ModuleType.PARENT);
            ModuleDAO.saveParentModuleAsync(this, parentModule);
        } catch (IOException e) {
            ErrorReporter.reportApplicationError(null, null, null, null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (IActivityStateChangeListener listener : modules) {
            listener.onStop(this);
        }
    }

    @Override
    protected void onDestroy() {
        for (IActivityStateChangeListener listener : modules) {
            listener.onDestroy(this);
        }
        super.onDestroy();
    }

    protected IParentModule getParentModule() {
        return parentModule;
    }

    protected void setModule(IParentModule parentModule) {
        getActionBar().setTitle(parentModule.getTitle().getString(this));
        getActionBar().setIcon(parentModule.getIcon().getIcon(this));
        this.parentModule = parentModule;
        this.parentModule.removeTailEmptyModules();
        this.modules = this.parentModule.getSubmodules();
//        parentModule.removeTailEmptyModules();
        initPager();
    }

    @Override
    public void goToSubmodules(IParentModule parentModule) {
        Intent intent = new Intent(this, SimplePagerActivity.class);
        intent.putExtra(SimplePagerActivity.KEY_PARENT_MODULE, parentModule.getId());
        // DOES NOT WORK (problem with homescreen parent without a view, doesnt work anyway)
//        String transitionName = getString(R.string.transition_card);
//        Log.d(TAG, parentModule.toString());
//        ActivityOptionsCompat options =
//                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
//                        parentModule.getViews(),   // The view which starts the transition
//                        transitionName    // The transitionName of the view we’re transitioning to
//                );
//        ActivityCompat.startActivity(this, intent, options.toBundle());
        startActivity(intent);
    }

    @Override
    public void toggleQuickMenu(IModule module, boolean activate) {
        ViewSwitcher holder = (ViewSwitcher) module.getHolder();
        if (activate) {
//            holder.showNext();
            if (holder.getDisplayedChild() != 1) {
                holder.setDisplayedChild(1);
            }
            toggledView = module.getView();
//            Log.d(TAG, "Toggling quick menu: activating, content: " + holder.getChildCount());
//            for (int i = 0; i < holder.getChildCount(); i++) {
//                Log.d(TAG, "child at " + i + ": " + holder.getChildAt(i));
//            }
        } else {
//            holder.showPrevious();
            if (holder.getDisplayedChild() != 0) {
                holder.setDisplayedChild(0);
            }
//            Log.d(TAG, "Toggling quick menu: deactivating, content: " + holder.getChildCount());
        }
    }

    @Override
    public void turnQuickMenusOff() {
        if (toggledView != null) {
            toggledView.getModule().onEvent(ModuleEvent.CANCEL, this);
        }
//        for (IModule module : modules) {
//            for (ModuleView moduleView : module.getViews(this)) {
//                module.onEvent(ModuleEvent.CANCEL, moduleView, this);
//            }
//        }
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
    public void goBackFromSubmodules(IParentModule previousParentModule) {
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onModuleEvent(final IModule module, ModuleEvent event) {
//        Log.d(TAG, "=====================================================================");
//        Log.d(TAG, "=====================================================================");
//        Log.d(TAG, "EVENT = " + event.name());
//        Log.d(TAG, "=====================================================================");
//        Log.d(TAG, "=====================================================================");

        switch (event) {
            case CANCEL:
                toggleQuickMenu(module, false);
                break;
            case DELETE:
//                Log.d(TAG, "looking for module: " + module);
//                Log.d(TAG, "index of view inside module: " + module.getViews(this).indexOf(moduleView));
//                Log.d(TAG, "ModuleViews: ");
//                List<ModuleView> moduleViews = module.getViews(this);
//                for (int i = 0; i < moduleViews.size(); i++) {
//                    Log.d(TAG, i + ". moduleView = " + moduleViews.get(i));
//                }
//                Log.d(TAG, "list:");
//                for (int i = 0; i < modules.size(); i++) {
//                    Log.d(TAG, i + ". module = " + modules.get(i));
//                }
//                for(IModule m : modules){
//                    Log.d(TAG, "module: " + m.getId() + ", title = " + m.getTitle().getString(this));
//                }
//                Log.d(TAG, "removing module {" + module.getTitle().getString(this) + "}: " + module.toString());
//                modules.set(ListUtils.getNthIndexOf(modules, module, module.getViews(this).indexOf(moduleView)), new EmptyModule());
                modules.set(parentModule.getSubmodules().indexOf(module), ModuleEnum.EMPTY.newInstance());
                restart();
                break;
            case MOVE:
//                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(module.getViews());
//                module.getHolder().startDrag(null, myShadow, null, 0);
                toggleQuickMenu(module, false);
                break;
            case ADD:
//                final int indexOf = ListUtils.getNthIndexOf(modules, module, module.getViews(this).indexOf(moduleView));
                final int indexOf = parentModule.getSubmodules().indexOf(module);
                ModuleListDialogFragment dialog = ModuleListDialogFragment.newInstance(this, new ModuleListDialogFragment.OnAddModuleListener() {
                    @Override
                    public void addModule(IModule newModule) {
                        modules.set(indexOf, newModule);
                        restart();
                    }
                });
                dialog.show(getFragmentManager(), "Applist");
//                restart();
                break;

        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public ViewGroup getSnackbarHolder() {
        return (ViewGroup) findViewById(R.id.snackbar_container);
    }

    protected void adjustModules(IParentModule parentModule, List<IModule> modules) {
        if (!parentModule.equals(ModuleSupplier.getPersonalInstance().getHomeScreenModule(this))) {
            if (modules.size() > 0) {
                if (!(modules.get(0) instanceof BackModule)) {
                    modules.add(0, new BackModule(parentModule));
                }
            } else {
                modules.add(new BackModule(parentModule));
            }
        }
    }

    private void restart() {
        Intent intent = new Intent(this, SimplePagerActivity.class);
        intent.putExtra(SimplePagerActivity.KEY_PARENT_MODULE, parentModule.getId());
        intent.putExtra(KEY_PAGE, viewPager.getCurrentItem());
        for (IActivityStateChangeListener listener : modules) {
            listener.onDestroy(this);
        }
        startActivity(intent);
        finish();
    }
}
