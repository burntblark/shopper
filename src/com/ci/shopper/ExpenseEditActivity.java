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

public class ExpenseEditActivity extends Activity
{
	private Uri categoryUri;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_view);

		getActionBar().setDisplayHomeAsUpEnabled(true);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.expense_edit_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.expense_save){
			ContentValues values = new ContentValues();
			
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
