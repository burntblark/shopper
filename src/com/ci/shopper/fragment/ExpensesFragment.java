package com.ci.shopper.fragment;
 
import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.ci.shopper.*;

public class ExpensesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
	private ListView listView;
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.expenses, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		listView = (ListView) view.findViewById(R.id.expense_list);

		View header = view
				.inflate(getActivity().getBaseContext(), R.layout.expense_list_header, null);
				
		listView.addHeaderView(header);
        String[] vest = {};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_list_item_1,vest);
        listView.setAdapter(adapter);
	}	
	
	@Override
	public Loader<Cursor> onCreateLoader(int p1, Bundle p2)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> p1, Cursor p2)
	{
		// TODO: Implement this method
	}

	@Override
	public void onLoaderReset(Loader<Cursor> p1)
	{
		// TODO: Implement this method
	}
	
}
