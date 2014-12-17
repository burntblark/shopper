package com.ci.shopper.fragment;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.SearchView.*;
import com.ci.shopper.*;
import com.ci.shopper.db.*;
import com.ci.shopper.provider.*;
import com.ci.shopper.dialog.*;

public class ItemsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
	SimpleCursorAdapter listAdapter;

	ListView listView;

	String listFilter;
	
	String[] listFilterArgs;

	private Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

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
		listView.setFastScrollEnabled(true);
		//listView.setFastScrollAlwaysVisible(true);

		TextView emptyView = (TextView) getActivity().findViewById(R.id.emptyView);
		listView.setEmptyView(emptyView);

		EditText q = (EditText) getView().findViewById(R.id.q);
		q.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
				{
					listFilter = ItemsTable.COLUMN_NAME + " LIKE ?";
					listFilterArgs = new String[]{"%" + cs.toString() + "%"};
					
					getLoaderManager().restartLoader(0, null, ItemsFragment.this);
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
											  int arg3)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable arg0)
				{
					// TODO Auto-generated method stub                          
				}
			});

		q.requestFocus();

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

		listView.setOnItemClickListener(new OnItemClickListener(){

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
		String [] fields = new String [] {ItemsTable.FIELD_NAME, ItemsTable.FIELD_CATEGORY};
		int[] views = new int[] {R.id.name, R.id.category};

		getLoaderManager().initLoader(0, null, this);
		listAdapter = new SimpleCursorAdapter(
			getActivity(), 
			R.layout.items_list_row, 
			null, 
			fields, 
			views, 0);

		listView.setAdapter(listAdapter);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int p1, Bundle p2)
	{
		String[] projection = { ItemsTable.COLUMN_ID, ItemsTable.COLUMN_NAME, ItemsTable.COLUMN_CATEGORY};
		CursorLoader cLoader = new CursorLoader(getActivity(), ItemContentProvider.CONTENT_URI, projection, listFilter, listFilterArgs, ItemsTable.COLUMN_NAME + " ASC");

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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.items_menu, menu);

		this.menu = menu;

		SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();

        search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));

        search.setOnQueryTextListener(new OnQueryTextListener() {

				@Override
				public boolean onQueryTextSubmit(String p1)
				{
					// TODO: Implement this method
					return true;
				}


				@Override 
				public boolean onQueryTextChange(String query)
				{
					if (query != null && !query.isEmpty())
					{
						search(query);
						return true; 
					}

					return true;
				} 

			});
	}

	private void search(String query)
	{
		//search db here
		String[] projection = new String[]{ItemsTable._ID, ItemsTable.COLUMN_NAME};
		Cursor cursor = getActivity().getContentResolver().query(ItemContentProvider.CONTENT_URI, projection, "name LIKE ?", new String[]{"%" + query + "%"}, null);
		//SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();

        search.setSuggestionsAdapter(new CursorAdapter(getActivity(), cursor, true){

				private TextView text;

				@Override
				public View newView(Context p1, Cursor p2, ViewGroup p3)
				{
					LayoutInflater inflater = (LayoutInflater) p1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

					View view = inflater.inflate(android.R.layout.simple_list_item_1, p3, false);

					text = (TextView) view.findViewById(android.R.id.text1);

					return view;
				}

				@Override
				public void bindView(View p1, Context p2, Cursor p3)
				{
					text.setText(p3.getString(1));
				}
			});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.search:
				return true;
			case R.id.item_add:
				(new ItemEditDialog())
					.show(getFragmentManager(), "ItemEditDialog");

				return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
