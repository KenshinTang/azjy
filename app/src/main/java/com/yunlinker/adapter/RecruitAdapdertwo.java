package com.yunlinker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunlinker.azjy.R;

import java.util.List;

/**
 * Created by Administrator on 2018/11/21/021.
 */

public class RecruitAdapdertwo extends BaseAdapter {
    private Context mContext;
    private List<String> screenlist;
    private int  selectItem=-1;
    public  RecruitAdapdertwo (Context context, List<String> screenlist){
        this.mContext = context;
        this.screenlist = screenlist;
    }

    @Override
    public int getCount() {
        return screenlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_screenitem,null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView)convertView.findViewById(R.id.screen_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.textView.setText(screenlist.get(position));
        if(position==selectItem){
            viewHolder.textView.setTextColor(Color.parseColor("#68AF47"));
        }else {
            viewHolder.textView.setTextColor(Color.parseColor("#333333"));
        }
        return convertView;
    }

    class ViewHolder{
        TextView textView;
    }
    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }
}
