package com.eclubprague.cardashboard.tablet.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ViewSwitcher;

import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IModuleContext;
import com.eclubprague.cardashboard.tablet.R;
import com.eclubprague.cardashboard.tablet.model.modules.IModuleContextTabletActivity;
import com.eclubprague.cardashboard.tablet.utils.CardSizeUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IModuleContext} interface
 * to handle interaction events.
 * Use the {@link SimplePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimplePageFragment extends Fragment {
    public static final String KEY_MODULE_ID = "moduleId";
    public static final String KEY_ROW_COUNT = "rowCount";
    public static final String KEY_COLUMN_COUNT = "columnCount";

    private static final String TAG = SimplePageFragment.class.getSimpleName();

    private List<IModule> modules;
    private int rowCount;
    private int columnCount;

    private int availableWidth;
    private int availableHeight;

    private CardSizeUtils.Size optimalCardSize;

    private IModuleContextTabletActivity moduleContext;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SimplePageFragment.
     */
    public static SimplePageFragment newInstance(List<IModule> modules, int rowCount, int columnCount) {
        Log.d(TAG, "recreating fragment: newInstance");
        SimplePageFragment fragment = new SimplePageFragment();
        fragment.setModules(modules);
        fragment.setRowCount(rowCount);
        fragment.setColumnCount(columnCount);
        return fragment;
    }

    public SimplePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "recreating fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.d(TAG, "onCreateView, modules: " + modules.size());
        View view = inflater.inflate(R.layout.fragment_simple_page, container, false);
//        TextView textView = (TextView) view.findViewById(R.id.textview);
//        StringBuilder sb = new StringBuilder();
//        for(IModule m : modules){
//            sb.append(m.getTitle().getString(getActivity()));
//        }
        GridView gridView = (GridView) view.findViewById(R.id.fragment_page_grid);

//        List<String> moduleNames = new ArrayList<>();
//        for (IModule m : modules) {
//            moduleNames.add(m.getTitle().getString(getActivity()));
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, moduleNames);
        gridView.setAdapter(new ModuleAdapter());

        availableWidth = container.getWidth();
        availableHeight = container.getHeight();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            moduleContext = (IModuleContextTabletActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPageFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        moduleContext = null;
    }

    public void setModules(List<IModule> modules) {
        this.modules = modules;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    private CardSizeUtils.Size getOptimalCardSize() {
        if (optimalCardSize == null) {
            optimalCardSize = CardSizeUtils.getOptimalCardSize(getActivity(), availableHeight, availableWidth);
        }
        return optimalCardSize;
    }

    private class ModuleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return rowCount * columnCount;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
//                Log.d(TAG, modules.size() + " <= " + position);
                IModule module = modules.get(position);
                ViewSwitcher viewHolder = (ViewSwitcher) module.createViewWithHolder(getActivity(), R.layout.module_holder, parent).holder;
                viewHolder.addView(module.createQuickMenuView(getActivity(), viewHolder));
                view = viewHolder;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moduleContext.turnQuickMenusOff();
                    }
                });
                //view = ViewUtils.setSize(view, getOptimalCardSize().width, getOptimalCardSize().height);
            } else {
                view = convertView;
            }
            return view;
        }
    }
}
