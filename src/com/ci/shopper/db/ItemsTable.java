package com.ci.shopper.db;

import android.provider.*;
import android.database.sqlite.*;
import android.content.*;
import android.database.*;
import com.ci.shopper.*;
import java.util.*;
import android.util.*;

public class ItemsTable implements BaseColumns
{
	public static final String TABLE_NAME = "items";

	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_CATEGORY_ID = "category_id";
	public static final String COLUMN_DESC = "description";


	private static final String SQL_CREATE_ENTRIES =
	"CREATE TABLE " + TABLE_NAME + " (" +
	_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	COLUMN_NAME + " TEXT, " +
	COLUMN_CATEGORY_ID + " INTEGER DEFAULT 0, " +
	COLUMN_DESC + " TEXT, " +
	"FOREIGN KEY("+COLUMN_CATEGORY_ID+") REFERENCES "+CategoriesTable.TABLE_NAME+"("+CategoriesTable._ID+") "+
	"ON UPDATE CASCADE ON DELETE SET DEFAULT);";

	private static final String SQL_DELETE_ENTRIES =
	"DROP TABLE IF EXISTS " + TABLE_NAME;

	public static void onCreate(SQLiteDatabase database) {
		Log.w("ShopperLog", SQL_CREATE_ENTRIES);
try{
		database.execSQL(SQL_CREATE_ENTRIES);
		}catch(Exception ex){
			Log.w("ShopperLog", ex.getMessage());
		}
		
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
								 int newVersion) {
		Log.w(CategoriesTable.class.getName(), "Upgrading database from version "
			  + oldVersion + " to " + newVersion
			  + ", which will destroy all old data");
		database.execSQL(SQL_DELETE_ENTRIES);
		onCreate(database);
	}
}
