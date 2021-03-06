package com.envived.android.features.description;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.envived.android.R;
import com.envived.android.features.description.CommentsActivity.Comment;
import com.envived.android.utils.Utils;

public class CommentsListAdapter extends BaseAdapter {
	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	
	private Context mContext;
	private List<Comment> mCommentList;
	private List<Comment> mActiveCommentList;
	private LayoutInflater mInflater;
	private List<String> filters;
	
	private Map<String, Integer> mCommentLayoutMap;
	
	public CommentsListAdapter(Context context, LinkedList<Comment> commentList) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mCommentList = commentList;
		mActiveCommentList = new LinkedList<Comment>();
		filters = new LinkedList<String>();
		
		mCommentLayoutMap = new HashMap<String, Integer>();
	}

	@Override
	public int getCount() {
		return mActiveCommentList.size();
	}

	@Override
	public Object getItem(int position) {
		return mActiveCommentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void addItem(Comment comment, boolean append) {
		if (append) {
			mCommentList.add(comment);
			if (checkFilter(comment)) {
				mActiveCommentList.add(comment);
			}
		} else {
			mCommentList.add(0, comment);
			if (checkFilter(comment)) {
				mActiveCommentList.add(0, comment);
			}
		}
		notifyDataSetChanged();
	}
	
	public void removeItem(int position) {
		mCommentList.remove(position);
		notifyDataSetChanged();
	}
	
	public void addAllItems(List<Comment> newComments, boolean append) {
		if (append) {
			mCommentList.addAll(newComments);
			if (checkFilter(newComments)) {
				mActiveCommentList.addAll(newComments);
			}
		}
		else {
			mCommentList.addAll(0, newComments);
			if (checkFilter(newComments)) {
				mActiveCommentList.addAll(0, newComments);
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.comment_row, parent, false);
			
			// Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
			holder = new ViewHolder();
			holder.wrapperLayout = (LinearLayout) convertView.findViewById(R.id.comment_row_wrapper);
			holder.author = (TextView)convertView.findViewById(R.id.comment_author);
			holder.date = (TextView)convertView.findViewById(R.id.comment_date);
			holder.text = (TextView)convertView.findViewById(R.id.comment_text);
			holder.subject = (TextView)convertView.findViewById(R.id.comment_subject);
			
			convertView.setTag(holder);
		}
		else {
			// Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
		}
		
		Resources r = mContext.getResources();
		int marginPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics());
		
		LinearLayout.LayoutParams marginLayoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, 
						LinearLayout.LayoutParams.WRAP_CONTENT);
		
		Comment currentComment = (Comment) getItem(position);
		int currentLayout = LEFT;
		
		if (position == 0) {
			currentLayout = LEFT;
		}
		else {
			// get previous item
			Comment prevComment = (Comment) getItem(position - 1);
			Integer prevLayout = mCommentLayoutMap.get(prevComment.getCommentUrl());
			if (prevLayout == null) {
				prevLayout = LEFT;
			}
			
			if (currentComment.getCommentOwnerUrl().equals(prevComment.getCommentOwnerUrl())) {
				currentLayout = prevLayout;
			}
			else {
				currentLayout = 1 - prevLayout;
			}
		}
		
		mCommentLayoutMap.put(currentComment.getCommentUrl(), currentLayout);
		
		if (currentLayout == LEFT) {
			// to the left
			marginLayoutParams.rightMargin = marginPx;
			marginLayoutParams.gravity = Gravity.LEFT;
			holder.wrapperLayout.setBackgroundDrawable((mContext.getResources().getDrawable(R.drawable.feature_program_presentation_comment_wrapper)));
		}
		else {
			// to the right
			marginLayoutParams.leftMargin = marginPx;
			marginLayoutParams.gravity = Gravity.RIGHT;
			holder.wrapperLayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.feature_program_presentation_comment_wrapper_gray));
		}
		
		holder.wrapperLayout.setLayoutParams(marginLayoutParams);
		
		bind(holder, position);
		
		return convertView;
	}
	
	public void addFilter(String filter) {
		filters.add(filter);
	}
	
	public void removeFilter(String filter) {
		filters.remove(filter);
	}
	
	private boolean checkFilter(List<Comment> comments) {
		if (filters.size() == 0) return true;
		
		for (Comment comment : comments) {
			for (String filter : filters) {
				if (filter.equals(comment.getCommentSubject())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean checkFilter(Comment comment) {
		if (filters.size() == 0) return true;
		
		for (String filter : filters) {
			if (filter.equals(comment.getCommentSubject())) {
				return true;
			}
		}
		return false;
	}
	
	private void filterData() {
		//Log.d("adapter", "filters:" + filters.toString());
		for (Comment comment : mCommentList) {
			//Log.d("adapter!", comment.toString());
			if (checkFilter(comment)) {
				mActiveCommentList.add(comment);
			}
		}
	}
	
	public void applyFilter() {
		mActiveCommentList.clear();
		filterData();
		notifyDataSetChanged();
	}
	
	private void bind(ViewHolder holder, int position) {
		
		Comment commentData = mActiveCommentList.get(position);
		
		holder.author.setText(commentData.getCommentOwner());
		holder.text.setText(commentData.getCommentContent());
		
		Calendar timestamp = commentData.getCommentTimestamp();
		String timestampString = Utils.calendarToString(timestamp, "dd MMM, HH:mm");
		holder.date.setText(timestampString);
		
		holder.subject.setText(commentData.getCommentSubject());
	}
	
	static class ViewHolder {
		LinearLayout wrapperLayout;
		
		TextView author;
		TextView date;
		TextView text;
		TextView subject;
	}
}
