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
import java.util.*;

public class ExpenseItemsActivity extends ListActivity
{
	SimpleAdapter mAdapter; 		
    LoaderManager loadermanager;		
    CursorLoader cursorLoader;
    private static String TAG="ItemsCursorLoader";

	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mAdapter = new SimpleAdapter(getApplicationContext(), list, R.layout.expense_item_row, 
									 new String[] { "name", "quantity", "date", "cost" },
									 new int[] { R.id.name, R.id.quantity, R.id.date, R.id.cost}){

			
		};
		// Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //  R.layout.content, R.id.tv_content, values);s

        setListAdapter(mAdapter);
	}

	public void addExpenditure(HashMap<String, String> map)
	{
		list.add(map);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		// TODO: Implement this method
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.expense_items_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.item_add:
				//open dialog with item search
				ItemSelectDialog dialog = new ItemSelectDialog();
				dialog.show(getFragmentManager(), ItemSelectDialog.class.getName());

				return true;
		}

		return super.onOptionsItemSelected(item);
	}


}
