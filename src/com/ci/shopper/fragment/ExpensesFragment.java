package com.ci.shopper.fragment;

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

public class ExpensesFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private ListView listView;
	SimpleCursorAdapter listAdapter;
	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.expenses, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listView = (ListView) view.findViewById(R.id.expense_list);

//		View header = view.inflate(getActivity().getBaseContext(),
//				R.layout.expense_list_header, null);
//
//		listView.addHeaderView(header);
		
		String [] fields = new String [] {ExpensesTable.COLUMN_ITEM_NAME};
		int[] views = new int[] {android.R.id.text1};

		getLoaderManager().initLoader(0, null, this);
		listAdapter = new SimpleCursorAdapter(
			getActivity(), 
			android.R.layout.simple_list_item_1, 
			null, 
			fields, 
			views, 0);

		listView.setAdapter(listAdapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		// TODO: Implement this method
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.expenses_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		switch(item.getItemId()){
			case R.id.expense_new:
				Intent intent = new Intent(getActivity(), ExpenseItemsActivity.class);
				startActivity(intent);
				
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	public Loader<Cursor> onCreateLoader(int p1, Bundle p2)
	{
		String[] projection = { "a."+ExpensesTable._ID, "b."+ItemsTable.COLUMN_NAME +" As "+ ExpensesTable.COLUMN_ITEM_NAME};
		CursorLoader cLoader = new CursorLoader(getActivity(), ExpenseContentProvider.CONTENT_URI, projection, null, null, null);

		return cLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data)
	{
		listAdapter.changeCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
		listAdapter.changeCursor(null);
	}
	
	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
		}
	}

}
