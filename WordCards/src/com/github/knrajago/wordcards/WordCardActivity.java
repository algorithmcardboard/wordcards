package com.github.knrajago.wordcards;

import static com.github.knrajago.wordcards.constants.WordCardConstants.ACCOUNT;
import static com.github.knrajago.wordcards.constants.WordCardConstants.ACCOUNT_TYPE;
import static com.github.knrajago.wordcards.constants.WordCardConstants.CONTENT_PROVIDER_URI;
import static com.github.knrajago.wordcards.constants.WordCardConstants.EXAMPLE_COL;
import static com.github.knrajago.wordcards.constants.WordCardConstants.MEANING_COL;
import static com.github.knrajago.wordcards.constants.WordCardConstants.TABLE_NAME;
import static com.github.knrajago.wordcards.constants.WordCardConstants.WORD_COL;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.github.knrajago.wordcards.cursoradapters.WordCardCursorAdapter;
import com.github.knrajago.wordcards.localdb.GreWordDBHelper;
import com.github.knrajago.wordcards.updators.LocalDBUpdatorService;



public class WordCardActivity extends Activity {

	private GreFlashCardsObserver mObserver = null;
	private WordCardCursorAdapter mAdapter = null;
	private SQLiteDatabase mDatabse = null;
	private Cursor mCursor = null;
	
	private String[] columns = new String[] {
    		BaseColumns._ID,
    		WORD_COL,
    		MEANING_COL,
    		EXAMPLE_COL
    };
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getFeeds();
        setContentView(R.layout.activity_word_card);
        
        final Button refreshBtn = (Button) findViewById(R.id.refresh_btn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intnt = new Intent(v.getContext(), LocalDBUpdatorService.class);
				v.getContext().startService(intnt);
			}
		});
        
        final ListView lst = (ListView) findViewById(R.id.word_lst_view);
        initAdapter();
        lst.setAdapter(this.mAdapter);
        
        this.mObserver = new GreFlashCardsObserver(new Handler());
        getContentResolver().registerContentObserver(Uri.parse(CONTENT_PROVIDER_URI), true, this.mObserver);
    }
    
    /**
     * Initialize Adapters
     */
    private void initAdapter() {
        mDatabse = new GreWordDBHelper(getApplicationContext()).getReadableDatabase();
        mCursor = mDatabse.query(TABLE_NAME, columns, null, null, null, null, null);
        
        mAdapter = new WordCardCursorAdapter(getApplicationContext(), this.mCursor, true);
    }
	

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return accountManager.getAccounts()[0];
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.word_card, menu);
        return true;
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getContentResolver().unregisterContentObserver(this.mObserver);
		if (this.mCursor != null && !this.mCursor.isClosed()) {
			this.mCursor.close();
		}
		
		if (this.mDatabse != null && this.mDatabse.isOpen()) {
			this.mDatabse.close();
		}
	}

	public class GreFlashCardsObserver extends ContentObserver {

		public GreFlashCardsObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			SQLiteDatabase db = new GreWordDBHelper(getApplicationContext()).getReadableDatabase();
	        Cursor newCursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
			mAdapter.changeCursor(newCursor);
		}
	}
}
