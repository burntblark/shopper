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
import com.ci.shopper.model.*;
import java.util.*;

public class ItemEditActivity extends Activity
{
	private Uri itemUri;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_edit);
		
		Intent intent = getIntent();
		Spinner spinner = (Spinner)findViewById(R.id.itemCatId);
		List <SpinnerObject> lables = getSpinnerObjects();
		// Creating adapter for spinner
		ArrayAdapter<SpinnerObject> spinnerAdapter = 
			new ArrayAdapter<SpinnerObject>
				(this, android.R.layout.simple_spinner_dropdown_item, lables);
															  
		spinner.setAdapter(spinnerAdapter);		
		
		if(intent.hasExtra("catId")){
			long catId = intent.getExtras().getLong("catId");
			
			int selectedPosition = spinnerAdapter.getPosition(new SpinnerObject(catId, null));
			spinner.setSelection(selectedPosition);
		}
		
		if(intent.hasExtra(ItemsTable._ID)){
			long _id = intent.getLongExtra(ItemsTable._ID, 0);
			itemUri = Uri.parse(ItemContentProvider.CONTENT_URI + "/" + _id);

			String[] projection = { ItemsTable._ID, ItemsTable.COLUMN_NAME, ItemsTable.COLUMN_DESC, ItemsTable.COLUMN_CATEGORY_ID};
			Cursor cursor = getContentResolver().query(itemUri, projection, null, null, null);
			
			if(cursor.moveToFirst()){
				setTitle(R.string.edit_item);

				((EditText)findViewById(R.id.itemName)).setText(cursor.getString(1));
				((EditText)findViewById(R.id.itemDesc)).setText(cursor.getString(2));
			}
		}

		getActionBar().setDisplayHomeAsUpEnabled(true);
    }
	
	public List < SpinnerObject> getSpinnerObjects(){
		List < SpinnerObject > labels = new ArrayList < SpinnerObject > ();
		
		String[] projection = { CategoriesTable._ID, CategoriesTable.COLUMN_NAME};
		Cursor cursor = getContentResolver().query(CategoryContentProvider.CONTENT_URI, projection, null, null, null);
		// Select All Query
		//String selectQuery = "SELECT  * FROM " + TABLE_LABELS;

		//SQLiteDatabase db = this.getReadableDatabase();
		//Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if ( cursor.moveToFirst () ) {
			do {labels.add ( new SpinnerObject ( cursor.getInt(0) , cursor.getString(1) ) );
			} while (cursor.moveToNext());
		}

		// closing connection
		cursor.close();

		// returning labels
		return labels;
	}
	
	public class SpinnerObject {

		private long databaseId;
		private String databaseValue;

		public SpinnerObject ( long databaseId , String databaseValue ) {
			this.databaseId = databaseId;
			this.databaseValue = databaseValue;
		}

		public long getId () {
			return databaseId;
		}

		public String getValue () {
			return databaseValue;
		}

		@Override
		public String toString () {
			return databaseValue;
		}

		@Override
		public boolean equals(Object o)
		{
			if (o == null) return false;
			if (o == this) return true;
			if (!(o instanceof SpinnerObject)) return false;
			
			return this.getId() == ((SpinnerObject) o).getId();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.item_edit_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.item_save){
			ContentValues values = new ContentValues();

			values.put("name", ((EditText) findViewById(R.id.itemName)).getText().toString());
			values.put("description", ((EditText) findViewById(R.id.itemDesc)).getText().toString());
			SpinnerObject o = (SpinnerObject)((Spinner) findViewById(R.id.itemCatId)).getSelectedItem();
			
			Long catId = o.getId();
			
			values.put("category_id", Long.toString(catId));
			
			if(itemUri == null){
				itemUri = getContentResolver().insert(ItemContentProvider.CONTENT_URI, values);
			}else{
				getContentResolver().update(itemUri, values, null, null);
			}

			Toast.makeText(getApplicationContext(), R.string.item_edited, Toast.LENGTH_SHORT).show();

			setResult(RESULT_OK);
			finish();
		}else if(item.getItemId() == R.id.category_delete){
			if(itemUri != null){
				getContentResolver().delete(itemUri, null, null);
			}
		}

		return super.onOptionsItemSelected(item);
	}
}
