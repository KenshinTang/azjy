package com.yunlinker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunlinker.azjy.R;

import java.util.List;

/**
 * Created by Administrator on 2018/11/21/021.
 */

public class AgeAdapder extends BaseAdapter{
    private Context mContext;
    private List<String> screenlist;

    public  AgeAdapder (Context context, List<String> screenlist){
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

        return convertView;
    }

    class ViewHolder{
        TextView textView;
    }
}
