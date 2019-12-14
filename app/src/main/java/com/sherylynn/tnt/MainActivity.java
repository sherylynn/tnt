package com.sherylynn.tnt;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final boolean USE_TEXTURE_VIEW=false;
    private static final boolean ENABLE_SUBTITLES=true;
    private static final String ASSET_FILENAME="bbb.m4v";
    private static final String Net_address="http://mlive1.91kds.cn/b9/shydbst.m3u8?id=cctv1&nwtime=1576330610&sign=a32dd2b0e1519ada73f5d08ce92d2994&mip=112.64.68.25&from=com";
    //private static final String Net_address="http://al.hls.huya.com/backsrc/1417202357-1417202357-6086837775129116672-2750277196-10057-A-0-1.m3u8";
    private static final String PLAY="short";

    private VLCVideoLayout mVideoLayout=null;

    private LibVLC mLibVLC =null;
    private MediaPlayer mMediaPlayer=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<String > args=new ArrayList<>();
        args.add("-vvv");
        mLibVLC=new LibVLC(this,args);
        mMediaPlayer=new MediaPlayer(mLibVLC);

        mVideoLayout=findViewById(R.id.video_layout);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mMediaPlayer.release();
        mLibVLC.release();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mMediaPlayer.attachViews(mVideoLayout,null,ENABLE_SUBTITLES,USE_TEXTURE_VIEW);

        if(PLAY=="local"){
            try{
                final Media media =new Media(mLibVLC,getAssets().openFd(ASSET_FILENAME));
                mMediaPlayer.setMedia(media);
                media.release();
            }catch(IOException e){
                throw new RuntimeException("Invalid asset folder");
            }
            mMediaPlayer.play();
        }else if(PLAY=="short") {
            mMediaPlayer.play(Uri.parse(Net_address));
        }else{
            //mMediaPlayer.play(Uri.parse(Net_address));
            Log.v("测试",""+Uri.parse(Net_address));
            mMediaPlayer.setMedia(new Media(mLibVLC,Uri.parse(Net_address)));
            mMediaPlayer.play();
        }
    }
}
