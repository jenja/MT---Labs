package com.example.jens.tnm082_lab1;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jens.tnm082_lab1.database.Item;
import com.example.jens.tnm082_lab1.dummy.DummyContent;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_TITLE = "item_title";
    public static final String ARG_ITEM_DESCRIPTION = "item_description";
    public static final String ARG_ITEM_RATING = "item_rating";

    /**
     * The dummy content this fragment is presenting.
     */

    String ItemId = "failed to load";
    String ItemTitle = "failed to load";
    String ItemDescription = "failed to load";
    int ItemRating = -1;
    private Item mItem;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hasOptionsMenu();

        if(getArguments().containsKey(ARG_ITEM_ID)){ItemTitle = getArguments().getString(ARG_ITEM_ID);}
        if(getArguments().containsKey(ARG_ITEM_TITLE)){ItemTitle = getArguments().getString(ARG_ITEM_TITLE);}
        if(getArguments().containsKey(ARG_ITEM_DESCRIPTION)){ItemDescription= getArguments().getString(ARG_ITEM_DESCRIPTION);}
        if(getArguments().containsKey(ARG_ITEM_RATING)){ItemRating = getArguments().getInt(ARG_ITEM_RATING);}

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(ItemTitle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (ItemDescription != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(ItemDescription);
        }

        return rootView;
    }

}
