package com.eclubprague.cardashboard.tablet.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eclubprague.cardashboard.core.modules.IModule;
import com.eclubprague.cardashboard.tablet.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SimplePageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SimplePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimplePageFragment extends Fragment {

    private List<IModule> modules;
    private int rowCount;
    private int columnCount;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SimplePageFragment.
     */
    public static SimplePageFragment newInstance(List<IModule> modules, int rowCount, int columnCount) {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_page, container, false);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText("modules count: " + modules.size());
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setModules(List<IModule> modules) {
        this.modules = modules;
    }

    private void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    private void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
