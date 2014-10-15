package com.ci.shopper;

import android.app.*;
import android.os.*;
import android.widget.*;
import java.util.*;
import android.view.*;

public class CategoryViewActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_view);
		
		//getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int catId = extras.getInt("CATEGORY_ID");
			HashMap <String, String> category = MainActivity.categories.get(catId);
			Toast.makeText(getApplicationContext(), "Category ID: " + catId, Toast.LENGTH_SHORT).show();
			setTitle(category.get("name"));
		}else{
			//finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
