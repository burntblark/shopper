package com.ci.shopper.fragment;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.ci.shopper.*;
import com.ci.shopper.widget.*;
import com.ci.shopper.db.*;
import com.ci.shopper.provider.*;
import com.jjoe64.graphview.*;
import com.jjoe64.graphview.GraphView.*;
import java.util.*;
import android.widget.ExpandableListView.*;

public class ItemsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
	SimpleCursorAdapter listAdapter;

	ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.items, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		initialize();
	}	

	private void initialize()
	{		
		listView = (ListView) getView().findViewById(R.id.item_list);
		TextView emptyView = (TextView) getActivity().findViewById(R.id.emptyView);
		listView.setEmptyView(emptyView);

		registerForContextMenu(listView);

		listView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View v, int position, long id)
				{
					Intent intent = new Intent(getActivity().getApplicationContext(), CategoryViewActivity.class);

					intent.putExtra(CategoriesTable._ID, id);
					startActivity(intent);
				}
			});

		listView.setOnItemClickListener( new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Toast.makeText(getActivity(), "Opens Item Activity", Toast.LENGTH_LONG).show();
				}		
			});
			
			fillList();
	}

	private void fillList()
	{
		String [] fields = new String [] {ItemsTable.COLUMN_NAME};
		int[] views = new int[] {android.R.id.text1};

		getLoaderManager().initLoader(0, null, this);
		listAdapter = new SimpleCursorAdapter(
			getActivity(), 
			android.R.layout.simple_list_item_1, 
			null, 
			fields, 
			views, 0);

		listView.setAdapter(listAdapter);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int p1, Bundle p2)
	{
		String[] projection = { ItemsTable._ID, ItemsTable.COLUMN_NAME};
		CursorLoader cLoader = new CursorLoader(getActivity(), ItemContentProvider.CONTENT_URI, projection, null, null, null);

		return cLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data)
	{
		listAdapter.changeCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
		listAdapter.changeCursor(null);
	}

}
