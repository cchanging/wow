package com.example.c_changing.main;

import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;

import java.io.IOException;

public class item {
    public String itemContent;
    public int itemImageResId;
    public boolean exist;
    public int isclicked;
    private int id;
    public MediaPlayer mediaPlayer;
    public MediaPlayer mediaPlayer_2;
    public MediaPlayer mediaPlayer_3;

    public String item;
    public ButtonCircleProgressBar mProgressBar;
    public static final int MSG_PROGRESS_UPDATE = 0x110;
    private int progress;
    private  int maxd;
    public CanvasView canvasView;
    public item(int i) {
        this.itemImageResId = 0;
        this.itemContent = "";
        this.isclicked = 0;
        this.exist = false;
        this.id = i;
        this.item = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SavAudio/" + id+ ".wav";
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //progress = mProgressBar.getProgress();
            //mProgressBar.setProgress(++progress);
            mProgressBar.setProgress(mediaPlayer.getCurrentPosition()*100/maxd);
            if(mediaPlayer_2 == null){
                mediaPlayer_2 = new MediaPlayer();
                try {
                    mediaPlayer_2.setDataSource(item);
                    mediaPlayer_2.prepare();
                    if(mediaPlayer_3 != null)
                        mediaPlayer_3.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mediaPlayer.getCurrentPosition()*100/maxd >= 96) {
                //mHandler.removeMessages(MSG_PROGRESS_UPDATE);
                //mProgressBar.setStatus(ButtonCircleProgressBar.Status.End);
                //mProgressBar.setProgress(0);
                mediaPlayer_3 = mediaPlayer;
                mediaPlayer = mediaPlayer_2;
                mediaPlayer.start();
                mediaPlayer_2 = null;
                mHandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 100);
            }else{
                mHandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 100);
            }

        };
    };

    public void runmusic(ButtonCircleProgressBar ProgressBar){
        mProgressBar = ProgressBar;
        if (mediaPlayer == null) {
            mediaPlayer =  new MediaPlayer();
            //mediaPlayer.setLooping(true);
            try {
                mediaPlayer.setDataSource(item);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            maxd = mediaPlayer.getDuration();
            /*progressBar.setMax(mediaPlayer.getDuration());
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                        progressBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            },0,50);*/
        }
        if (mProgressBar.getStatus()== ButtonCircleProgressBar.Status.Starting){
            mProgressBar.setStatus(ButtonCircleProgressBar.Status.End);
            mHandler.removeMessages(MSG_PROGRESS_UPDATE);
        }else{
            mHandler.sendEmptyMessage(MSG_PROGRESS_UPDATE);
            mProgressBar.setStatus(ButtonCircleProgressBar.Status.Starting);
        }
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else {
            mediaPlayer.start();
        }
    }

    public void change(){
        String item = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SavAudio/" + id+ ".wav";
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mHandler.removeMessages(0x110);
                mProgressBar.setStatus(ButtonCircleProgressBar.Status.End);
                mProgressBar.setProgress(0);
            }
            mediaPlayer.stop();
            mediaPlayer.release();
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setLooping(true);
                mediaPlayer.setDataSource(item);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            maxd = mediaPlayer.getDuration();
        }
    }
}
