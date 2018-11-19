package com.example.c_changing.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    private GridView gridView;
    private List<item> dataList;
    private MyAdapter adapter;
    private TextView textView;

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gridview);
        textView = findViewById(R.id.textView);
        //初始化数据
        initData();

        String[] from={"img","text"};

        //int[] to={R.id.img,R.id.text};

        //adapter=new SimpleAdapter(this, dataList, R.layout.item, from, to);

        gridView.setAdapter(adapter = new MyAdapter(this, dataList));
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return MotionEvent.ACTION_MOVE == event.getAction() ? true
                        : false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            //当某个元素被点击时调用该方法

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                adapter.setSeclection(arg2);
                if(dataList.get(arg2).isclicked == 0) {
                    dataList.get(arg2).isclicked = 1;
                    textView.setText(" Select: Track"+(arg2/4+1)+", "+(arg2%4+1));
                }
                else {
                    dataList.get(arg2).isclicked = 0;
                    textView.setText(" Select:");
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    void initData() {
        //图
        //图标下的文字
        dataList = new ArrayList<item>();
        for (int i = 0; i <16; i++) {
            //Map<String, Object> map=new HashMap<String, Object>();
            //map.put("img", icno[i]);
            //map.put("text",name[i]);
            int icon = R.drawable.tu;
            item Item = new item(icon);
            dataList.add(Item);
        }
    }


}
