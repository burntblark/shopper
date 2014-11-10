package com.ci.shopper.dialog;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.ci.shopper.*;
import com.ci.shopper.provider.*;

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

					values.put("name", ((EditText) view.findViewById(R.id.catName)).getText().toString());
					//values.put("description", ((EditText) findViewById(R.id.catDesc)).getText().toString());

					getActivity().getContentResolver().insert(CategoryContentProvider.CONTENT_URI, values);

					Toast.makeText(getActivity().getApplicationContext(), R.string.category_edited, Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
				}
			});

		AlertDialog dialog = builder.create();

		return dialog;
	}

}
	
