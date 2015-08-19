package com.eclubprague.cardashboard.tablet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;

import com.eclubprague.cardashboard.core.data.ModuleSupplier;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.base.models.ModuleId;
import com.eclubprague.cardashboard.core.modules.predefined.BackModule;
import com.eclubprague.cardashboard.tablet.R;

import java.util.List;

public class ModuleActivity extends SimplePagerActivity {
    public static final String KEY_PARENT_MODULE = ModuleActivity.class.getName() + ".KEY_PARENT_MODULE";
    public static final String KEY_PREVIOUS_PARENT_MODULE = ModuleActivity.class.getName() + ".KEY_PREVIOUS_PARENT_MODULE";

    private IParentModule topParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IParentModule module = getParentModule(savedInstanceState, KEY_PARENT_MODULE);
        topParent = getParentModule(savedInstanceState, KEY_PREVIOUS_PARENT_MODULE);
        setModule(module);
    }

    private IParentModule getParentModule(Bundle savedInstanceState, String key){
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(KEY_PARENT_MODULE, getParentModule().getId());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void adjustModules(IParentModule parentModule, List<IModule> modules) {
        if (!parentModule.equals(ModuleSupplier.getPersonalInstance().getHomeScreenModule(this))) {
            if (modules.size() > 0) {
                if (!(modules.get(0) instanceof BackModule)) {
                    modules.add(0, new BackModule(this, parentModule));
                }
            } else {
                modules.add(new BackModule(this, parentModule));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_module, menu);
        return true;
    }
}
