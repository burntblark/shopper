package com.ci.shopper;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.ci.shopper.db.*;
import com.ci.shopper.dialog.*;
import com.ci.shopper.provider.*;

public class ExpenseItemsActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
	SimpleCursorAdapter mAdapter; 		
    LoaderManager loadermanager;		
    CursorLoader cursorLoader;
    private static String TAG="ItemsCursorLoader";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//loadermanager=getLoaderManager();

		//String[] uiBindFrom = {  ItemsTable.COLUMN_NAME};		
		//int[] uiBindTo = {R.id.expItemName};

        /*Empty adapter that is used to display the loaded data*/
		//mAdapter = new SimpleCursorAdapter(this,R.layout.expense_item_row, null, uiBindFrom, uiBindTo,0);  
        //setListAdapter(mAdapter);
		
		//loadermanager.initLoader(1, null, this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		// TODO: Implement this method
		super.onListItemClick(l, v, position, id);
		
		CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox1);
		if(!cb.isChecked()){
			ExpenseEditDialog dialog = new ExpenseEditDialog();
			dialog.show(getFragmentManager(), ExpenseEditDialog.class.getName(), v);

			//cb.setChecked(true);
		}else{
			cb.setChecked(false);
		}
	}
	
	
	
	@Override
	public Loader<Cursor> onCreateLoader(int p1, Bundle p2)
	{
		String[] projection = { ItemsTable._ID, ItemsTable.COLUMN_NAME };
		cursorLoader = new CursorLoader(this, ItemContentProvider.CONTENT_URI, projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{
		if(mAdapter!=null && cursor!=null)
			mAdapter.swapCursor(cursor); //swap the new cursor in.
		else
			Log.v(TAG,"OnLoadFinished: mAdapter is null");
	}
	

	@Override
	public void onLoaderReset(Loader<Cursor> p1)
	{
		if(mAdapter!=null)
			mAdapter.swapCursor(null);
		else
			Log.v(TAG,"OnLoaderReset: mAdapter is null");
	}
	
}
