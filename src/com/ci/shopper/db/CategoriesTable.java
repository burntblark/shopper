package com.ci.shopper.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class CategoriesTable implements BaseColumns
{
	public static final String TABLE_NAME = "categories";

	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESC = "description";
	

	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES =
	"CREATE TABLE " + TABLE_NAME + " (" +
	_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
	COLUMN_DESC + TEXT_TYPE +
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
}
