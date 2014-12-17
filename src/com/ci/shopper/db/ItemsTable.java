package com.ci.shopper.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class ItemsTable implements BaseColumns
{
	public static final String TABLE_NAME = "items";

	public static final String FIELD_NAME = "name";
	public static final String FIELD_CATEGORY_ID = "category_id";
	public static final String FIELD_BARCODE = "barcode";
	public static final String FIELD_CATEGORY = "category";
	
	public static final String COLUMN_ID = TABLE_NAME+"._id";
	public static final String COLUMN_NAME = TABLE_NAME+".name";
	public static final String COLUMN_CATEGORY_ID = CategoriesTable.TABLE_NAME+".category_id";
	public static final String COLUMN_BARCODE = TABLE_NAME+".barcode";
	public static final String COLUMN_CATEGORY = CategoriesTable.TABLE_NAME+".name As category";
	
	private static final String SQL_CREATE_ENTRIES =
	"CREATE TABLE " + TABLE_NAME + " (" +
	_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	FIELD_NAME + " TEXT, " +
	FIELD_CATEGORY_ID + " INTEGER DEFAULT 0, " +
	FIELD_BARCODE + " TEXT, " +
	"FOREIGN KEY("+FIELD_CATEGORY_ID+") REFERENCES "+CategoriesTable.TABLE_NAME+"("+CategoriesTable._ID+") "+
	"ON UPDATE CASCADE ON DELETE SET DEFAULT);";

	private static final String SQL_DELETE_ENTRIES =
	"DROP TABLE IF EXISTS " + TABLE_NAME;

	public static void onCreate(SQLiteDatabase database) {
		Log.i("ShopperLog", SQL_CREATE_ENTRIES);
try{
		database.execSQL(SQL_CREATE_ENTRIES);
		}catch(Exception ex){
			Log.i("ShopperLog", ex.getMessage());
		}
		
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
								 int newVersion) {
		Log.i(CategoriesTable.class.getName(), "Upgrading database from version "
			  + oldVersion + " to " + newVersion
			  + ", which will destroy all old data");
		database.execSQL(SQL_DELETE_ENTRIES);
		onCreate(database);
	}
}
