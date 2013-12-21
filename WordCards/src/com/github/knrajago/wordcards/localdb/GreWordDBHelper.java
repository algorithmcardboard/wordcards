package com.github.knrajago.wordcards.localdb;

import static com.github.knrajago.wordcards.constants.WordCardConstants.EXAMPLE_COL;
import static com.github.knrajago.wordcards.constants.WordCardConstants.MEANING_COL;
import static com.github.knrajago.wordcards.constants.WordCardConstants.STATE_COL;
import static com.github.knrajago.wordcards.constants.WordCardConstants.TABLE_NAME;
import static com.github.knrajago.wordcards.constants.WordCardConstants.TXING_COL;
import static com.github.knrajago.wordcards.constants.WordCardConstants.WORD_COL;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class GreWordDBHelper extends SQLiteOpenHelper {

	private static final String TEXT_TYPE = " TEXT";
	//private static final String INT_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "grewords.db";
	private static final String SQL_CREATE_ENTRIES =
	    "CREATE TABLE " + TABLE_NAME + " (" +
	    		BaseColumns._ID + " INTEGER PRIMARY KEY," +
	    		MEANING_COL + TEXT_TYPE + COMMA_SEP +
			    WORD_COL + TEXT_TYPE + COMMA_SEP +
			    EXAMPLE_COL + TEXT_TYPE + COMMA_SEP + 
			    STATE_COL + TEXT_TYPE + COMMA_SEP +
			    TXING_COL + TEXT_TYPE + COMMA_SEP +
			    " UNIQUE(" + WORD_COL + "))";
	
	public static final String SQL_DELETE_ENTRIES =
		    "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	public GreWordDBHelper(Context pContext) {
		super(pContext, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public GreWordDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) 
	{
		super(context, name, factory, version, errorHandler);
	}
	
	public GreWordDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		db.execSQL(SQL_CREATE_ENTRIES);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		db.execSQL(SQL_CREATE_ENTRIES);
	}

}
