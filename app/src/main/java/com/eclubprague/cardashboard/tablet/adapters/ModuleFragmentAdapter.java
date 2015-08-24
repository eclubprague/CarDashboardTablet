package com.eclubprague.cardashboard.tablet.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IModuleContext;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.predefined.EmptyModule;
import com.eclubprague.cardashboard.tablet.fragments.SimplePageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 30. 7. 2015.
 * <p/>
 * Adapter for module fragments..
 */
public class ModuleFragmentAdapter extends FragmentStatePagerAdapter {
    public static final String TAG = ModuleFragmentAdapter.class.getSimpleName();
//    private static final List<Fragment> fragments = new ArrayList<>();

    private int count = -1;
    private final int rowCount;
    private final int columnCount;
    private final List<IModule> modules;
    private final IModuleContext moduleContext;
    private final IParentModule parentModule;

    public ModuleFragmentAdapter(FragmentManager fm, int rowCount, int columnCount, List<IModule> modules, IModuleContext moduleContext, IParentModule parentModule) {
        super(fm);
//        Log.d(TAG,  "fragmentcount = " + fm.getBackStackEntryCount());
//        FragmentTransaction ft = fm.beginTransaction();
//        for(int i = 0; i < fm.getBackStackEntryCount(); i++){
//            ft.remove(fm.findFragmentById(fm.getBackStackEntryAt(i).getId()));
//        }
//        for(Fragment f : fragments){
//            ft.remove(f);
//        }
//        ft.commitAllowingStateLoss();
//        fm.executePendingTransactions();
//        fragments.clear();
//        Log.d(TAG, "creating new fragment manager: row*column = " + rowCount * columnCount + ", fragments = " + fragments.size() + ", modules = " + modules );
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.modules = modules;
        this.moduleContext = moduleContext;
        this.parentModule = parentModule;
    }

    @Override
    public Fragment getItem(int i) {
//        Log.d(TAG, "obtaining item from adapter: page = " + i + ", modules = " + modules);
        if (modules == null) {
            throw new IllegalStateException("Module list is null.");
        }
        return SimplePageFragment.newInstance(getSubModules(i), rowCount, columnCount);
    }

    @Override
    public int getCount() {
        if (modules == null) {
            throw new IllegalStateException("Module list is null.");
        }
        if (count == -1) {
            count = ((int) Math.round(Math.ceil((double) modules.size() / (rowCount * columnCount)))) + 1;
        }
        return count;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private List<IModule> getSubModules(int page) {
//        Log.d(TAG, "requesting submodules for page = " + page + ", modules size = " + modules.size());
        if (modules == null) {
            throw new IllegalStateException("Module list is null.");
        }
        int modulesPerPage = rowCount * columnCount;
        int start = page * modulesPerPage;
        while (modules.size() < start + modulesPerPage) {
            modules.add(new EmptyModule());
        }
        int end = (modules.size() < start + modulesPerPage) ? modules.size() : start + modulesPerPage;
        return new ArrayList<>(modules.subList(start, end));
    }
}
