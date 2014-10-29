package com.ci.shopper.fragment;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.ci.shopper.*;
import com.ci.shopper.db.*;
import com.ci.shopper.provider.*;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
	SimpleCursorAdapter catListAdapter;

	ListView catListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.main, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		catListView = (ListView) getView().findViewById(R.id.categoriesListView);
		registerForContextMenu(catListView);

		catListView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View v, int position, long id)
				{
					Intent intent = new Intent(getActivity().getApplicationContext(), CategoryViewActivity.class);

					intent.putExtra(CategoriesTable._ID, id);
					startActivity(intent);
				}
			});

		View header = getActivity().getLayoutInflater().inflate(R.layout.cat_summary_header, null);
		catListView.addHeaderView(header);

		fillCategories();
	}	

	private void fillCategories()
	{
		String [] fields = new String [] {CategoriesTable.COLUMN_NAME};
		int[] views = new int[] {R.id.categoryName};

		getLoaderManager().initLoader(0, null, this);
		catListAdapter = new SimpleCursorAdapter(getActivity(), R.layout.category_row, null, fields, views);

		catListView.setAdapter(catListAdapter);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int p1, Bundle p2)
	{
		String[] projection = { CategoriesTable._ID, CategoriesTable.COLUMN_NAME, CategoriesTable.COLUMN_DESC};
		CursorLoader cLoader = new CursorLoader(getActivity(), CategoryContentProvider.CONTENT_URI, projection, null, null, null);

		return cLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data)
	{
		catListAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader loader)
	{
		catListAdapter.swapCursor(null);
	}

}
