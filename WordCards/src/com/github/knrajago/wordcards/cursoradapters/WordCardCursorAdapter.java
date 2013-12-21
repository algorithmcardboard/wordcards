package com.github.knrajago.wordcards.cursoradapters;

import static com.github.knrajago.wordcards.constants.WordCardConstants.EXAMPLE_COL;
import static com.github.knrajago.wordcards.constants.WordCardConstants.MEANING_COL;
import static com.github.knrajago.wordcards.constants.WordCardConstants.WORD_COL;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.github.knrajago.wordcards.R;

public class WordCardCursorAdapter extends CursorAdapter {

	LayoutInflater mLayoutInflater = null;
	public WordCardCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
        mLayoutInflater = LayoutInflater.from(context);
	}

	public WordCardCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}
	
	@Override
	public void bindView(View pView, Context pContext, Cursor pCursor) {
		Log.i("Soundmail", "bindView() entry");
		updateView(pView, pCursor);
		Log.i("Soundmail", "bindView() exit");
	}

	@Override
	public View newView(Context pContext, Cursor pCursor, ViewGroup pParent) {
		View v = mLayoutInflater.inflate(R.layout.single_word_layout, pParent, false);
		updateView(v, pCursor);
        return v;
	}
	
	private void updateView(View pView, Cursor pCursor) {
		TextView wordListText = (TextView) pView.findViewById(R.id.word_txt);
		TextView meaningText = (TextView) pView.findViewById(R.id.meaning_txt);
		TextView exampleText = (TextView) pView.findViewById(R.id.example_txt);
		
		wordListText.setText(pCursor.getString(pCursor.getColumnIndex(WORD_COL)));
		meaningText.setText(pCursor.getString(pCursor.getColumnIndex(MEANING_COL)));
		exampleText.setText(pCursor.getString(pCursor.getColumnIndex(EXAMPLE_COL)));
		
	}
}
