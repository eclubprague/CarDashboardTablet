package com.eclubprague.cardashboard.tablet.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.blahami2.cardashboard.R;
import cz.blahami2.cardashboard.model.applications.AbstractDashboardApplication;
import cz.blahami2.cardashboard.model.applications.ApplicationsAdapter;
import cz.blahami2.cardashboard.model.applications.EmptyDashboardApplication;
import cz.blahami2.cardashboard.model.helper.CardSizeCalculator;
import cz.blahami2.cardashboard.utils.DpConvertor;
import cz.blahami2.cardashboard.utils.ViewUtils;
import it.gmariotti.cardslib.library.cards.material.MaterialLargeImageCard;
import it.gmariotti.cardslib.library.view.CardViewNative;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppListFragment extends Fragment {
    private static final String ARG_ROWS = "rows";
    private static final String ARG_COLUMNS = "columns";
    private static final String ARG_PAGE_NUMBER = "page_number";

    private int mRows;
    private int mColumns;
    private int mPageNumber;
    private ApplicationsAdapter mApplicationsAdapter;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AppListFragment.
     */
    public static AppListFragment newInstance( int pageNumber, int rows, int columns ) {
        AppListFragment fragment = new AppListFragment();
        Bundle args = new Bundle();
        args.putInt( ARG_ROWS, rows );
        args.putInt( ARG_COLUMNS, columns );
        args.putInt( ARG_PAGE_NUMBER, pageNumber );
        fragment.setArguments( args );
        return fragment;
    }

    public AppListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if ( getArguments() != null ) {
            mRows = getArguments().getInt( ARG_ROWS );
            mColumns = getArguments().getInt( ARG_COLUMNS );
            mPageNumber = getArguments().getInt( ARG_PAGE_NUMBER );
        }
        mApplicationsAdapter = new ApplicationsAdapter( getActivity(), mPageNumber, mRows * mColumns );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        Log.d( getClass().getName(), "creating view" );
        Log.d( getClass().getName(), "container = " + container.getHeight() + ", " + container.getWidth() );

        View view = inflater.inflate( R.layout.fragment_app_list, container, false );

        int height = container.getHeight();
        int width = container.getWidth();
        CardSizeCalculator.Size cardSize = new CardSizeCalculator( getActivity() ).getOptimalCardSize( height, width );
//        int availableCells = columns * rows;
//        int leftMargin = (availableWidth - columns * sideSpaceRequiredPerCard) / 2;
//        int topMargin = (availableHeight - rows * sideSpaceRequiredPerCard) / 2;


        LinearLayout appContainer = (LinearLayout) view.findViewById( R.id.app_list_container );
        int spacing = getResources().getDimensionPixelSize( R.dimen.app_list_card_spacing );
        int right = spacing;
        int bottom = spacing;
        int left;
        int top;

        for ( int i = 0; i < mRows; i++ ) {
            LinearLayout row = new LinearLayout( getActivity().getApplicationContext() );
            row.setOrientation( LinearLayout.HORIZONTAL );
            top = (i == 0) ? spacing : 0;
            for ( int j = 0; j < mColumns; j++ ) {
                final AbstractDashboardApplication applicationItem = mApplicationsAdapter.getItem( i * mColumns + j );
                CardViewNative cardView = (CardViewNative) inflater.inflate( R.layout.app_list_card, container, false );
                ViewPager.LayoutParams params = (ViewPager.LayoutParams) cardView.getLayoutParams();
                params.height = cardSize.height;
                params.width = cardSize.width;
                cardView.setLayoutParams( params );
                left = (j == 0) ? spacing : 0;
                ViewUtils.addMarginsInDp( cardView, left, top, right, bottom );
                MaterialLargeImageCard card = MaterialLargeImageCard.with( getActivity().getApplicationContext() )
                        .setTextOverImage( applicationItem.getTitle() )
                        .useDrawableId( applicationItem.getImageDrawableId() )
                        .build();
                if ( applicationItem instanceof EmptyDashboardApplication ) {
                    cardView.setVisibility( View.INVISIBLE );
                }
                cardView.setCard( card );
                cardView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick( View v ) {
                        applicationItem.startApplicationActivity( getActivity() );
                    }
                } );

                TextView overtext = (TextView) cardView.findViewById( R.id.card_thumbnail_image_text_over );
                overtext.setBackgroundColor( getResources().getColor( R.color.card_thumbnail_image_text_over_background ) );

//                CardView cardView = (CardView) inflater.inflate(R.layout.app_list_card, container, false);
//                TextView textView = (TextView) card.findViewById(R.id.app_list_card_title);
//                textView.setText("This is cell on coordinates [" + i + ", " + j + "].");
//                Log.d(getClass().getName(), "creating cell [" + i + ", " + j + "]");
//                ImageView imageView = (ImageView) cardView.findViewById(R.id.app_list_card_image);
//                imageView.setImageDrawable(getResources().getDrawable(R.drawable.music));
                row.addView( cardView );
            }
            appContainer.addView( row );
        }
        return view;
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch ( ClassCastException e ) {
            throw new ClassCastException( activity.toString()
                    + " must implement OnFragmentInteractionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction( Uri uri );
    }

}
