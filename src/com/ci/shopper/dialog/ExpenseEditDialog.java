package com.ci.shopper.dialog;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.ci.shopper.*;
import java.text.*;
import java.util.*;

public class ExpenseEditDialog extends DialogFragment
{
	View view;
	View targetView;

	private String title;

	public ExpenseEditDialog(){
		this.title = "Edit Expenditure";
	}
	
	public ExpenseEditDialog(String title){
		this.title = title;
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(this.title);

		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.expense_edit, null);

		builder.setView(view)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button
					HashMap<String, Object> values = new HashMap<String, Object>();
					//Toast.makeText(getActivity(), Long.toString(getArguments().getLong("item_id")), Toast.LENGTH_SHORT).show();
					values.put("item_id", getArguments().getLong("item_id"));
					values.put("name", title);
					values.put("cost", ((TextView)view.findViewById(R.id.expEditItemCost)).getText().toString());
					values.put("quantity", ((TextView) view.findViewById(R.id.expEditItemQty)).getText().toString());
					
					//values.put("date", Double.toString(quantity));
					
					//tv.setText(formattedPrice);
					//values.put("cost", cost);
					Date date;
					try
					{
						DatePicker dp = (DatePicker)view.findViewById(R.id.expEditDate);
						
						SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
						date = fmt.parse(dp.getYear() + "-" + dp.getMonth() + "-" + dp.getDayOfMonth());
						
					}
					catch (ParseException e)
					{
						//TODO: provide alternative to fetch date
						date = new Date();
					}

					values.put("date", date);
					
					ExpenseItemsActivity activity = (ExpenseItemsActivity) getActivity();
					activity.addExpenditure(values);
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

	public void show(FragmentManager manager, String tag, View v)
	{
		// TODO: Implement this method
		super.show(manager, tag);
		targetView = v;
		//this.cb = (CheckBox) v.findViewById(R.id.checkBox1);
	}

	
}
	
