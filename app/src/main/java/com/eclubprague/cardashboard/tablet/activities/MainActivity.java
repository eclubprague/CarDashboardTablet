package com.eclubprague.cardashboard.tablet.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.eclubprague.cardashboard.core.modules.TestModule;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.models.resources.IconResource;
import com.eclubprague.cardashboard.core.modules.base.models.resources.StringResource;
import com.eclubprague.cardashboard.tablet.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SimplePagerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setModules(Arrays.<IModule>asList(
//            new SimpleAbstractModule(null, null, null, null),
//                new SimpleAbstractModule(null, null, null, null),
//                new SimpleAbstractModule(null, null, null, null),
//                new SimpleAbstractModule(null, null, null, null)
//        ));
        List<IModule> modules = new ArrayList<>();
        String gm = "Google maps";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 33; i++) {
            sb.append(gm.charAt(i % gm.length()));
            modules.add(new TestModule(StringResource.fromString(sb.toString()),
                    IconResource.fromResourceId(com.eclubprague.cardashboard.core.R.drawable.ic_language_black_24dp),
                    null, null));
        }
        setModules(modules);
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
