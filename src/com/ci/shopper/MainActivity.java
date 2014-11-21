package com.ci.shopper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.widget.*;
import android.view.*;
import android.view.ContextMenu.*;
import android.widget.*;
import android.widget.AdapterView.*;

import com.ci.shopper.db.*;
import com.ci.shopper.dialog.ItemEditDialog;
import com.ci.shopper.fragment.*;

import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v4.app.Fragment;

public class MainActivity extends Activity
{
	private static final int EDIT_CATEGORY_REQUEST = 1;

	private static final int EDIT_ITEM_REQUEST = 2;

	private DrawerLayout drawer;
    private View leftDrawer;
    private View rightDrawer;

	private FrameLayout frame;

	private Fragment dashboard;

	private ListView mDrawerList;
    private ListView rightDrawerList;

    ActionBarDrawerToggle mDrawerToggle;
    ActionBarDrawerToggle rightDrawerToggle;


    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_drawer);

		setLeftDrawer((DrawerLayout) findViewById(R.id.drawer_layout));
        rightDrawer = findViewById(R.id.right_drawer_menu);
        leftDrawer = findViewById(R.id.left_drawer_menu);
		setContentFrame((FrameLayout) findViewById(R.id.content_frame));

	    mDrawerList = (ListView) findViewById(R.id.left_drawer_menu);

		//getActionBar().setHomeButtonEnabled(true);
		//getActionBar().setDisplayHomeAsUpEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, getLeftDrawer(),
												  R.drawable.ic_drawer, 
												  R.string.drawer_open,
												  R.string.drawer_close) {

            public void onDrawerClosed(View view)
			{
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView)
			{
                //Set the title on the action when drawer open
                //getActionBar().setTitle(mDrawerTitle);
                super.onDrawerOpened(drawerView);
            }
			
			
        };

        getLeftDrawer().setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

		initialize();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	private void initialize()
	{
		dashboard = new HomeFragment();

		getFragmentManager()
			.beginTransaction()
			.replace(R.id.content_frame, dashboard)
			.commit();



		String[] names = new String[]{
			"Dashboard",
			"Expenditure",
			"Locations",
			"Settings",
			//"Sports",
		};

	    /*Array of Images*/
	    int[] image = new int[] {
			R.drawable.ic_action_overflow,
			R.drawable.ic_action_accept,
			R.drawable.ic_action_accept, 
			R.drawable.ic_action_accept,
			//R.drawable.ic_action_accept,
	    };

	    final List<HashMap<String, String>> listinfo = new ArrayList<HashMap<String, String>>();
	    listinfo.clear();
	    for (int i=0;i < names.length;i++)
		{
	        HashMap<String, String> hm = new HashMap<String, String>();
	        hm.put("name", names[i]);
	        hm.put("image", Integer.toString(image[i]));
	        listinfo.add(hm);
	    }

	    // Keys used in Hashmap
	    final String[] from = { "image", "name" };
	    final int[] to = {R.id.img, R.id.txt};


	    SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), listinfo, R.layout.drawer_list_item, from, to){
	        @Override
	        public View getView(int pos, View convertView, ViewGroup parent)
			{
	            View v = convertView;
	            if (v == null)
				{
	                LayoutInflater vi = (LayoutInflater)getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
	                v = vi.inflate(R.layout.drawer_list_item, null);
	            }
	            TextView tv = (TextView)v.findViewById(to[1]);
				HashMap<String, String> obj = listinfo.get(pos);
	            tv.setText(obj.get(from[1]));
	            //tv.setTypeface(faceBold);

				ImageView img = (ImageView)v.findViewById(to[0]);
				img.setImageResource(Integer.parseInt(obj.get(from[0])));
	            return v;
	        }
	    };

	    mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new OnItemClickListener(){

				Fragment fragment;

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					// TODO: Implement this method
					selectItem(position);
				}

				private void selectItem(int position)
				{
					switch (position)
					{
						default:
						case 0:
							fragment = new HomeFragment();
							break;
						case 1:
							fragment = new ExpensesFragment();
							break;

					}

					// Insert the fragment by replacing any existing fragment
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment)
						.commit();

					// Highlight the selected item, update the title, and close the drawer
					mDrawerList.setItemChecked(position, true);
					//setTitle(mPlanetTitles[position]);
					getLeftDrawer().closeDrawer(mDrawerList);
				}

			});
	}

	public DrawerLayout getLeftDrawer()
	{
		return drawer;
	}

	public void setLeftDrawer(DrawerLayout drawer)
	{
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
        if (mDrawerToggle.onOptionsItemSelected(item))
		{
            drawer.closeDrawer(rightDrawer);
            return true;
        }
		else if (item.getItemId() == R.id.category_add)
		{
            drawer.closeDrawer(leftDrawer);
            drawer.openDrawer(rightDrawer);
            return  true;
        }
		/*switch(item.getItemId()){
		 case R.id.item_add:
		 DialogFragment dialog = new ItemEditDialog();
		 dialog.show(getFragmentManager(), "CategoryEditDialog");

		 return true;
		 case R.id.expense_new:
		 Intent intent = new Intent(this, ExpenseItemsActivity.class);
		 startActivity(intent);

		 return true;
		 }*/

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
			MenuInflater inflater = getMenuInflater();
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

	public FrameLayout getContentFrame()
	{
		return frame;
	}

	public void setContentFrame(FrameLayout frame)
	{
		this.frame = frame;
	}
}
