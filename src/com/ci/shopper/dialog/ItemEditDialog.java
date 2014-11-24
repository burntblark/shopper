package com.ci.shopper.dialog;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.ci.shopper.*;
import com.ci.shopper.db.*;
import com.ci.shopper.provider.*;
import java.util.*;

public class ItemEditDialog extends DialogFragment
{
	View view;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("Edit Item");

		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.item_edit, null);
		
		builder.setView(view)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button
					ContentValues values = new ContentValues();

					values.put(ItemsTable.COLUMN_NAME, ((EditText) view.findViewById(R.id.itemName)).getText().toString());
					values.put(ItemsTable.COLUMN_CATEGORY_ID, Long.toString((((SpinnerObject)((Spinner) view.findViewById(R.id.itemCatId)).getSelectedItem())).getId()));
					values.put(ItemsTable.COLUMN_BARCODE, ((EditText) view.findViewById(R.id.itemBarcode)).getText().toString());

					getActivity().getContentResolver().insert(ItemContentProvider.CONTENT_URI, values);

					Toast.makeText(getActivity().getApplicationContext(), R.string.category_edited, Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
				}
			});

		AlertDialog dialog = builder.create();
		
		Spinner spinner = (Spinner) view.findViewById(R.id.itemCatId);
		List <SpinnerObject> lables = getSpinnerObjects();
		// Creating adapter for spinner
		ArrayAdapter<SpinnerObject> spinnerAdapter = 
			new ArrayAdapter<SpinnerObject>
		(getActivity(), android.R.layout.simple_spinner_dropdown_item, lables);

		spinner.setAdapter(spinnerAdapter);		

		
		return dialog;
	}
	
	public List < SpinnerObject> getSpinnerObjects(){
		List < SpinnerObject > labels = new ArrayList < SpinnerObject > ();

		String[] projection = { CategoriesTable._ID, CategoriesTable.COLUMN_NAME};
		Cursor cursor = getActivity().getContentResolver().query(CategoryContentProvider.CONTENT_URI, projection, null, null, null);
		
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
	
}
	
