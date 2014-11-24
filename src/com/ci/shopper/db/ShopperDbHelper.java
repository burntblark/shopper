package com.ci.shopper.db;
import android.database.sqlite.*;
import android.content.*;
import android.util.*;


public class ShopperDbHelper extends SQLiteOpenHelper
{
	// If you change the database schema, you must increment the database version.
	public static final int DATABASE_VERSION = 7;
	public static final String DATABASE_NAME = "Shopper.db";

	public ShopperDbHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void onCreate(SQLiteDatabase db)
	{
		Log.w("ShopperLog", "Creating Categories table");
		CategoriesTable.onCreate(db);
		Log.w("ShopperLog", "Creating Items table");
		ItemsTable.onCreate(db);
		Log.w("ShopperLog", "Creating Expenses table");
		ExpensesTable.onCreate(db);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// This database is only a cache for online data, so its upgrade policy is
		// to simply to discard the data and start over
		//db.execSQL(SQL_DELETE_ENTRIES);
		
		CategoriesTable.onUpgrade(db, oldVersion, newVersion);
		ItemsTable.onUpgrade(db, oldVersion, newVersion);
		ExpensesTable.onUpgrade(db, oldVersion, newVersion);
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		onUpgrade(db, oldVersion, newVersion);
	}
}

