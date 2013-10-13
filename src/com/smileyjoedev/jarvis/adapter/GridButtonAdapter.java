package com.smileyjoedev.jarvis.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.smileyjoedev.jarvis.interfaces.OnGridButtonItemClickListener;
import com.smileyjoedev.jarvis.view.Button;


public class GridButtonAdapter extends BaseAdapter {

	// the titles to be displayed //
	private String[] mTitles;
	// the ids, this is just used to return what has been clicked //
	private int[] mIds;
	// the onClick listener //
	private OnGridButtonItemClickListener mOnClickListener;

	public GridButtonAdapter(String[] titles, int[] ids, OnGridButtonItemClickListener onClickListener) {
		mTitles = titles;
		mIds = ids;
		mOnClickListener = onClickListener;
	}

	@Override
	public int getCount() {
		return mTitles.length;
	}

	@Override
	public String getItem(int arg0) {
		return mTitles[arg0];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getId(int position) {
		return mIds[position];
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Button button = new Button(parent.getContext());
		
		button.setText(getItem(position));
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mOnClickListener.onItemClick(getItem(position), getId(position));
				
			}
		});
		
		return button;
	}
}