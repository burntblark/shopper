package com.ci.shopper;

import android.app.*;
import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.ci.shopper.db.*;
import com.ci.shopper.provider.*;

public class CategoryEditActivity extends Activity
{
	private Uri categoryUri;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_edit);
		Intent intent = getIntent();
		if(intent.hasExtra(CategoriesTable._ID)){
			long _id = intent.getLongExtra(CategoriesTable._ID, 0);
			categoryUri = Uri.parse(CategoryContentProvider.CONTENT_URI + "/" + _id);
			
			String[] projection = { CategoriesTable._ID, CategoriesTable.COLUMN_NAME, CategoriesTable.COLUMN_DESC};
			Cursor cursor = getContentResolver().query(categoryUri, projection, null, null, null);
			cursor.moveToFirst();
			
			setTitle(R.string.edit_category);
			
			((EditText)findViewById(R.id.catName)).setText(cursor.getString(1));
			//((EditText)findViewById(R.id.catDesc)).setText(cursor.getString(2));
		}
		
		//getActionBar().setDisplayHomeAsUpEnabled(true);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.category_edit_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.category_save){
			ContentValues values = new ContentValues();
			
			values.put("name", ((EditText) findViewById(R.id.catName)).getText().toString());
			//values.put("description", ((EditText) findViewById(R.id.catDesc)).getText().toString());
			
			if(categoryUri == null){
				categoryUri = getContentResolver().insert(CategoryContentProvider.CONTENT_URI, values);
			}else{
				getContentResolver().update(categoryUri, values, null, null);
			}
			
			Toast.makeText(getApplicationContext(), R.string.category_edited, Toast.LENGTH_SHORT).show();
			
			setResult(RESULT_OK);
			finish();
		}else if(item.getItemId() == R.id.category_delete){
			if(categoryUri != null){
				getContentResolver().delete(categoryUri, null, null);
			}
		}

		return super.onOptionsItemSelected(item);
	}
}
