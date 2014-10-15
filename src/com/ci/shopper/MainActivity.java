package com.ci.shopper;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.zip.*;
import android.content.*;
import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.AdapterView.*;


public class MainActivity extends Activity
{
	static final int EDIT_CATEGORY_REQUEST = 1;
	
	public static ArrayList <HashMap<String, String>> categories = new ArrayList<HashMap<String,String>>(); 
	HashMap<String, String> category; 
	LazyAdapter adapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		if(categories.isEmpty()){
			category = new HashMap<String,String>();
		
			category.put("id", "1");
			category.put("name", "Groceries");
			category.put("description", "Items for the house.");
		
			categories.add(category);
		
			category = new HashMap<String,String>();
		
			category.put("id", "2");
			category.put("name", "Fast Food");
			category.put("description", "Food I buy from local food joints.");

			categories.add(category);
		}
		
		adapter = new LazyAdapter(this, categories);
		ListView listView = (ListView) findViewById(R.id.categoriesListView);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String catId = (String) view.getTag();
				
				Intent intent = new Intent(getApplicationContext(), CategoryViewActivity.class);
				intent.putExtra("CATEGORY_ID", position);
				startActivity(intent);
			}
		});
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
		if (requestCode == EDIT_CATEGORY_REQUEST){
			if (resultCode == RESULT_OK){
				adapter.notifyDataSetChanged();
			}
		}
		
		//super.onActivityResult(requestCode, resultCode, data);
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.category_add){
			Intent intent = new Intent(this, CategoryEditActivity.class);
			startActivityForResult(intent, EDIT_CATEGORY_REQUEST);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public class LazyAdapter extends BaseAdapter {

		private Activity activity;
		private ArrayList<HashMap<String, String>> data;
		private LayoutInflater inflater=null;

		public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
			activity = a;
			data=d;
			inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}

		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			return data.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View vi=convertView;
			if(convertView==null)
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


	

	
