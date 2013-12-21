package com.github.knrajago.wordcards.updators;


import android.app.IntentService;
import android.content.Intent;

public class LocalDBUpdatorService extends IntentService {

	public LocalDBUpdatorService() {
		super("LocalDBUpdatorService");
	}
	
	public LocalDBUpdatorService(String pMsg) {
		super(pMsg);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		RefreshDataFromGoogleXLSTask task = new RefreshDataFromGoogleXLSTask(getApplicationContext());
		task.execute();
	}
}
