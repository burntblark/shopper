package com.ci.shopper.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class ExpensesTable implements BaseColumns{

	public static final String TABLE_NAME = "expenses";

	public static final String COLUMN_ITEM_ID = "item_id";
	public static final String COLUMN_ITEM_QTY = "item_qty";
	public static final String COLUMN_EXP_DATE = "expense_at";

	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_ITEM_ID + " INTEGER, " 
			+ COLUMN_ITEM_QTY + " REAL DEFAULT 0.00, " 
			+ COLUMN_EXP_DATE + " TEXT, " 
			+ "FOREIGN KEY(" + COLUMN_ITEM_ID + ") "
			+ "REFERENCES " + ItemsTable.TABLE_NAME + "(" + ItemsTable._ID + ") "
			+ "ON UPDATE CASCADE ON DELETE CASCADE);";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	public static void onCreate(SQLiteDatabase database) {
		Log.i("ShopperLog", "Querying: " + SQL_CREATE_ENTRIES);
		try {
			database.execSQL(SQL_CREATE_ENTRIES);
		} catch (Exception ex) {
			Log.e("ShopperLog",  "Querying Error: " + ex.getMessage());
		}

	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.i(ExpensesTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL(SQL_DELETE_ENTRIES);
		
		onCreate(database);
	}
}
