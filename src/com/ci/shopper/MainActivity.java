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
import com.ci.shopper.fragment.*;
import android.support.v4.widget.*;


public class MainActivity extends Activity
{
	private static final int EDIT_CATEGORY_REQUEST = 1;

	private static final int EDIT_ITEM_REQUEST = 2;

	private DrawerLayout drawer;

	private FrameLayout frame;

    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_drawer);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		frame = (FrameLayout) findViewById(R.id.content_frame);
		
		Fragment dashboard = new HomeFragment();
		
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.content_frame, dashboard);
		ft.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);

		return true;
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
				Intent intent2 = new Intent(getApplicationContext(), ItemEditActivity.class);
				intent2.putExtra("catId", _id);
				startActivityForResult(intent2, EDIT_ITEM_REQUEST);
				break;
			case R.id.category_delete:
				Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_LONG).show();
				break;
			default: break;
		}
		
		return super.onContextItemSelected(item);
	}
}
