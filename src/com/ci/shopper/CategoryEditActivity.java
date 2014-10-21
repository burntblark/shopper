package com.ci.shopper;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import com.ci.shopper.db.*;
import android.net.*;

public class CategoryEditActivity extends Activity
{
	private Uri categoryUri;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_edit);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.category_edit_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		if (item.getItemId() == R.id.category_save){
			//Intent intent = new Intent(this, CategoryEditActivity.class);
			//startActivity(intent);
			ContentValues values = new ContentValues();
			
			//HashMap <String, String> category = new HashMap <String, String> ();
			//category.put("id", Integer.toString(MainActivity.categories.size()));
			values.put("name", ((EditText) findViewById(R.id.catName)).getText().toString());
			values.put("description", ((EditText) findViewById(R.id.catDesc)).getText().toString());
			
			//MainActivity.categories.add(category);
			categoryUri = getContentResolver().insert(CategoryContentProvider.CONTENT_URI, values);
			
			Toast.makeText(getApplicationContext(), R.string.category_edited, Toast.LENGTH_SHORT).show();
			
			setResult(RESULT_OK);
			finish();
		}

		return super.onOptionsItemSelected(item);
	}
}
