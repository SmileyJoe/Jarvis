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

import com.smileyjoedev.jarvis.adapter.GridButtonAdapter;
import com.smileyjoedev.jarvis.adapter.GridCheckBoxAdapter;
import com.smileyjoedev.jarvis.debug.Debug;
import com.smileyjoedev.jarvis.debug.DebugItem;
import com.smileyjoedev.jarvis.interfaces.OnGridButtonItemClickListener;
import com.smileyjoedev.jarvis.interfaces.OnGridCheckBoxItemClickListener;
import com.smileyjoedev.jarvis.view.Button;
import com.smileyjoedev.jarvis.view.TextView;

public class Hud extends DialogFragment {

	private LinearLayout mWrapper;
	private ArrayList<Integer> mSelectedTypes;
	private ArrayList<String> mSelectedTags;

	private static final int TAG_SCROLL_LOG_WRAPPER = 1;
	private static final int TAG_FILTER_WRAPPER = 2;
	private static final int TAG_FILTER_BUTTON = 3;
	private static final int TAG_ACTION_WRAPPER = 4;
	private static final int TAG_ACTION_BUTTON = 5;
	
	private static final int ACTION_SAVE = 1;
	private static final int ACTION_EMAIL = 2;
	private static final int ACTION_CLEAR_LOG = 3;
	private static final int ACTION_COPY_DB = 4;
	private static final int ACTION_CLEAR_DB = 5;

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

		initActions();
		initFilter();
		initLog();

		getDialog().setTitle("Jarvis");

		return mWrapper;
	}
	
	private void initActions(){
		String[] titles = new String[] {"Save", "Email", "Clear Log", "Copy DB", "Clear DB"};
		int[] ids = new int[] {ACTION_SAVE, ACTION_EMAIL, ACTION_CLEAR_LOG, ACTION_COPY_DB, ACTION_CLEAR_DB};
		
		Button show = new Button(getActivity());
		LinearLayout wrapper = new LinearLayout(getActivity());
		GridView grid = new GridView(getActivity());
		
		GridButtonAdapter adapter = new GridButtonAdapter(titles, ids, new OnGridButtonItemClickListener() {
			
			@Override
			public void onItemClick(String title, int id) {
				switch(id){
					case Hud.ACTION_SAVE:
						saveToFile();
						Debug.d("Save to file");
						break;
					case Hud.ACTION_EMAIL:
						email();
						Debug.d("Email");
						break;
					case Hud.ACTION_CLEAR_LOG:
						clearLog();
						Debug.d("Clear Log");
						break;
					case Hud.ACTION_COPY_DB:
						copyDb();
						Debug.d("Copy DB");
						break;
					case Hud.ACTION_CLEAR_DB:
						clearDb();
						Debug.d("Clear DB");
						break;
				}
				
			}
		});

		wrapper.setOrientation(LinearLayout.VERTICAL);
		wrapper.setTag(Hud.TAG_ACTION_WRAPPER);
		wrapper.setVisibility(View.GONE);

		show.setText("Show Actions");
		show.setTag(Hud.TAG_ACTION_BUTTON);
		show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getView().findViewWithTag(Hud.TAG_ACTION_WRAPPER).getVisibility() == View.VISIBLE) {
					hideActions();
				} else {
					showActions();
				}

			}
		});

		grid.setNumColumns(2);
		grid.setAdapter(adapter);
		
		wrapper.addView(grid);

		mWrapper.addView(show);
		mWrapper.addView(wrapper);
	}
	
	private void saveToFile(){
		// TODO: save to file //
	}
	
	private void email(){
		// TODO: Email //
	}
	
	private void clearLog(){
		Debug.clearLog();
	}
	
	private void copyDb(){
		// TODO: Copy DB //
	}
	
	private void clearDb(){
		// TODO: Clear DB //
	}
	
	private void hideActions(){
		getActionButton().setText("Show Actions");
		getActionWrapper().setVisibility(View.GONE);
	}
	
	private void showActions(){
		getActionButton().setText("Hide Actions");
		getActionWrapper().setVisibility(View.VISIBLE);
	}

	private void initTypeFilter(ViewGroup wrapper) {
		String[] titles = new String[] { "All", "Verbose", "Information", "Debug", "Warning", "Error" };
		int[] ids = new int[] { Debug.ALL, Debug.VERBOSE, Debug.INFO, Debug.DEBUG, Debug.WARNING, Debug.ERROR };

		GridView grid = new GridView(getActivity());
		GridCheckBoxAdapter adapter = new GridCheckBoxAdapter(titles, ids, new OnGridCheckBoxItemClickListener() {

			@Override
			public void onItemClick(String title, int id, boolean isChecked) {
				if (isChecked) {
					mSelectedTypes.add(id);
				} else {
					mSelectedTypes.remove((Integer) id);
				}
				resetLog();

			}
		});

		adapter.setSelectedIds(mSelectedTypes);

		grid.setNumColumns(2);
		grid.setAdapter(adapter);

		wrapper.addView(titleView("Types"));
		wrapper.addView(grid);
	}

	private void initTagFilter(ViewGroup wrapper) {
		GridView grid = new GridView(getActivity());

		String[] titles = getTags();
		int[] ids = new int[titles.length];

		for (int i = 0; i < titles.length; i++) {
			ids[i] = i;
		}

		GridCheckBoxAdapter adapter = new GridCheckBoxAdapter(titles, ids, new OnGridCheckBoxItemClickListener() {

			@Override
			public void onItemClick(String title, int id, boolean isChecked) {
				if (isChecked) {
					mSelectedTags.add(title);
				} else {
					mSelectedTags.remove(title);
				}
				resetLog();

			}
		});

		adapter.setSelectedIds(null);

		grid.setNumColumns(2);
		grid.setAdapter(adapter);

		wrapper.addView(titleView("Tags"));
		wrapper.addView(grid);
	}

	private TextView titleView(String title) {
		TextView titleView = new TextView(getActivity());
		titleView.setText(title);
		titleView.setTypeface(null, Typeface.BOLD);
		return titleView;
	}

	private void initFilter() {

		Button showFilter = new Button(getActivity());
		LinearLayout filterWrapper = new LinearLayout(getActivity());

		filterWrapper.setOrientation(LinearLayout.VERTICAL);
		filterWrapper.setTag(Hud.TAG_FILTER_WRAPPER);
		filterWrapper.setVisibility(View.GONE);

		showFilter.setText("Show Filter");
		showFilter.setTag(Hud.TAG_FILTER_BUTTON);
		showFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getView().findViewWithTag(Hud.TAG_FILTER_WRAPPER).getVisibility() == View.VISIBLE) {
					hideFilter();
				} else {
					showFilter();
				}

			}
		});

		initTagFilter(filterWrapper);
		initTypeFilter(filterWrapper);

		mWrapper.addView(showFilter);
		mWrapper.addView(filterWrapper);

	}

	private String[] getTags() {
		ArrayList<String> tagsArray = new ArrayList<String>();

		for (int i = 0; i < Debug.getLogSize(); i++) {
			DebugItem item = Debug.getLogItem(i);

			if (!tagsArray.contains(item.getTag())) {
				tagsArray.add(item.getTag());
				mSelectedTags.add(item.getTag());
			}

		}

		String[] tags = new String[tagsArray.size()];

		for (int i = 0; i < tagsArray.size(); i++) {
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
		return (Button) getView().findViewWithTag(Hud.TAG_FILTER_BUTTON);
	}

	private LinearLayout getFilterWrapper() {
		return (LinearLayout) getView().findViewWithTag(Hud.TAG_FILTER_WRAPPER);
	}
	
	private Button getActionButton(){
		return (Button) getView().findViewWithTag(Hud.TAG_ACTION_BUTTON);
	}
	
	private LinearLayout getActionWrapper(){
		return (LinearLayout) getView().findViewWithTag(Hud.TAG_ACTION_WRAPPER);
	}

	private void resetLog() {
		LinearLayout scrollItem = (LinearLayout) getView().findViewWithTag(Hud.TAG_SCROLL_LOG_WRAPPER);

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
		scrollItem.setTag(Hud.TAG_SCROLL_LOG_WRAPPER);

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
