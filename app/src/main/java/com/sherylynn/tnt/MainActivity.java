package com.sherylynn.tnt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

        try{
            final Media media =new Media(mLibVLC,getAssets().openFd(ASSET_FILENAME));
            mMediaPlayer.setMedia(media);
            media.release();
        }catch(IOException e){
            throw new RuntimeException("Invalid asset folder");
        }
        mMediaPlayer.play();
    }
}
