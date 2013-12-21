package com.github.knrajago.wordcards.constants;

public class WordCardConstants {
	public static final int ALL_TABLE = 1;
	public static final int ONE_ROW = 2;
	
	public static final String TABLE_NAME = "gre_words";
    public static final String WORD_COL = "word";
    public static final String MEANING_COL = "meaning";
    public static final String EXAMPLE_COL = "example";
    public static final String STATE_COL = "state";
    public static final String TXING_COL = "transactioning";
    
    public static final String PREF_NAME = "lastUpdDtTime";
    public static final String LAST_UPDATE_DATE_TIME_KEY = "lastUpdateTime";
    
	// Content provider authority
    public static final String AUTHORITY = "com.github.knrajago.flashcards.provider";
    public static final String CONTENT_PROVIDER_URI = "content://com.github.knrajago.wordcards.provider/GreWordsProvider";
    // Account type
    public static final String ACCOUNT_TYPE = "flashcards.knrajago.github.com";
    // Account
    public static final String ACCOUNT = "default_account";
    // Incoming Intent key for extended data
    public static final String KEY_SYNC_REQUEST =
            "com.github.knrajago.flashcards.KEY_SYNC_REQUEST";
    
    public static final String SPREADSHEETS_API_URL = "https://spreadsheets.google.com/feeds/spreadsheets/private/full";
    public static final String GRE_LIST_XLS_TITLE = "GREList";
}
