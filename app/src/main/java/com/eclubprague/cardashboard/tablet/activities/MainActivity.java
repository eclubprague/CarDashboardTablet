package com.eclubprague.cardashboard.tablet.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.eclubprague.cardashboard.core.modules.IModule;
import com.eclubprague.cardashboard.core.modules.SimpleAbstractModule;
import com.eclubprague.cardashboard.tablet.R;

import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends SimplePagerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setModules(Arrays.<IModule>asList(
            new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null),
                new SimpleAbstractModule(null, null, null, null)
        ));
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

}
