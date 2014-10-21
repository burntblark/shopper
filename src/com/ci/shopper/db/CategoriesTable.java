package com.ci.shopper.db;

import android.provider.*;
import android.database.sqlite.*;
import android.content.*;
import android.database.*;
import com.ci.shopper.*;
import java.util.*;
import android.util.*;

public class CategoriesTable implements BaseColumns
{
	public static final String TABLE_NAME = "categories";

	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_NAME_DESC = "description";
	

	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES =
	"CREATE TABLE " + TABLE_NAME + " (" +
	_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
	COLUMN_NAME_DESC + TEXT_TYPE +
	");";

	private static final String SQL_DELETE_ENTRIES =
	"DROP TABLE IF EXISTS " + TABLE_NAME;

	public static void onCreate(SQLiteDatabase database) {
		Log.w(CategoriesTable.class.getName(), SQL_CREATE_ENTRIES);
			  
		database.execSQL(SQL_CREATE_ENTRIES);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
								 int newVersion) {
		Log.w(CategoriesTable.class.getName(), "Upgrading database from version "
			  + oldVersion + " to " + newVersion
			  + ", which will destroy all old data");
		database.execSQL(SQL_DELETE_ENTRIES);
		onCreate(database);
	}
	
	public static ArrayList <HashMap<String,String>> getCategories()
	{

		// get shopping categories from db
		//SQLiteOpenHelper dbHelper = new ShopperDbHelper(MainActivity.getContext());
		/*SQLiteDatabase db = dbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
    		CategoryEntry._ID,
    		CategoryEntry.COLUMN_NAME_NAME,
    		CategoryEntry.COLUMN_NAME_DESC,
		};

// How you want the results sorted in the resulting Cursor
		String sortOrder =
			CategoryEntry.COLUMN_NAME_NAME + " ASC";

		Cursor cursor = db.query(
			CategoryEntry.TABLE_NAME,  // The table to query
			projection,                               // The columns to return
			null,                                // The columns for the WHERE clause
			null,                            // The values for the WHERE clause
			null,                                     // don't group the rows
			null,                                     // don't filter by row groups
			sortOrder                                 // The sort order
		);

		cursor.moveToFirst();
		ArrayList <HashMap<String,String>> categories = new ArrayList<HashMap<String,String>>();
		while (cursor.isAfterLast() == false) 
		{
			HashMap<String,String> category = new HashMap<String,String>();

			category.put("id", cursor.getString(0));
			category.put("name", cursor.getString(1));
			category.put("description", cursor.getString(2));

			categories.add(category);
			cursor.moveToNext();
		}*/

		return null;
	}

	public static void addCategory(Long id, String name, String description)
	{
		//SQLiteOpenHelper mDbHelper =  new ShopperDbHelper(MainActivity.getContext());
		// Gets the data repository in write mode
		//SQLiteDatabase db = mDbHelper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(_ID, id);
		values.put(COLUMN_NAME_NAME, name);
		values.put(COLUMN_NAME_DESC, description);

		// Insert the new row, returning the primary key value of the new row
		//long newRowId = db.insert(TABLE_NAME,null,values);
	}
}
