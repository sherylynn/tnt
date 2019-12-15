package com.sherylynn.tnt

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.blankj.utilcode.util.LogUtils
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.RendererItem
import org.videolan.libvlc.util.DisplayManager
import org.videolan.libvlc.util.VLCVideoLayout
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private var mVideoLayout: VLCVideoLayout? = null
    private val enableCloneMode = false
    private val isBenchmark = false
    private var mLibVLC: LibVLC? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var mDisplayManager: DisplayManager? = null
    //private val mRendererItem: LiveData<RendererItem>? = null
    private val mRendererItem = RendererLiveData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mRendererItem.observe(this, Observer { setRenderer(it) })
        setContentView(R.layout.activity_main)
        val args = ArrayList<String>()
        args.add("-vvv")
        mLibVLC = LibVLC(this, args)
        mMediaPlayer = MediaPlayer(mLibVLC)
        mVideoLayout = findViewById(R.id.video_layout)
        LogUtils.e("日志" + "onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer!!.release()
        mLibVLC!!.release()
    }

    override fun onStart() {
        super.onStart()
        mDisplayManager = DisplayManager(this, mRendererItem, false, enableCloneMode, isBenchmark)
        //mMediaPlayer.attachViews(mVideoLayout,null,ENABLE_SUBTITLES,USE_TEXTURE_VIEW);
        mMediaPlayer!!.attachViews(mVideoLayout!!, mDisplayManager, ENABLE_SUBTITLES, USE_TEXTURE_VIEW)
        LogUtils.e("日志" + "onStart")
        if (PLAY === "local") {
            try {
                val media = Media(mLibVLC, assets.openFd(ASSET_FILENAME))
                mMediaPlayer!!.media = media
                media.release()
            } catch (e: IOException) {
                throw RuntimeException("Invalid asset folder")
            }
            mMediaPlayer!!.play()
        } else if (PLAY === "short") {
            mMediaPlayer!!.play(Uri.parse(Net_address))
        } else { //mMediaPlayer.play(Uri.parse(Net_address));
            Log.v("测试", "测试地址" + Uri.parse(Net_address))
            LogUtils.e(mDisplayManager!!.isSecondary)
            LogUtils.e(mDisplayManager!!.displayType)
            Toast.makeText(this, "窗口" + mDisplayManager!!.isPrimary, Toast.LENGTH_SHORT).show()
            mMediaPlayer!!.media = Media(mLibVLC, Uri.parse(Net_address))
            mMediaPlayer!!.play()
        }
    }

    companion object {
        private const val USE_TEXTURE_VIEW = false
        private const val ENABLE_SUBTITLES = true
        private const val ASSET_FILENAME = "bbb.m4v"
        private const val Net_address = "http://mlive1.91kds.cn/b9/shydbst.m3u8?id=cctv1&nwtime=1576330610&sign=a32dd2b0e1519ada73f5d08ce92d2994&mip=112.64.68.25&from=com"
        //private static final String Net_address="http://al.hls.huya.com/backsrc/1417202357-1417202357-6086837775129116672-2750277196-10057-A-0-1.m3u8";
        private const val PLAY = "debug"
    }
}