package com.ci.shopper;

import android.app.*;
import android.content.res.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.widget.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.ci.shopper.fragment.*;
import java.util.*;

import android.app.Fragment;
import android.app.FragmentManager;

public class MainActivity extends Activity
{
	private DrawerLayout drawer;
    private View leftDrawer;
    private View rightDrawer;

	private FrameLayout frame;

	private ListView mDrawerList;

    ActionBarDrawerToggle mDrawerToggle;
    ActionBarDrawerToggle rightDrawerToggle;


    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_drawer);

		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

		setLeftDrawer((DrawerLayout) findViewById(R.id.drawer_layout));
        rightDrawer = findViewById(R.id.right_drawer_menu);
        leftDrawer = findViewById(R.id.left_drawer_menu);
		setContentFrame((FrameLayout) findViewById(R.id.content_frame));

	    mDrawerList = (ListView) findViewById(R.id.left_drawer_menu);

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

		initialize();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	private void initialize()
	{
		String[] names = new String[]{
			"Overview",
			"Expenditure",
			"Items",
			"Categories",
			"Shopping List",
			"Preferences"
		};

	    /*Array of Images*/
	    int[] icons = new int[] {
			R.drawable.ic_action_overflow,
			R.drawable.ic_action_accept,
			R.drawable.ic_action_new, 
			R.drawable.ic_action_overflow,
			R.drawable.ic_action_new, 
			R.drawable.ic_action_overflow
	    };

	    final List<HashMap<String, String>> listinfo = new ArrayList<HashMap<String, String>>();
	    listinfo.clear();
	    for (int i=0;i < Math.min(names.length, icons.length);i++)
		{
	        HashMap<String, String> menuInfo = new HashMap<String, String>();
	        menuInfo.put("name", names[i]);
	        menuInfo.put("image", Integer.toString(icons[i]));
	        listinfo.add(menuInfo);
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

				ImageView img = (ImageView)v.findViewById(to[0]);
				img.setImageResource(Integer.parseInt(obj.get(from[0])));
	            return v;
	        }
	    };

	    mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener((new OnItemClickListener(){

											   Fragment fragment;

											   @Override
											   public void onItemClick(AdapterView<?> parent, View view, int position, long id)
											   {
												   selectItem(position);
											   }

											   private OnItemClickListener selectItem(int position)
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
													   case 2:
														   fragment = new ItemsFragment();
														   break;

												   }

												   // Insert the fragment by replacing any existing fragment
												   FragmentManager fragmentManager = getFragmentManager();
												   fragmentManager.beginTransaction()
													   .replace(R.id.content_frame, fragment)
													   .commit();

												   // Highlight the selected item, update the title, and close the drawer
												   mDrawerList.setItemChecked(position, true);
												   getLeftDrawer().closeDrawer(mDrawerList);

												   return this;
											   }

										   }).selectItem(0));
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}


	public DrawerLayout getLeftDrawer()
	{
		return drawer;
	}

	public void setLeftDrawer(DrawerLayout drawer)
	{
		this.drawer = drawer;
	}

	public ActionBarDrawerToggle getDrawerToggle()
	{
		return mDrawerToggle;
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
