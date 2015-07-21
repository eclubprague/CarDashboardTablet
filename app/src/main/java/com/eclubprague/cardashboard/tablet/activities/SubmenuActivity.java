package com.eclubprague.cardashboard.tablet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.eclubprague.cardashboard.core.data.ModuleSupplier;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.ISubmenuModule;
import com.eclubprague.cardashboard.core.modules.base.models.ModuleId;
import com.eclubprague.cardashboard.core.modules.predefined.BackModule;
import com.eclubprague.cardashboard.core.modules.predefined.HomeScreenModule;
import com.eclubprague.cardashboard.tablet.R;

import java.util.List;

public class SubmenuActivity extends SimplePagerActivity {
    public static final String KEY_PARENT_MODULE = SubmenuActivity.class.getName() + ".KEY_PARENT_MODULE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getIntent() != null;
        Intent intent = getIntent();
        ModuleId parentModuleId = (ModuleId) intent.getSerializableExtra(KEY_PARENT_MODULE);
        setModule(ModuleSupplier.getInstance().findSubmenuModule(this, parentModuleId));

    }

    @Override
    protected void adjustModules(ISubmenuModule parentModule, List<IModule> modules) {
        if (!parentModule.equals(HomeScreenModule.getInstance())) {
            if (modules.size() > 0) {
                if (!(modules.get(0) instanceof BackModule)) {
                    modules.add(0, new BackModule(this, parentModule));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submenu, menu);
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
    public int getMaxModules() {
        return getModulesPerPageCount() * getPageCount(getModulesPerPageCount() - 1);
    }
}
