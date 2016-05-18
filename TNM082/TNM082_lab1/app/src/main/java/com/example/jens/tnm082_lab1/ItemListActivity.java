package com.example.jens.tnm082_lab1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.jens.tnm082_lab1.database.Datasource;
import com.example.jens.tnm082_lab1.database.Item;
import com.example.jens.tnm082_lab1.dummy.AddDialog;
import com.example.jens.tnm082_lab1.dummy.DummyContent;
import com.example.jens.tnm082_lab1.dummy.EditDialog;
import com.example.jens.tnm082_lab1.dummy.SortDialog;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity
                              implements AddDialog.AddDialogListener,
                                         SortDialog.SortDialogListener{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ActionMode mActionMode;
    private Datasource mDS;
    private ArrayList<Item> mArrayList;
    public SimpleItemRecyclerViewAdapter mAdapter;
    private long mActivatedPosition;
    private ItemDetailFragment fragment;
    View recyclerView;
    private int col;
    private boolean asc;

    private boolean ascending = true;
    private boolean descending = false;

    private int sort = 0;

    private long itemID;

    public static final String PREFS_NAME = "MyPrefsFile";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        sort = settings.getInt("sortSettings", 0);
        ascending = settings.getBoolean("ascendingSettings", false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView, sort, true);


        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.ascending_order).setChecked(ascending);
        return true;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, int col, boolean asc) {
        mAdapter = new SimpleItemRecyclerViewAdapter(col, ascending);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ItemList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.jens.tnm082_lab1/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ItemList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.jens.tnm082_lab1/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("sortSettings", sort);
        editor.putBoolean("ascendingSettings", ascending);

        // Commit the edits!
        editor.commit();
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {


        public SimpleItemRecyclerViewAdapter(int col, boolean ascending) {

            //fetch items from database and store in arraylist
            openDB();
            mArrayList = mDS.fetchAll(col, ascending);
            closeDB();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Inflate with layout for the items
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mTitleView;
            public Item mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                //Bind to the layout
                mIdView = (TextView) view.findViewById(R.id.id);
                mTitleView = (TextView) view.findViewById(R.id.name);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTitleView.getText() + "'";
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mArrayList.get(position);
            holder.mIdView.setText("" + mArrayList.get(position).id);
            holder.mTitleView.setText(mArrayList.get(position).getTitle());

            holder.mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (mTwoPane) {
                        Bundle arguments = new Bundle();

                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, "" + holder.mItem.getId());
                        arguments.putString(ItemDetailFragment.ARG_ITEM_TITLE, holder.mItem.getTitle());
                        arguments.putString(ItemDetailFragment.ARG_ITEM_DESCRIPTION, holder.mItem.getDescription());
                        arguments.putString(ItemDetailFragment.ARG_ITEM_RATING, "" + holder.mItem.getRating());

                        fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();

                        mActivatedPosition = holder.mItem.getId();
                        mActionMode = startActionMode(mActionModeCallback);

                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);

                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_TITLE, holder.mItem.getTitle());
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_DESCRIPTION, holder.mItem.getDescription());
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_RATING, holder.mItem.getRating());

                        startActivityForResult(intent, 0);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }
    }

    //When you select an item in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
            return true;
        }

        switch (item.getItemId()) {
            case R.id.add:
                addItemDialog();
                return true;
            case R.id.help:
                Log.d("TAG", "help");
                return true;
            case R.id.sorting:
                Log.d("TAG", "sorting");
                sortItemDialog();
                return true;
            case R.id.ascending_order:
                Log.d("TAG", "ascending");

                if(item.isChecked()) {
                    item.setChecked(false);
                    ascending = false;
                    sortList(0,ascending);
                }
                else {
                    item.setChecked(true);
                    ascending = true;
                    sortList(0,ascending);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.options_menu_item, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.add:
                    addItemDialog();
                    return true;
                case R.id.delete_item:
                    if(mArrayList.size() != 0) {
                        deletePost(mActivatedPosition);
                        if(fragment != null)
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        mode.finish();
                                            }
                    return true;

                case R.id.edit_item:
                    return true;

                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == 0){
           if(resultCode == Activity.RESULT_OK){
               long itemId = data.getLongExtra(ItemDetailFragment.ARG_ITEM_ID, 0);
               deletePost(itemId);
           }
        }

    }

    private void deletePost(long itemID){
        openDB();

        ListIterator<Item> listIter = mArrayList.listIterator();
        while(listIter.hasNext()){
                if(listIter.next().getId() == itemID) {
                    listIter.remove();
                    break;
                }
            }

        mAdapter.notifyDataSetChanged();
        mDS.deleteItem(itemID);
        closeDB();
    }

    private void openDB(){
        mDS = new Datasource(this);
        mDS.open();
    }

    private void closeDB(){
        mDS.close();
    }

    public void addItemDialog() {
        DialogFragment dialog = new AddDialog();
        dialog.show(getSupportFragmentManager(), "additem");
    }

    public void sortItemDialog() {
        DialogFragment dialog = new SortDialog();
        dialog.show(getSupportFragmentManager(), "sortitem");
    }

    public void onAddDialogPositiveClick(DialogFragment dialog, String mName, String mDesc) {
        // User touched the dialog's positive button

        //Add the user input from addDialog to the datasource
        mDS = new Datasource(this);
        mDS.open();
        mDS.insertItem(mName, 1, mDesc);
        mArrayList = mDS.fetchAll(0, ascending);
        mDS.close();
        mAdapter.notifyDataSetChanged();
    }

    public void onAddDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

    }

    public void onSortDialogPositiveClick(DialogFragment dialog, int sortId) {
        // User touched the dialog's positive button
        sort = sortId;
        sortList(sort,ascending);
    }

    public void onSortDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

    }

    public void sortList(int id, boolean ascending){

        mDS = new Datasource(this);
        mDS.open();
        mArrayList = mDS.fetchAll(id, ascending);
        mDS.close();
        mAdapter.notifyDataSetChanged();
    }

}
