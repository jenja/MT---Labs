package com.example.jens.tnm082_lab1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jens.tnm082_lab1.database.Datasource;
import com.example.jens.tnm082_lab1.dummy.AddDialog;
import com.example.jens.tnm082_lab1.dummy.EditDialog;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity implements EditDialog.EditDialogListener{

    private long itemID;
    private Datasource mDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            //Send items
            itemID = getIntent().getLongExtra(ItemDetailFragment.ARG_ITEM_ID, 0);

            arguments.putLong(ItemDetailFragment.ARG_ITEM_ID, itemID);

            arguments.putString(ItemDetailFragment.ARG_ITEM_TITLE,
                    getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_TITLE));

            arguments.putString(ItemDetailFragment.ARG_ITEM_DESCRIPTION,
                    getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_DESCRIPTION));

            arguments.putString(ItemDetailFragment.ARG_ITEM_RATING,
                    getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_RATING));


            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
                return true;
            case R.id.delete_item:
                Intent intent = new Intent();
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, itemID);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
            case R.id.edit_item:
                editItemDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_item, menu);
        return true;
    }

    public void editItemDialog() {
        DialogFragment dialog = new EditDialog();
        dialog.show(getSupportFragmentManager(), "edititem");
    }

    public void onEditDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

    }

    public void onEditDialogPositiveClick(DialogFragment dialog, String mName, String mDesc) {
        // User touched the dialog's positive button

        //Add the user input from addDialog to the datasource
        mDS = new Datasource(this);
        mDS.open();
        mDS.updateItem(itemID, mName, 1, mDesc);
        mDS.close();
        finish();
    }

}
