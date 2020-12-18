package com.o2o_jiangchen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.o2o_jiangchen.model.MarkModel;
import com.o2o_jiangchen.my_interface.SwitchSelectInterface;
import com.yunpeng_chuankou.R;

import java.util.List;


/**
 * Created by Administrator on 2020/5/12.
 */

public class FloorGridAdapter extends BaseAdapter {
    private Context context;
    private List<MarkModel> modelList;
    private SwitchSelectInterface switchSelectInterface;

    public FloorGridAdapter(Context context, List<MarkModel> modelList,SwitchSelectInterface switchSelectInterface) {
        this.context = context;
        this.modelList = modelList;
        this.switchSelectInterface = switchSelectInterface;
    }


    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FloorGridAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new FloorGridAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_gridview_floor, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.ll = convertView.findViewById(R.id.ll);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FloorGridAdapter.ViewHolder) convertView.getTag();
        }
        if (modelList.get(position).isSelect()){
            viewHolder.ll.setBackground(context.getResources().getDrawable(R.drawable.bg_corner_yellow_blue));
//            viewHolder.rl.setBackgroundColor(context.getResources().getColor(R.color.bg_activity_gray));
        }else{
//            viewHolder.rl.setBackgroundColor(context.getResources().getColor(R.color.white));
            viewHolder.ll.setBackground(context.getResources().getDrawable(R.drawable.bg_corner_yellow_deep));
        }
        viewHolder.title.setText(modelList.get(position).marker_name);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchSelectInterface.select(position);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView title;
        private LinearLayout ll;
    }
}