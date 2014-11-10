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

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
	SimpleCursorTreeAdapter catListAdapter;

	ExpandableListView catListView;

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

		initialize();

		fillCategories();

		GraphViewData[] data = new GraphViewData[30];
		double value = 0.0d;
		for (int i = 1; i <= data.length; i++)
		{
			value += Math.random() * 50 - 1;
			data[i - 1] = new GraphViewData(i, value);
		}
		// init example series data
		GraphViewSeries exampleSeries = new GraphViewSeries(data);

		GraphView graphView = new LineGraphView(
		    getActivity().getApplicationContext() // context
		    , "Perfomance" // heading
		);
		graphView.addSeries(exampleSeries); // data

		LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.summary_chart);
		layout.addView(graphView);
	}	

	private void initialize()
	{		
		catListView = (ExpandableListView) getView().findViewById(R.id.lvExp);
		TextView emptyView = (TextView) getActivity().findViewById(R.id.emptyView);
		catListView.setEmptyView(emptyView);

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
		
		catListView.setOnChildClickListener( new OnChildClickListener(){
				@Override
				public boolean onChildClick(ExpandableListView p1, View p2, int p3, int p4, long p5)
				{
					Toast.makeText(getActivity(), "Opens Items Activity", Toast.LENGTH_LONG).show();
					return false;
				}		
		});
	}

	private void fillCategories()
	{
		String [] groupFields = new String [] {CategoriesTable.COLUMN_NAME};
		int[] groupViews = new int[] {R.id.categoryName};

		String [] childFields = new String [] {CategoriesTable.COLUMN_NAME};
		int[] childViews = new int[] {R.id.itemName};

		getLoaderManager().initLoader(0, null, this);
		catListAdapter = new SimpleCursorTreeAdapter(
			getActivity(), 
			null,
			R.layout.category_row, 
			groupFields, 
			groupViews,
			R.layout.item_row,
			childFields, 
			childViews){

			@Override
			protected Cursor getChildrenCursor(Cursor p1)
			{
				long groupId = p1.getLong(0);
				String[] projection = { ItemsTable._ID, ItemsTable.COLUMN_NAME, ItemsTable.COLUMN_DESC};
				
				return getActivity().getContentResolver().query(ItemContentProvider.CONTENT_URI, projection, "category_id = '"+groupId+"'", null, null);
			}


		};

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
		catListAdapter.changeCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
		catListAdapter.changeCursor(null);
	}

}
