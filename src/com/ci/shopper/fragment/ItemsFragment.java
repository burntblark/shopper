package com.ci.shopper.fragment;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.SearchView.*;
import com.ci.shopper.*;
import com.ci.shopper.db.*;
import com.ci.shopper.provider.*;

public class ItemsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
	SimpleCursorAdapter listAdapter;

	ListView listView;

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
					return false;
				}
				

				@Override 
				public boolean onQueryTextChange(String query) {

					search(query);

					return true; 

				} 

			});
	}

	private void search(String query){
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
		switch(item.getItemId()){
			case R.id.search:
				return true;
		}
		
		return super.onOptionsItemSelected(item);
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
