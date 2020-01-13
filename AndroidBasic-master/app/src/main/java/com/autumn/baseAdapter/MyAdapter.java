package com.autumn.baseAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.autumn.R;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    List<ItemBean> mlist;
    private  LayoutInflater mInflater;

    public MyAdapter(Context context, List<ItemBean> list){
        this.mlist = list;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //冗余式
//        View view = mInflater.inflate(R.layout.item,null);
//        ImageView imageView = view.findViewById(R.id.item_iv_image);
//        TextView titleView = view.findViewById(R.id.item_tv_title);
//        TextView contentView = view.findViewById(R.id.item_iv_content);
//
//        ItemBean itemBean = mlist.get(position);
//        imageView.setImageResource(itemBean.ItemImageResid);
//        titleView.setText(itemBean.ItemTitle);
//        contentView.setText(itemBean.ItemContent);
        //普通式
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.item,null);
        }

        //方便


        ImageView imageView = convertView.findViewById(R.id.item_iv_image);
        TextView titleView = convertView.findViewById(R.id.item_tv_title);
        TextView contentView = convertView.findViewById(R.id.item_iv_content);

        ItemBean itemBean = mlist.get(position);
        imageView.setImageResource(itemBean.ItemImageResid);
        titleView.setText(itemBean.ItemTitle);
        contentView.setText(itemBean.ItemContent);



        return convertView;
    }

    class ViewHolder{
        public ImageView imageView;
        public TextView title;
        public TextView content;
    }

}
