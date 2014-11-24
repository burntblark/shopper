package com.ci.shopper.dialog;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.ExpandableListView.*;
import com.ci.shopper.*;
import com.ci.shopper.db.*;
import com.ci.shopper.provider.*;

public class ItemSelectDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>
{
	View view;

	private ExpandableListView listView;

	private SimpleCursorTreeAdapter catListAdapter;
	
	private AlertDialog dialog;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("Select Item");

		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.item_select, null);

		builder.setView(view);
		
		listView = (ExpandableListView) view.findViewById(R.id.itemListView);
			
		
		fillCategories();
		
		dialog = builder.create();
		
		listView.setOnChildClickListener(new OnChildClickListener(){
				@Override
				public boolean onChildClick(ExpandableListView p1, View p2, int p3, int p4, long p5)
				{
					ExpenseEditDialog dialog2 = new ExpenseEditDialog(((TextView)p2.findViewById(android.R.id.text1)).getText().toString());
					
					Bundle args = new Bundle();
					args.putLong("item_id", p5);
					dialog2.setArguments(args);
					
					dialog2.show(getFragmentManager(), ExpenseEditDialog.class.getName());
					
					dialog.cancel();
					return false;
				}		
			});
		
		return dialog;
	}
	
	private void fillCategories()
	{
		String [] groupFields = new String [] {CategoriesTable.COLUMN_NAME};
		int[] groupViews = new int[] {android.R.id.text1};

		String [] childFields = new String [] {CategoriesTable.COLUMN_NAME};
		int[] childViews = new int[] {android.R.id.text1};

		getLoaderManager().initLoader(0, null, this);
		catListAdapter = new SimpleCursorTreeAdapter(
			getActivity(), 
			null,
			android.R.layout.simple_expandable_list_item_1, 
			groupFields, 
			groupViews,
			android.R.layout.simple_list_item_1,
			childFields, 
			childViews){

			@Override
			protected Cursor getChildrenCursor(Cursor p1)
			{
				long groupId = p1.getLong(0);
				String[] projection = { ItemsTable._ID, ItemsTable.COLUMN_NAME, ItemsTable.COLUMN_BARCODE};

				return getActivity().getContentResolver().query(ItemContentProvider.CONTENT_URI, projection, "category_id = ?", new String[]{Long.toString(groupId)}, null);
			}


		};

		listView.setAdapter(catListAdapter);
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
}
	
