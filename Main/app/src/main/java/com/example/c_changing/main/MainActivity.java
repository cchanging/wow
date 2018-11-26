package com.example.c_changing.main;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    File rootPath =  new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SavAudio");
    private int mState = -1;
    private GridView gridView;
    private List<item> dataList;
    private MyAdapter adapter;
    private TextView textView;
    private Button recode;
    private Button stop;
    private TextView txt;
    private UIHandler uiHandler;
    private UIThread uiThread;
    private CustomSeekBar seekBar;
    private int loc = -1;
    private int rec = -1;
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
        if(!rootPath.exists()){
            rootPath.mkdir();
        }
        gridView = (GridView) findViewById(R.id.gridview);
        textView = findViewById(R.id.textView);
        recode = findViewById(R.id.recode);
        stop = findViewById(R.id.stop);
        txt = (TextView)this.findViewById(R.id.txt);
        init();
        recode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });


        initData();


        //int[] to={R.id.img,R.id.text};

        //adapter=new SimpleAdapter(this, dataList, R.layout.item, from, to);

        gridView.setAdapter(adapter = new MyAdapter(this, dataList, this));
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
                /*adapter.setSeclection(arg2);
                if(dataList.get(arg2).isclicked == 0) {
                    dataList.get(arg2).isclicked = 1;
                    textView.setText(" Select: Track"+(arg2/4+1)+", "+(arg2%4+1));
                }
                else {
                    dataList.get(arg2).isclicked = 0;
                    textView.setText(" Select:");
                }
                adapter.notifyDataSetChanged();*/
            }
        });
    }

    void initData() {
        dataList = new ArrayList<item>();
        for (int i = 0; i <16; i++) {
            //Map<String, Object> map=new HashMap<String, Object>();
            //map.put("img", icno[i]);
            //map.put("text",name[i]);
            item Item = new item(i);
            dataList.add(Item);
        }
    }
    public void setText(String str){
        textView.setText(str);
    }

    private int  findSelected(){
        for(int i =0; i < dataList.size(); i++ ){
            if(dataList.get(i).isclicked == 1){
                return i;
            }
        }
        return -1;
    }
    private void init(){
        uiHandler = new UIHandler();
    }
    private void record(){
        int mResult = -1;
        loc = findSelected();
        if(loc == -1){
            mResult = -2;
        }
        else {
            if (mState != -1) {
                Message msg = new Message();
                Bundle b = new Bundle();// 存放数据
                b.putInt("cmd", CMD_RECORDFAIL);
                b.putInt("msg", ErrorCode.E_STATE_RECODING);
                msg.setData(b);

                uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
                return;
            }
            AudioRecordFunc mRecord_1 = AudioRecordFunc.getInstance();
            mResult = mRecord_1.startRecordAndFile(loc);
        }
        if(mResult == ErrorCode.SUCCESS){
            uiThread = new UIThread();
            new Thread(uiThread).start();
            mState = 0;
            rec = loc;
        }else{
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd",CMD_RECORDFAIL);
            b.putInt("msg", mResult);
            msg.setData(b);
            uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
        }
    }

    private void stop(){
        if(mState != -1){
            AudioRecordFunc mRecord_1 = AudioRecordFunc.getInstance();
            mRecord_1.stopRecordAndFile();

            if(uiThread != null){
                uiThread.stopThread();
            }
            if(uiHandler != null)
                uiHandler.removeCallbacks(uiThread);
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd",CMD_STOP);
            b.putInt("msg", mState);
            adapter.change(rec);
            adapter.notifyDataSetChanged();
            msg.setData(b);
            uiHandler.sendMessageDelayed(msg,1000); // 向Handler发送消息,更新UI
            mState = -1;
        }
    }

    private final static int CMD_RECORDING_TIME = 2000;
    private final static int CMD_RECORDFAIL = 2001;
    private final static int CMD_STOP = 2002;
    class UIHandler extends Handler {
        public UIHandler() {
        }
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.d("MyHandler", "handleMessage......");
            super.handleMessage(msg);
            Bundle b = msg.getData();
            int vCmd = b.getInt("cmd");
            switch(vCmd)
            {
                case CMD_RECORDING_TIME:
                    int vTime = b.getInt("msg");
                    MainActivity.this.txt.setText("正在录音中，已录制："+vTime+" s");
                    break;
                case CMD_RECORDFAIL:
                    int vErrorCode = b.getInt("msg");
                    String vMsg = ErrorCode.getErrorInfo(MainActivity.this, vErrorCode);
                    MainActivity.this.txt.setText("录音失败："+vMsg);
                    break;
                case CMD_STOP:
                    int vFileType = b.getInt("msg");
                    AudioRecordFunc mRecord_1 = AudioRecordFunc.getInstance();
                    long mSize = mRecord_1.getRecordFileSize();
                    MainActivity.this.txt.setText("录音已停止.录音文件:"+AudioFileFunc.getWavFilePath(rec));
                    break;
                default:
                    break;
            }
        }
    };
    class UIThread implements Runnable {
        int mTimeMill = 0;
        boolean vRun = true;
        public void stopThread(){
            vRun = false;
        }
        public void run() {
            while(vRun){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mTimeMill ++;
                Log.d("thread", "mThread........"+mTimeMill);
                Message msg = new Message();
                Bundle b = new Bundle();// 存放数据
                b.putInt("cmd",CMD_RECORDING_TIME);
                b.putInt("msg", mTimeMill);
                msg.setData(b);

                MainActivity.this.uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
            }

        }
    }
}
