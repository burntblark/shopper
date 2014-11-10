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

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("Edit Expense");

		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.expense_edit, null);

		builder.setView(view)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button
					//ContentValues values = new ContentValues();
					CheckBox cb = (CheckBox) targetView.findViewById(R.id.checkBox1);
					TextView tv = (TextView) targetView.findViewById(R.id.expItemCost);
					TextView tvDate = (TextView) targetView.findViewById(R.id.expItemDate);
					
					cb.setChecked(true);
					double cost = Double.parseDouble(((TextView)view.findViewById(R.id.expEditItemCost)).getText().toString());
					String formattedPrice = new DecimalFormat("\u20A6 ##,##0.00").format(cost);
					tv.setText(formattedPrice);
					
					DatePicker dp = (DatePicker)view.findViewById(R.id.expEditDate);
					try
					{
						SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
						Date date = fmt.parse(dp.getYear() + "-" + dp.getMonth() + "-" + dp.getDayOfMonth());
						
						SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
						tvDate.setText(fmtOut.format(date));
					}
					catch (ParseException e)
					{}

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

	@Override
	public void show(FragmentManager manager, String tag, View v)
	{
		// TODO: Implement this method
		super.show(manager, tag);
		targetView = v;
		//this.cb = (CheckBox) v.findViewById(R.id.checkBox1);
	}

	
}
	
