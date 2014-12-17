package com.ci.shopper.provider;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;
import android.util.*;
import com.ci.shopper.db.*;
import java.util.*;

public class OverviewContentProvider extends ContentProvider
{
	private ShopperDbHelper database;

	private static final int EXPENSES = 10;
	private static final int SUMMARY = 20;

	private static final String AUTHORITY = "com.ci.shopper.provider.Overview";

	private static final String BASE_PATH = "overviews";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/overviews";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/overview";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, EXPENSES);
	}


	@Override
	public boolean onCreate()
	{
		database = new ShopperDbHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		Log.i("ShopperLog", "Query db for expenses overview");
		
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		//checkColumns(projection);
		qBuilder.setTables( 
			ExpensesTable.TABLE_NAME + " a Join " + 
			ItemsTable.TABLE_NAME + " b On (a.item_id = b._id) Join " + 
			CategoriesTable.TABLE_NAME + " c On (b.category_id = c._id)" );
			
		String groupBy = null;

		int uriType = sURIMatcher.match(uri);
		
		switch(uriType) {
			case EXPENSES:
				//groupBy = "c._id";
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = qBuilder.query(db, projection, selection, selectionArgs, groupBy, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(Uri p1)
	{
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
					  String[] selectionArgs) {
		return 0;
	}

	private void checkColumns(String[] projection) {
		String[] available = { 
			CategoriesTable.COLUMN_NAME,
			CategoriesTable._ID };

		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			// check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
}
