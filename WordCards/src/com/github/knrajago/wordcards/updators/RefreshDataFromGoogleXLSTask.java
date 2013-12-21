package com.github.knrajago.wordcards.updators;

import static com.github.knrajago.wordcards.constants.WordCardConstants.CONTENT_PROVIDER_URI;
import static com.github.knrajago.wordcards.constants.WordCardConstants.EXAMPLE_COL;
import static com.github.knrajago.wordcards.constants.WordCardConstants.GRE_LIST_XLS_TITLE;
import static com.github.knrajago.wordcards.constants.WordCardConstants.LAST_UPDATE_DATE_TIME_KEY;
import static com.github.knrajago.wordcards.constants.WordCardConstants.MEANING_COL;
import static com.github.knrajago.wordcards.constants.WordCardProtectedConstants.PASSWORD;
import static com.github.knrajago.wordcards.constants.WordCardConstants.PREF_NAME;
import static com.github.knrajago.wordcards.constants.WordCardConstants.SPREADSHEETS_API_URL;
import static com.github.knrajago.wordcards.constants.WordCardProtectedConstants.USER_NAME;
import static com.github.knrajago.wordcards.constants.WordCardConstants.WORD_COL;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class RefreshDataFromGoogleXLSTask extends AsyncTask<Void, Void, Void> {

	
	private Context mContext = null;
	
	/**
	 * Default constructor.
	 */
	public RefreshDataFromGoogleXLSTask(Context pContext) {
		mContext = pContext;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		SpreadsheetService service = getSpreadsheetService();
		ContentResolver resolver = this.mContext.getContentResolver();
		Uri uri = Uri.parse(CONTENT_PROVIDER_URI);
		resolver.delete(uri, "1", null);
		
		try {
			service.setUserCredentials(USER_NAME, PASSWORD);
			URL spreadSheetUrl = new URL(SPREADSHEETS_API_URL);
			SpreadsheetQuery query = new SpreadsheetQuery(spreadSheetUrl);
			query.setTitleQuery(GRE_LIST_XLS_TITLE);
			query.setTitleExact(true);
			SpreadsheetFeed spreadSheetFeed = service.getFeed(query, SpreadsheetFeed.class);
			DateTime updDtTm = spreadSheetFeed.getUpdated();
			long updDtTmLong = updDtTm.getValue();
			long storedLstEditedDtTm = getLastUpdateTime();
			
			if (storedLstEditedDtTm < updDtTmLong) {
				setLastUpdateTime(updDtTmLong);
				List<SpreadsheetEntry> entryList = spreadSheetFeed.getEntries();
				if (entryList == null) {
					//
				} else {
					
					for (SpreadsheetEntry oneSheetEntry : entryList) {
						List<WorksheetEntry> wrkSheetList = oneSheetEntry.getWorksheets();
						WorksheetEntry wrkShtEntry = wrkSheetList.get(0);
						URL cellFeedUrl= wrkShtEntry.getCellFeedUrl();
						CellFeed celFeed = service.getFeed (cellFeedUrl, CellFeed.class);
						List<CellEntry> cellEntryList = celFeed.getEntries();
						String word = null;
						String meaning = null;
						String examples = null;
						int iDx = 0;
						for (CellEntry oneCell : cellEntryList) {
							switch(iDx % 3) {
							case 0:
								word = oneCell.getCell().getValue();
								iDx = 0;
								break;
							case 1:
								meaning = oneCell.getCell().getValue();
								break;
							case 2:
								examples = oneCell.getCell().getValue();
								break;
							}
							
							if (iDx > 1) {
								ContentValues values = new ContentValues();
								values.put(WORD_COL, word);
								values.put(MEANING_COL, meaning);
								values.put(EXAMPLE_COL, examples);
								Log.i("Soundmail", "Trying to insert - " + word + ", " + meaning + ", " + examples);
								resolver.insert(uri, values);
							}
							iDx++;
						}
					}
					
				}
			} else {
				Log.i("Soundmail", "The last update time stored is the latest.  So, no need to update");
			}
		} catch (AuthenticationException e) {
			Log.e("Soundmail", "Refresh Data from Google XLS - AuthenticationException", e);
		} catch (MalformedURLException e) {
			Log.e("Soundmail", "Refresh Data from Google XLS - MalformedURLException", e);
		} catch (IOException e) {
			Log.e("Soundmail", "Refresh Data from Google XLS - IOException", e);
		} catch (ServiceException e) {
			Log.e("Soundmail", "Refresh Data from Google XLS - ServiceException", e);
		}
		resolver.notifyChange(uri, null);
		return null;
	}
	
	
	private SpreadsheetService getSpreadsheetService () {
		Log.i("Soundmail", "Before");
		final SpreadsheetService service = new SpreadsheetService("MySpreadsheetIntegration");
		service.setProtocolVersion(SpreadsheetService.Versions.V3);
		return service;
	}
	
	private void setLastUpdateTime(long pLastUpdateTime) {
		SharedPreferences lastUpdPrefrence = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		Editor edit = lastUpdPrefrence.edit();
		edit.clear();
		edit.putLong(LAST_UPDATE_DATE_TIME_KEY, pLastUpdateTime);
		edit.commit();
	}
	
	private long getLastUpdateTime() {
		SharedPreferences lastUpdPrefrence = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return lastUpdPrefrence.getLong(LAST_UPDATE_DATE_TIME_KEY, -1);
	}
}
