package com.ci.shopper;

import android.app.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.ci.shopper.db.*;
import com.ci.shopper.provider.*;
import android.content.*;

public class CategoryViewActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>
{
	private Uri categoryUri;

	SimpleCursorAdapter itemListAdapter;
	
	ListView itemListView;
	
	long catId = 0;
	
	public static final int SAVE_ITEM_REQUEST = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_view);

		//getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		itemListView = (ListView) findViewById(R.id.catItems);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			//get items per category
			catId = extras.getLong(CategoriesTable._ID);
			categoryUri = Uri.parse(CategoryContentProvider.CONTENT_URI + "/" + catId);
			Cursor cursor = getContentResolver().query(categoryUri, new String[] {CategoriesTable.COLUMN_NAME}, null, null, null);

			cursor.moveToFirst();
			setTitle(cursor.getString(0));
			
			fillItems(catId);
		}else{
			//get all items
			fillItems(0);
		}
	}
	
	private void fillItems(long catId){
		String [] fields = new String [] {ItemsTable.COLUMN_NAME};
		int[] views = new int[] {R.id.itemName};
		Bundle b = new Bundle();
		b.putLong("catId", catId);
		
		getLoaderManager().initLoader(1, b, this);
		itemListAdapter = new SimpleCursorAdapter(this, R.layout.item_row, null, fields, views, 1);

		itemListView.setAdapter(itemListAdapter);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int p1, Bundle b)
	{
		String[] projection = { ItemsTable._ID, ItemsTable.COLUMN_NAME, ItemsTable.COLUMN_BARCODE};
		//Toast.makeText(getApplicationContext(), Long.toString(b.getLong("catId")), Toast.LENGTH_LONG).show();
		CursorLoader cLoader = new CursorLoader(this, ItemContentProvider.CONTENT_URI, projection, "category_id = '"+b.getLong("catId", 0)+"'", null, null);
		
		return cLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data)
	{
		itemListAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> p1)
	{
		itemListAdapter.swapCursor(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.category_view_menu, menu);

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == SAVE_ITEM_REQUEST)
		{
			if (resultCode == RESULT_OK)
			{
				itemListAdapter.notifyDataSetChanged();
			}
		}
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.item_edit)
		{
			Intent intent = new Intent(this, ItemEditActivity.class);
			intent.putExtra("catId", catId);
			startActivityForResult(intent, SAVE_ITEM_REQUEST);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
}
