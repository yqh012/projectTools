package com.yqh.tv.screen.tools.activity

import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Surface
import android.view.SurfaceView
import android.view.TextureView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.yqh.tv.screen.tools.R
import com.yqh.tv.screen.tools.base.BaseActivity
import com.yqh.tv.screen.tools.databinding.ActivityTestBinding

class TestActivity : BaseActivity<ActivityTestBinding>(ActivityTestBinding::inflate) {
    private val mediaPlayer by lazy {
        MediaPlayer()
    }

    override fun initListener() {
        mediaPlayer.apply {
            setAudioStreamType(AudioManager.STREAM_SYSTEM)
            setOnErrorListener { mediaPlayer, what, exit ->
                LogUtils.e("player", "error : $what ; exit : $exit")
                toast("播放错误...")
                false
            }
            setOnInfoListener { mediaPlayer, what, exit ->
                LogUtils.e("player", "error : $what ; exit : $exit")
                true
            }
            setOnPreparedListener {
                LogUtils.e("player", "source play prepared ... ")
                toast("开始播放...")
                it.start()
            }
            setOnCompletionListener {
                LogUtils.e("player", "source play complet ... ")
                toast("播放完成...")
                finish()
            }
        }

        viewBinding.textureView.post {
            viewBinding.textureView.surfaceTextureListener =
                object : TextureView.SurfaceTextureListener {
                    override fun onSurfaceTextureAvailable(
                        surface: SurfaceTexture,
                        p1: Int,
                        p2: Int
                    ) {
                        mediaPlayer.setSurface(Surface(surface))
                        setPlayerSource()
                    }

                    override fun onSurfaceTextureSizeChanged(
                        p0: SurfaceTexture,
                        p1: Int,
                        p2: Int
                    ) {
                    }

                    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean = true


                    override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {}

                }
        }
    }

    private fun setPlayerSource() {
        mediaPlayer.setDataSource("http://vodhlsp-v1.yysh.mgtv.com/uploads/20191224/hls/fe53a2993147/fe53a2993147_600_720.m3u8?auth_key=1736426939-0-0-6678d64166d50d2df5f0a93d77a7ba0a")
//        mediaPlayer.setDataSource("http://live1.hrtn.net/live/cctv5hd_3000.m3u8?auth_key=1636185211-0f76421613784c6f93d45969ea893da4-0-c3f410676136e402834307546b18dd59")
//        mediaPlayer.setDataSource("http://live1.hrtn.net/live/cctv5jhd_3000/cctv5jhd_3000_163592808.ts?auth_key=1635942392-8125e1ace0594fbb8a569f3b9b88e705-0-fae5a6c9cd19e0fed87161c0e1a8b18d")
        mediaPlayer.prepareAsync()
    }

    override fun initData() {
    }

    fun toast(msg: Any) {
        Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.apply {
            stop()
            release()
        }
    }

}