package com.smileyjoedev.jarvis;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.smileyjoedev.jarvis.adapter.GridCheckBoxAdapter;
import com.smileyjoedev.jarvis.debug.Debug;
import com.smileyjoedev.jarvis.debug.DebugItem;
import com.smileyjoedev.jarvis.interfaces.OnGridCheckBoxItemClickListener;
import com.smileyjoedev.jarvis.view.Button;
import com.smileyjoedev.jarvis.view.TextView;

public class HUD extends DialogFragment {

	private LinearLayout mWrapper;
	private ArrayList<Integer> mSelectedTypes;
	private ArrayList<String> mSelectedTags;

	private static int TAG_SCROLL_LOG_WRAPPER = 1;
	private static int TAG_FILTER_WRAPPER = 2;
	private static int TAG_FILTER_BUTTON = 3;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mSelectedTypes = new ArrayList<Integer>();
		mSelectedTypes.add(Debug.ALL);
		mSelectedTags = new ArrayList<String>();
		initWrapper();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		initFilter();
		initLog();
		
		getDialog().setTitle("Jarvis");
		
		return mWrapper;
	}
	
	private void initFilter() {
		String[] types = new String[] { "All", "Verbose", "Information", "Debug", "Warning", "Error" };
		int[] ids = new int[] { Debug.ALL, Debug.VERBOSE, Debug.INFO, Debug.DEBUG, Debug.WARNING, Debug.ERROR };

		Button showFilter = new Button(getActivity());
		GridView gridType = new GridView(getActivity());
		GridCheckBoxAdapter adapter = new GridCheckBoxAdapter(types, ids, new OnGridCheckBoxItemClickListener() {
			
			@Override
			public void onItemClick(String title, int id, boolean isChecked) {
				if(isChecked){
					mSelectedTypes.add(id);
				} else {
					mSelectedTypes.remove((Integer) id);
				}
				resetLog();
				
			}
		});
		
		GridView gridTag = new GridView(getActivity());
		
		String[] tags = getTags();
		int[] tagIds = new int[tags.length];
		
		for(int i = 0; i < tags.length; i++){
			tagIds[i] = i;
		}
		
		GridCheckBoxAdapter adapterTag = new GridCheckBoxAdapter(tags, tagIds, new OnGridCheckBoxItemClickListener() {
			
			@Override
			public void onItemClick(String title, int id, boolean isChecked) {
				if(isChecked){
					mSelectedTags.add(title);
				} else {
					mSelectedTags.remove(title);
				}
				resetLog();
				
			}
		});
		
		adapterTag.setSelectedIds(null);
		
		gridTag.setAdapter(adapterTag);
		
		adapter.setSelectedIds(mSelectedTypes);
		
		LinearLayout filterWrapper = new LinearLayout(getActivity());
		
		filterWrapper.setOrientation(LinearLayout.VERTICAL);
		
		TextView typeTitle = new TextView(getActivity());
		TextView tagTitle = new TextView(getActivity());
		
		typeTitle.setText("Type");
		tagTitle.setText("Tag");
		
		typeTitle.setTypeface(null, Typeface.BOLD);
		tagTitle.setTypeface(null, Typeface.BOLD);

		filterWrapper.setTag(HUD.TAG_FILTER_WRAPPER);

		showFilter.setText("Show Filter");

		showFilter.setTag(HUD.TAG_FILTER_BUTTON);

		filterWrapper.setVisibility(View.GONE);

		showFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getView().findViewWithTag(HUD.TAG_FILTER_WRAPPER).getVisibility() == View.VISIBLE) {
					hideFilter();
				} else {
					showFilter();
				}

			}
		});

		gridType.setNumColumns(2);

		gridType.setAdapter(adapter);

		filterWrapper.addView(typeTitle);
		filterWrapper.addView(gridType);
		filterWrapper.addView(tagTitle);
		filterWrapper.addView(gridTag);

		mWrapper.addView(showFilter);
		mWrapper.addView(filterWrapper);

	}
	
	private String[] getTags(){
		ArrayList<String> tagsArray = new ArrayList<String>();
		
		for(int i = 0; i < Debug.getLogSize(); i++){
			DebugItem item = Debug.getLogItem(i);
			
			if(!tagsArray.contains(item.getTag())){
				tagsArray.add(item.getTag());
				mSelectedTags.add(item.getTag());
			}
			
		}
		
		String[] tags = new String[tagsArray.size()];
		
		for(int i = 0; i < tagsArray.size(); i++){
			tags[i] = tagsArray.get(i);
		}
		
		return tags;
	}

	private void showFilter() {
		getFilterButton().setText("Hide Filter");
		getFilterWrapper().setVisibility(View.VISIBLE);
	}

	private void hideFilter() {
		getFilterButton().setText("Show Filter");
		getFilterWrapper().setVisibility(View.GONE);
	}

	private Button getFilterButton() {
		return (Button) getView().findViewWithTag(HUD.TAG_FILTER_BUTTON);
	}

	private LinearLayout getFilterWrapper() {
		return (LinearLayout) getView().findViewWithTag(HUD.TAG_FILTER_WRAPPER);
	}

	private void resetLog() {
		LinearLayout scrollItem = (LinearLayout) getView().findViewWithTag(HUD.TAG_SCROLL_LOG_WRAPPER);

		scrollItem.removeAllViews();

		for (int i = 0; i < Debug.getLogSize(); i++) {
			DebugItem item = Debug.getLogItem(i);

			if ((mSelectedTypes.contains(Debug.ALL) || mSelectedTypes.contains(item.getType())) && mSelectedTags.contains(item.getTag())) {
				scrollItem.addView(getLogItemView(Debug.getLogItem(i)));
			}

		}

		scrollItem.invalidate();
	}

	private void initLog() {
		ScrollView scrollLogWrapper = new ScrollView(getActivity());
		LinearLayout scrollItem = new LinearLayout(getActivity());
		scrollItem.setTag(HUD.TAG_SCROLL_LOG_WRAPPER);

		scrollItem.setOrientation(LinearLayout.VERTICAL);

		for (int i = 0; i < Debug.getLogSize(); i++) {
			scrollItem.addView(getLogItemView(Debug.getLogItem(i)));
		}

		scrollLogWrapper.addView(scrollItem);

		mWrapper.addView(scrollLogWrapper);
	}

	private TextView getLogItemView(DebugItem item) {
		TextView view = new TextView(getActivity());
		int color = Color.BLACK;

		switch (item.getType()) {
			case Debug.DEBUG:
				color = Color.BLUE;
				break;
			case Debug.ERROR:
				color = Color.RED;
				break;
			case Debug.INFO:
				color = Color.GREEN;
				break;
			case Debug.VERBOSE:
				color = Color.BLACK;
				break;
			case Debug.WARNING:
				color = Color.YELLOW;
				break;
		}

		view.setText(item.getMessage());
		view.setTextColor(color);
		view.setPadding(0, 0, 0, 20);

		return view;
	}

	private void initWrapper() {
		mWrapper = new LinearLayout(getActivity());
		mWrapper.setOrientation(LinearLayout.VERTICAL);
		mWrapper.setPadding(10, 10, 10, 10);
	}

}
