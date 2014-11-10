package com.ci.shopper;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.widget.*;
import android.view.*;
import android.view.ContextMenu.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.ci.shopper.db.*;
import com.ci.shopper.dialog.*;
import com.ci.shopper.fragment.*;
import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v4.app.Fragment;

public class MainActivity extends Activity
{
	private static final int EDIT_CATEGORY_REQUEST = 1;

	private static final int EDIT_ITEM_REQUEST = 2;

	private DrawerLayout drawer;

	private FrameLayout frame;

	private Fragment dashboard;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_drawer);
		
		setLeftDrawer((DrawerLayout) findViewById(R.id.drawer_layout));
		setContentFrame((FrameLayout) findViewById(R.id.content_frame));
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, getLeftDrawer(),
												  R.drawable.ic_drawer, R.string.drawer_open,
												  R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                //Set the title on the action when drawer open
                //getActionBar().setTitle(mDrawerTitle);
                super.onDrawerOpened(drawerView);
            }
        };

        getLeftDrawer().setDrawerListener(mDrawerToggle);
		
		
		initialize();
	}
	
	private void initialize() {
		dashboard = new HomeFragment();
		
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.content_frame, dashboard)
				.commit();
	}

	public DrawerLayout getLeftDrawer() {
		return drawer;
	}

	public void setLeftDrawer(DrawerLayout drawer) {
		this.drawer = drawer;
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
		switch(item.getItemId()){
			case R.id.item_add:
				DialogFragment dialog = new ItemEditDialog();
				dialog.show(getFragmentManager(), "CategoryEditDialog");
			
				return true;
			case R.id.expense_new:
				Intent intent = new Intent(this, ExpenseItemsActivity.class);
				startActivity(intent);
				
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

	public FrameLayout getContentFrame() {
		return frame;
	}

	public void setContentFrame(FrameLayout frame) {
		this.frame = frame;
	}
}
