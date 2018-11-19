package com.example.c_changing.main;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<item> mList;//数据源
    private LayoutInflater mInflater;//布局装载器对象
    private item bean;
    private ImageView img;
    private CanvasView canvasView;
    private int clickTemp = -1;
    private int lastPosition;

    public MyAdapter(Context context, List<item> list) {
        mList = list;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSeclection(int position) {
        clickTemp = position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item,null);
        bean = mList.get(position);
        //canvasView = view.findViewById(R.id.view);
        //img = view.findViewById(R.id.imageView);
        if (clickTemp == position) {
            if(bean.isclicked == 1) {
                view.setBackgroundResource(R.drawable.changed2);
            }
            else{
                view.setBackgroundResource(R.drawable.tu);
            }
        } else {
            bean = mList.get(position);
            bean.isclicked = 0;
            //canvasView = view.findViewById(R.id.view);
            //img = view.findViewById(R.id.imageView);
            //img.setImageResource(bean.Img);
            view.setBackgroundResource(R.drawable.tu);
        }
        return view;
    }
}
