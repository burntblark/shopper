package com.ci.shopper.fragment;
 
import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import com.ci.shopper.*;

public class ExpensesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.expenses, container, false);
		return view;
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
