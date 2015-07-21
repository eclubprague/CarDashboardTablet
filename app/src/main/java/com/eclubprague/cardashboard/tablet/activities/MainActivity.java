package com.eclubprague.cardashboard.tablet.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.eclubprague.cardashboard.core.data.ModuleSupplier;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.ISubmenuModule;
import com.eclubprague.cardashboard.tablet.R;

import java.util.List;

public class MainActivity extends SimplePagerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setSubmenuModule(Arrays.<IModule>asList(
//            new AbstractSimpleModule(null, null, null, null),
//                new AbstractSimpleModule(null, null, null, null),
//                new AbstractSimpleModule(null, null, null, null),
//                new AbstractSimpleModule(null, null, null, null)
//        ));
        setModule(ModuleSupplier.getInstance().getHomeScreenModule(this));
    }

    @Override
    protected void adjustModules(ISubmenuModule parentModule, List<IModule> modules) {
        // no need to do anything
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        return getPageCount(getModulesPerPageCount()) * getModulesPerPageCount();
    }
}
