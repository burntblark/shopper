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
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(this.title);

		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.expense_edit, null);

		builder.setView(view)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button
					HashMap<String, String> values = new HashMap<String, String>();
					
					values.put("name", title);
					//CheckBox cb = (CheckBox) targetView.findViewById(R.id.checkBox1);
					//TextView tv = (TextView) targetView.findViewById(R.id.expItemCost);
					//TextView tvDate = (TextView) targetView.findViewById(R.id.expItemDate);
					
					//cb.setChecked(true);
					double cost = Double.parseDouble(((TextView)view.findViewById(R.id.expEditItemCost)).getText().toString());
					double quantity = Double.parseDouble(((TextView) view.findViewById(R.id.expEditItemQty)).getText().toString());
					
					values.put("quantity", Double.toString(quantity));
					
					String formattedPrice = new DecimalFormat("\u20A6 ##,##0.00").format(cost*quantity);
					//tv.setText(formattedPrice);
					values.put("cost", formattedPrice);
					try
					{
						DatePicker dp = (DatePicker)view.findViewById(R.id.expEditDate);
						
						SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
						Date date = fmt.parse(dp.getYear() + "-" + dp.getMonth() + "-" + dp.getDayOfMonth());
						
						SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
						//tvDate.setText(fmtOut.format(date));
						values.put("date", fmtOut.format(date));
					}
					catch (ParseException e)
					{
						//TODO: provide alternative to fetch date
					}
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
	
