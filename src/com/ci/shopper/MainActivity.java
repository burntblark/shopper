package com.ci.shopper;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.view.ContextMenu.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.ci.shopper.db.*;
import com.ci.shopper.provider.*;


public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>
{
	static final int EDIT_CATEGORY_REQUEST = 1;

	SimpleCursorAdapter catListAdapter;
	
	ListView catListView;

    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		catListView = (ListView) findViewById(R.id.categoriesListView);
		registerForContextMenu(catListView);
		
		catListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> p1, View v, int position, long id)
			{
				Intent intent = new Intent(getApplicationContext(), CategoryViewActivity.class);
				
				intent.putExtra(CategoriesTable._ID, id);
				startActivity(intent);
			}
		});
		
		View header = getLayoutInflater().inflate(R.layout.cat_summary_header,null);
		catListView.addHeaderView(header);
		
		fillCategories();
	}	
	
	private void fillCategories(){
		String [] fields = new String [] {CategoriesTable.COLUMN_NAME};
		int[] views = new int[] {R.id.categoryName};
		
		getLoaderManager().initLoader(0, null, this);
		catListAdapter = new SimpleCursorAdapter(this, R.layout.category_row, null, fields, views);
	
		catListView.setAdapter(catListAdapter);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int p1, Bundle p2)
	{
		String[] projection = { CategoriesTable._ID, CategoriesTable.COLUMN_NAME, CategoriesTable.COLUMN_DESC};
		CursorLoader cLoader = new CursorLoader(this, CategoryContentProvider.CONTENT_URI, projection, null, null, null);
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == EDIT_CATEGORY_REQUEST)
		{
			if (resultCode == RESULT_OK)
			{
				catListAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.category_add)
		{
			Intent intent = new Intent(this, CategoryEditActivity.class);
			startActivityForResult(intent, EDIT_CATEGORY_REQUEST);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId()==R.id.categoriesListView) {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
			String title = (String) ((TextView) info.targetView
                .findViewById(R.id.categoryName)).getText();
			menu.setHeaderTitle(title);
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.category_context_menu, menu);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		long _id = menuinfo.id; //_id from database in this case
		//to get the position in the adapter -> menuinfo.position
		switch (item.getItemId()) {
			case R.id.category_edit:
				Intent intent = new Intent(getApplicationContext(), CategoryEditActivity.class);
				intent.putExtra(CategoriesTable._ID, _id);
				startActivityForResult(intent, EDIT_CATEGORY_REQUEST);
				break;
			case R.id.category_add_item:
				Toast.makeText(getApplicationContext(), "Add Item", Toast.LENGTH_LONG).show();
				break;
			case R.id.category_delete:
				Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_LONG).show();
				break;
			default: break;
		}
		
		return super.onContextItemSelected(item);
	}
}
