package com.o2o_jiangchen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.o2o_jiangchen.model.Grid0model;
import com.yunpeng_chuankou.R;

import java.util.ArrayList;
import java.util.List;

public class Grid3Adapter extends BaseAdapter {
	private Context context;
	private List<Grid0model> model0List = new ArrayList<Grid0model>();
	public Grid3Adapter(Context context, List<Grid0model> model0List) {
		this.context = context;
		this.model0List = model0List;
	}

	@Override
	public int getCount() {
		return model0List.size();
	}

	@Override
	public Object getItem(int position) {
		return model0List.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.new_item_gridview, parent, false);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.img.setBackgroundResource(model0List.get(position).getImg());
		viewHolder.title.setText(model0List.get(position).getName());
		return convertView;
	}

	private class ViewHolder {
		private TextView title;
		private ImageView img;
	}

}
