package com.ci.shopper.fragment;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.view.ContextMenu.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.ExpandableListView.*;
import com.ci.shopper.*;
import com.ci.shopper.db.*;
import com.ci.shopper.dialog.*;
import com.ci.shopper.provider.*;
import com.jjoe64.graphview.*;
import com.jjoe64.graphview.GraphView.*;

import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
	SimpleCursorTreeAdapter catListAdapter;

	ExpandableListView catListView;
	
	static final int EDIT_CATEGORY_REQUEST = 1;

	static final int EDIT_ITEM_REQUEST = 2;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

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
	}	

	private void initialize()
	{		
		catListView = (ExpandableListView) getView().findViewById(R.id.lvExp);
		TextView emptyView = (TextView) getActivity().findViewById(R.id.emptyView);
		catListView.setEmptyView(emptyView);

		View header = getActivity().getLayoutInflater().inflate(R.layout.cat_summary_header, null);
		catListView.addHeaderView(header);

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

		catListView.setOnChildClickListener(new OnChildClickListener(){
				@Override
				public boolean onChildClick(ExpandableListView p1, View p2, int p3, int p4, long p5)
				{
					Toast.makeText(getActivity(), "Opens Items Activity", Toast.LENGTH_LONG).show();
					return false;
				}		
			});
			
		fillCategories();
		
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
				String[] projection = { ItemsTable._ID, ItemsTable.COLUMN_NAME, ItemsTable.COLUMN_BARCODE};

				return getActivity().getContentResolver().query(ItemContentProvider.CONTENT_URI, projection, "category_id = '" + groupId + "'", null, null);
			}


		};

		catListView.setAdapter(catListAdapter);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int p1, Bundle p2)
	{
		String[] projection = { CategoriesTable._ID, CategoriesTable.COLUMN_NAME};
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.category_add:
				DialogFragment dialog = new CategoryEditDialog();
				dialog.show(getFragmentManager(), "CategoryEditDialog");

				return true;
			case R.id.item_add:
				DialogFragment dialog2 = new ItemEditDialog();
				dialog2.show(getFragmentManager(), "ItemEditDialog");

				return true;
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		if (v.getId() == R.id.categoriesListView)
		{
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
			String title = (String) ((TextView) info.targetView
                .findViewById(R.id.categoryName)).getText();
			menu.setHeaderTitle(title);
			MenuInflater inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.category_context_menu, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		long _id = menuinfo.id; //_id from database in this case
		//to get the position in the adapter -> menuinfo.position
		switch (item.getItemId())
		{
			case R.id.category_edit:
				Intent intent = new Intent(getActivity().getApplicationContext(), CategoryEditActivity.class);
				intent.putExtra(CategoriesTable._ID, _id);
				startActivityForResult(intent, EDIT_CATEGORY_REQUEST);
				break;
			case R.id.category_add_item:
				Intent intent2 = new Intent(getActivity().getApplicationContext(), ItemEditActivity.class);
				intent2.putExtra("catId", _id);
				startActivityForResult(intent2, EDIT_ITEM_REQUEST);
				break;
			case R.id.category_delete:
				Toast.makeText(getActivity().getApplicationContext(), "Delete", Toast.LENGTH_LONG).show();
				break;
			default: break;
		}

		return super.onContextItemSelected(item);
	}
}
