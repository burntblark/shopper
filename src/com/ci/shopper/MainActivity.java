package com.ci.shopper;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.ci.shopper.db.*;
import java.util.*;
import android.util.*;


public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>
{
	static final int EDIT_CATEGORY_REQUEST = 1;

	SimpleCursorAdapter adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		fillCategories();
	}	
	
	private void fillCategories(){
		String [] fields = new String [] {CategoriesTable.COLUMN_NAME_NAME};
		int[] views = new int[] {R.id.categoriesTextView};
		
		getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(this, R.layout.categories, null, fields, views);
		
		ListView catListView = (ListView) findViewById(R.id.categoriesListView);
		catListView.setAdapter(adapter);
		Log.w("ShopperLog", "Got here");
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int p1, Bundle p2)
	{
		String[] projection = { CategoriesTable._ID, CategoriesTable.COLUMN_NAME_NAME, CategoriesTable.COLUMN_NAME_DESC};
		CursorLoader cLoader = new CursorLoader(this, CategoryContentProvider.CONTENT_URI, projection, null, null, null);
		
		return cLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data)
	{
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader loader)
	{
		adapter.swapCursor(null);
		
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
				adapter.notifyDataSetChanged();
			}
		}

		//super.onActivityResult(requestCode, resultCode, data);
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

	public class LazyAdapter extends BaseAdapter
	{

		private Activity activity;
		private ArrayList<HashMap<String, String>> data;
		private LayoutInflater inflater=null;

		public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d)
		{
			activity = a;
			data = d;
			inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		public int getCount()
		{
			return data.size();
		}

		public Object getItem(int position)
		{
			return data.get(position);
		}

		public long getItemId(int position)
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			View vi=convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.categories, null);

			TextView title = (TextView)vi.findViewById(R.id.categoriesTextView); // title
			TextView artist = (TextView)vi.findViewById(R.id.itemCount); // artist name

			HashMap<String, String> item = new HashMap<String, String>();
			item = data.get(position);

			// Setting all values in listview
			vi.setTag(item.get("id"));

			title.setText(item.get("name"));
			artist.setText(item.get("description"));

			return vi;
		}
	}
}


	

	
