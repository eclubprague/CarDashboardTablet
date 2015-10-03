package com.eclubprague.cardashboard.tablet.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ViewSwitcher;

import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IModuleContext;
import com.eclubprague.cardashboard.core.modules.base.models.ViewWithHolder;
import com.eclubprague.cardashboard.core.modules.predefined.BackModule;
import com.eclubprague.cardashboard.core.modules.predefined.EmptyModule;
import com.eclubprague.cardashboard.core.views.ModuleView;
import com.eclubprague.cardashboard.tablet.R;
import com.eclubprague.cardashboard.tablet.model.modules.IModuleContextTabletActivity;
import com.eclubprague.cardashboard.tablet.settings.ShowcaseManager;
import com.eclubprague.cardashboard.tablet.utils.CardSizeUtils;
import com.eclubprague.cardashboard.tablet.view.GridLayout;

import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

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
    private static final String SHOWCASE_ID = SimplePageFragment.class.getName();

    private static final String SHOWCASE_ID_MODULE = SHOWCASE_ID;
    private static final String SHOWCASE_ID_EMPTY = SHOWCASE_ID + EmptyModule.class.getSimpleName();
    private static final String SHOWCASE_ID_BACK = SHOWCASE_ID + BackModule.class.getSimpleName();


    private List<IModule> modules;

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
    public static SimplePageFragment newInstance(List<IModule> modules) {
//        Log.d(TAG, "recreating fragment: newInstance");
        SimplePageFragment fragment = new SimplePageFragment();
        fragment.modules = modules;
        return fragment;
    }

    public SimplePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "recreating fragment");
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
        availableWidth = container.getWidth();
        availableHeight = container.getHeight();
        GridLayout gridLayout = (GridLayout) view;
        CardSizeUtils.Size cardSize = CardSizeUtils.getCardSize(getActivity());
        gridLayout.init(availableHeight, availableWidth, cardSize.height, cardSize.width, CardSizeUtils.getCardMargin(getActivity()));

//        ModuleAdapter adapter = new ModuleAdapter();
//        for(int i = 0; i < adapter.getCount(); i++){
//            gridLayout.setChildAt(i, adapter.getView(i, null, gridLayout));
//        }

//        ModuleAdapter adapter = new ModuleAdapter();
//        for (int i = 0; i < adapter.getCount(); i++) {
//            TextView tv = new TextView(getActivity());
//            tv.setText("Test string");
//            gridLayout.setChildAt(i, tv);
//        }

        ModuleAdapter adapter = new ModuleAdapter();
        for (int i = 0; i < modules.size(); i++) {
            gridLayout.setChildAt(i, adapter.getView(i, null, gridLayout));
        }

//        ShowcaseConfig config = new ShowcaseConfig();
//        config.setDelay(500); // half second between each showcase view
//        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);
//        sequence.setConfig(config);
//
//        sequence.start();
//
//        long delay = 500;
//        boolean moduleSet = ShowcaseManager.getBoolean(SHOWCASE_ID_MODULE);
//        boolean emptySet = ShowcaseManager.getBoolean(SHOWCASE_ID_EMPTY);
//        boolean backSet = ShowcaseManager.getBoolean(SHOWCASE_ID_BACK);
//        for(int i = 0; i < modules.size(); i++){
//            if(moduleSet && backSet && emptySet){
//                break;
//            }
//            IModule module = modules.get(i);
//            if(module instanceof EmptyModule){
//                sequence.addSequenceItem(module.getView(),
//                        "This is button one", "GOT IT");
//                emptySet = true;
//                ShowcaseManager.putBoolean(SHOWCASE_ID_EMPTY, true);
//            } else if(module instanceof BackModule) {
//                sequence.addSequenceItem(module.getView(),
//                        "This is button one", "GOT IT");
//                backSet = true;
//                ShowcaseManager.putBoolean(SHOWCASE_ID_BACK, true);
//            }else {
//                sequence.addSequenceItem(module.getView(),
//                        "This is button one", "GOT IT");
//                moduleSet = true;
//                ShowcaseManager.putBoolean(SHOWCASE_ID_MODULE, true);
//            }
//        }

//        List<String> moduleNames = new ArrayList<>();
//        for (IModule m : modules) {
//            moduleNames.add(m.getTitle().getString(getActivity()));
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, moduleNames);
//        gridView.setAdapter(new ModuleAdapter());

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

    private CardSizeUtils.Size getOptimalCardSize() {
        if (optimalCardSize == null) {
            optimalCardSize = CardSizeUtils.getOptimalCardSize(getActivity(), availableHeight, availableWidth);
        }
        return optimalCardSize;
    }

    private class ModuleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return modules.size();
        }

        @Override
        public Object getItem(int position) {
            return modules.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
//                Log.d(TAG, modules.size() + " <= " + position);
                IModule module = (IModule) getItem(position);
                ViewWithHolder<ModuleView> viewWithHolder = module.createViewWithHolder(moduleContext, R.layout.module_holder, parent);
                ViewSwitcher viewHolder = (ViewSwitcher) viewWithHolder.holder;
                ModuleView v = viewWithHolder.view;
//                Log.d(TAG, "Got view of module: " + module);
//                Log.d(TAG, "which is: " + v);
                viewHolder.addView(module.createQuickMenuView(moduleContext, viewHolder));
                view = viewHolder;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moduleContext.turnQuickMenusOff();
                    }
                });

//                view.setOnDragListener(new View.OnDragListener() {
//                    @Override
//                    public boolean onDrag(View v, DragEvent event) {
//                        switch (event.getAction()) {
//                            case DragEvent.ACTION_DRAG_STARTED:
//                                Log.d(TAG, "drag started");
//                                break;
//                            case DragEvent.ACTION_DRAG_ENDED:
//                                Log.d(TAG, "drag ended");
//                                break;
//                            case DragEvent.ACTION_DRAG_ENTERED:
//                                Log.d(TAG, "drag entered");
//                                break;
//                            case DragEvent.ACTION_DRAG_EXITED:
//                                Log.d(TAG, "drag exited");
//                                break;
//                            case DragEvent.ACTION_DRAG_LOCATION:
//                                Log.d(TAG, "drag location");
//                                break;
//                            case DragEvent.ACTION_DROP:
//                                Log.d(TAG, "drag drop");
//                                break;
//                        }
//                        Log.d(TAG, "location = " + event.getX() + ":" + event.getY());
//                        return false;
//                    }
//                });
                //view = ViewUtils.setSize(view, getOptimalCardSize().width, getOptimalCardSize().height);
            } else {
                view = convertView;
            }
            return view;
        }
    }
}
