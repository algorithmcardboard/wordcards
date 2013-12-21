package com.github.knrajago.wordcards.provider;

import static com.github.knrajago.wordcards.constants.WordCardConstants.ALL_TABLE;
import static com.github.knrajago.wordcards.constants.WordCardConstants.EXAMPLE_COL;
import static com.github.knrajago.wordcards.constants.WordCardConstants.MEANING_COL;
import static com.github.knrajago.wordcards.constants.WordCardConstants.ONE_ROW;
import static com.github.knrajago.wordcards.constants.WordCardConstants.TABLE_NAME;
import static com.github.knrajago.wordcards.constants.WordCardConstants.WORD_COL;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.github.knrajago.wordcards.localdb.GreWordDBHelper;

public class GreWordProvider extends ContentProvider {

	// Creates a UriMatcher object.
    private static final UriMatcher sUriMatcher;


    static {
    	sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    	sUriMatcher.addURI("com.github.knrajago.wordcards.provider", "GreWordsProvider", ALL_TABLE);
    	sUriMatcher.addURI("com.github.knrajago.wordcards.provider", "GreWordsProvider/#", ONE_ROW);
    }
    
    private GreWordDBHelper mDBHelper;
    
    @Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
    	SQLiteDatabase db = mDBHelper.getWritableDatabase();
    	int deleteCount = db.delete(TABLE_NAME, selection, selectionArgs);
    	db.close();
    	return deleteCount;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri pUri, ContentValues pValues) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		long id = db.insertOrThrow(TABLE_NAME, null, pValues);
		db.close();
		return pUri;
	}

	@Override
	public boolean onCreate() {
		mDBHelper = new GreWordDBHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch(sUriMatcher.match(uri)) {
		case ALL_TABLE:
			SQLiteDatabase db = mDBHelper.getReadableDatabase();
			return db.query(TABLE_NAME,
					new String[] {BaseColumns._ID, WORD_COL, MEANING_COL, EXAMPLE_COL},
					null, null, null, null, null);
		default:
			
		}
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}
}
