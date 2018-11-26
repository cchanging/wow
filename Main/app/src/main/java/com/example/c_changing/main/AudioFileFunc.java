package com.example.c_changing.main;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
public class AudioFileFunc {
    //音频输入-麦克风
    public final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;

    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public final static int AUDIO_SAMPLE_RATE = 44100;  //44.1KHz,普遍使用的频率
    //录音输出文件
    private static String AUDIO_RAW_FILENAME = "RawAudio.raw";
    private static String AUDIO_WAV_FILENAME = "FinalAudio.wav";
    public static String AUDIO_AMR_FILENAME = "FinalAudio.amr";
    public static File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SavAudio");
    public static File [] files;
    public static int num;



    /**
     * 判断是否有外部存储设备sdcard
     * @return true | false
     */
    public static boolean isSdcardExit(){
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 获取麦克风输入的原始音频流文件路径
     * @return
     */
    public static String getRawFilePath(int loc){
        String mAudioRawPath = "";
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        //获取当前时间
        String str = formatter.format(curDate);

        if(isSdcardExit()){
            //files = f.listFiles();
            //num = files.length;
            //AUDIO_RAW_FILENAME =  String.valueOf(num) + ".raw";
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mAudioRawPath = fileBasePath+"/SavAudio/"+loc+".raw";//AUDIO_RAW_FILENAME;
        }

        return mAudioRawPath;
    }

    /**
     * 获取编码后的WAV格式音频文件路径
     * @return
     */
    public static String getWavFilePath(int loc){
        String mAudioWavPath = "";
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        //获取当前时间
        String str = formatter.format(curDate);
        if(isSdcardExit()){
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mAudioWavPath = fileBasePath+"/SavAudio/"+ loc + ".wav";//AUDIO_WAV_FILENAME;
        }
        return mAudioWavPath;
    }


    /**
     * 获取编码后的AMR格式音频文件路径
     * @return
     */
    public static String getAMRFilePath(){
        String mAudioAMRPath = "";
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        //获取当前时间
        String str = formatter.format(curDate);
        if(isSdcardExit()){
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mAudioAMRPath = fileBasePath+"/SavAudio/"+str + ".amr";//AUDIO_AMR_FILENAME;
        }
        return mAudioAMRPath;
    }


    /**
     * 获取文件大小
     * @param path,文件的绝对路径
     * @return
     */
    public static long getFileSize(String path){
        File mFile = new File(path);
        if(!mFile.exists())
            return -1;
        return mFile.length();
    }

}
