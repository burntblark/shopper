package com.ci.shopper.db;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;
import android.text.*;
import java.util.*;

public class CategoryContentProvider extends ContentProvider
{
	private ShopperDbHelper database;
	
	private static final int CATEGORIES = 10;
	private static final int CATEGORY_ID = 20;
	
	private static final String AUTHORITY = "com.ci.shopper.db";
	
	private static final String BASE_PATH = "categories";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/categories";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/category";
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, CATEGORIES);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CATEGORY_ID);
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
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		checkColumns(projection);
		qBuilder.setTables(CategoriesTable.TABLE_NAME);
		
		int uriType = sURIMatcher.match(uri);
		switch(uriType) {
			case CATEGORIES:
				break;
			case CATEGORY_ID:
				qBuilder.appendWhere(CategoriesTable._ID + "=" + uri.getLastPathSegment());
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		SQLiteDatabase db = database.getWritableDatabase();
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
		SQLiteDatabase db = database.getWritableDatabase();
		long id = 0;
		
		switch(uriType){
			case CATEGORIES:
				id = db.insert(CategoriesTable.TABLE_NAME, null, values);
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
		SQLiteDatabase db = database.getWritableDatabase();
		int rowsDeleted = 0;

		switch(uriType){
			case CATEGORIES:
				rowsDeleted = db.delete(CategoriesTable.TABLE_NAME, selection, selectionArgs);
				break;
			case CATEGORY_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = db.delete(CategoriesTable.TABLE_NAME, CategoriesTable._ID + "=" + id, null);
				}else{
					rowsDeleted = db.delete(CategoriesTable.TABLE_NAME, CategoriesTable._ID + "=" + id + " and " + selection, selectionArgs);
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
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
			case CATEGORIES:
				rowsUpdated = sqlDB.update(CategoriesTable.TABLE_NAME, 
										   values, 
										   selection,
										   selectionArgs);
				break;
			case CATEGORY_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = sqlDB.update(CategoriesTable.TABLE_NAME, 
											   values,
											   CategoriesTable._ID + "=" + id, 
											   null);
				} else {
					rowsUpdated = sqlDB.update(CategoriesTable.TABLE_NAME, 
											   values,
											   CategoriesTable._ID + "=" + id 
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
		String[] available = { CategoriesTable.COLUMN_NAME_NAME,
			CategoriesTable.COLUMN_NAME_DESC,
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
