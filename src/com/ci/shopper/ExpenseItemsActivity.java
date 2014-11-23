package com.ci.shopper;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.ci.shopper.dialog.*;
import java.text.*;
import java.util.*;

public class ExpenseItemsActivity extends ListActivity
{
	ExpenseListAdapter mAdapter; 		
    LoaderManager loadermanager;		
    CursorLoader cursorLoader;

	ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mAdapter = new ExpenseListAdapter(getApplicationContext(), data);

        setListAdapter(mAdapter);
	}

	public void addExpenditure(HashMap<String, Object> map)
	{
		data.add(map);
		mAdapter.notifyDataSetChanged();
	}

	public class ExpenseListAdapter extends BaseAdapter
	{

		private ArrayList<HashMap<String, Object>> mData;

		private LayoutInflater mInflater;

		public ExpenseListAdapter(Context context, ArrayList<HashMap<String, Object>> data)
		{
			mData = data;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount()
		{
			return mData.size();
		}

		@Override
		public Object getItem(int p1)
		{
			return mData.get(p1);
		}

		@Override
		public long getItemId(int p1)
		{
			return p1;
		}

		@Override
		public View getView(int p1, View convertView, ViewGroup p3)
		{
			View view;
			ViewHolder holder;
			if (convertView == null)
			{
				view = mInflater.inflate(R.layout.expense_item_row, p3, false);

				holder = new ViewHolder();
				holder.name = (TextView)view.findViewById(R.id.name);
				holder.cost = (TextView)view.findViewById(R.id.cost);
				holder.date = (TextView)view.findViewById(R.id.date);
				holder.quantity = (TextView)view.findViewById(R.id.quantity);

				view.setTag(holder);
			}
			else
			{
				view = convertView;
				holder = (ViewHolder)view.getTag();
			}

			holder.name.setText(mData.get(p1).get("name").toString());
			
			double quantity = Double.parseDouble(mData.get(p1).get("quantity").toString());
			double unitCost = Double.parseDouble(mData.get(p1).get("cost").toString());
			String totalCost = formatCurrency(unitCost * quantity);

			holder.cost.setText(totalCost);

			SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d, yyyy");
			String date = fmtOut.format((Date) mData.get(p1).get("date"));

			holder.date.setText(date);
			holder.quantity.setText(formatCurrency(unitCost) + "×" + mData.get(p1).get("quantity"));

			return view;
		}
		
		public String formatCurrency(double value){
			return new DecimalFormat("\u20A6 ##,##0.00").format(value);
		}

		private class ViewHolder
		{
			public TextView name, cost, date, quantity;
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		// TODO: Implement this method
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.expense_items_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.item_add:
				//open dialog with item search
				ItemSelectDialog dialog = new ItemSelectDialog();
				dialog.show(getFragmentManager(), ItemSelectDialog.class.getName());

				return true;
				
			case R.id.done:
				
				return true;
		}

		return super.onOptionsItemSelected(item);
	}


}
