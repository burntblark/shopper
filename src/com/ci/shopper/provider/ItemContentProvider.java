package com.ci.shopper.provider;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;
import android.text.*;
import java.util.*;
import com.ci.shopper.db.*;
import android.util.*;

public class ItemContentProvider extends ContentProvider
{
	private ShopperDbHelper dbHelper;

	private static final int ITEMS = 10;
	private static final int ITEM_ID = 20;

	private static final String AUTHORITY = "com.ci.shopper.provider.Item";

	private static final String BASE_PATH = "items";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/items";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/item";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, ITEMS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ITEM_ID);
	}


	@Override
	public boolean onCreate()
	{
		dbHelper = new ShopperDbHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		Log.w("ShopperLog", "Querying " + uri.toString());
		
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		checkColumns(projection);
		qBuilder.setTables(ItemsTable.TABLE_NAME);

		int uriType = sURIMatcher.match(uri);
		switch(uriType) {
			case ITEMS:
				break;
			case ITEM_ID:
				qBuilder.appendWhere(ItemsTable._ID + "=" + uri.getLastPathSegment());
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = qBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(Uri p1)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long id = 0;

		switch(uriType){
			case ITEMS:
				id = db.insert(ItemsTable.TABLE_NAME, null, values);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);

		}

		getContext().getContentResolver().notifyChange(uri, null);

		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int rowsDeleted = 0;

		switch(uriType){
			case ITEMS:
				rowsDeleted = db.delete(ItemsTable.TABLE_NAME, selection, selectionArgs);
				break;
			case ITEM_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(ItemsTable.TABLE_NAME, ItemsTable._ID + "=" + id, null);
				}else{
					rowsDeleted = db.delete(ItemsTable.TABLE_NAME, ItemsTable._ID + "=" + id + " and " + selection, selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);

		}

		getContext().getContentResolver().notifyChange(uri, null);

		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
					  String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
			case ITEMS:
				rowsUpdated = sqlDB.update(ItemsTable.TABLE_NAME, 
										   values, 
										   selection,
										   selectionArgs);
				break;
			case ITEM_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = sqlDB.update(ItemsTable.TABLE_NAME, 
											   values,
											   ItemsTable._ID + "=" + id, 
											   null);
				} else {
					rowsUpdated = sqlDB.update(ItemsTable.TABLE_NAME, 
											   values,
											   ItemsTable._ID + "=" + id 
											   + " and " 
											   + selection,
											   selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection) {
		String[] available = { ItemsTable.COLUMN_NAME,
			ItemsTable.COLUMN_BARCODE,
			ItemsTable.COLUMN_CATEGORY_ID,
			ItemsTable._ID };

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
