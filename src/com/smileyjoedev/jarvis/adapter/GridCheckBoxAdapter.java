package com.smileyjoedev.jarvis.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.smileyjoedev.jarvis.interfaces.OnGridCheckBoxItemClickListener;
import com.smileyjoedev.jarvis.view.CheckBox;


public class GridCheckBoxAdapter extends BaseAdapter {

	private String[] mTitles;
	private int[] mIds;
	private OnGridCheckBoxItemClickListener mOnClickListener;
	private ArrayList<Integer> mSelectedIds;
	
	public GridCheckBoxAdapter(String[] titles, int[] ids, OnGridCheckBoxItemClickListener onClickListener) {
		mTitles = titles;
		mIds = ids;
		mOnClickListener = onClickListener;
		mSelectedIds = new ArrayList<Integer>();
	}
	
	public void setSelectedIds(ArrayList<Integer> selectedIds){
		mSelectedIds = selectedIds;
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
	
	public int getId(int position){
		return mIds[position];
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		CheckBox check = new CheckBox(parent.getContext());
		
		check.setText(getItem(position));
		
		if(mSelectedIds == null || mSelectedIds.contains(getId(position))){
			check.setChecked(true);
		} else {
			check.setChecked(false);
		}
		
		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mOnClickListener.onItemClick(getItem(position), getId(position), isChecked);
			}
		});
		
		return check;
	}

	
	
}
