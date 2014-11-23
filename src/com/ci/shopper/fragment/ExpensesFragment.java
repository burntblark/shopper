package com.ci.shopper.fragment;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.ci.shopper.*;
import java.util.Calendar;

public class ExpensesFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private ListView listView;
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

		View header = view.inflate(getActivity().getBaseContext(),
				R.layout.expense_list_header, null);

		listView.addHeaderView(header);
		String[] vest = {};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
				.getBaseContext(), android.R.layout.simple_list_item_1, vest);
		listView.setAdapter(adapter);
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
	public Loader<Cursor> onCreateLoader(int p1, Bundle p2) {
		// TODO: Implement this method
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> p1, Cursor p2) {
		// TODO: Implement this method
	}

	@Override
	public void onLoaderReset(Loader<Cursor> p1) {
		// TODO: Implement this method
	}
	
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getActivity().getFragmentManager(), "datePicker");
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
