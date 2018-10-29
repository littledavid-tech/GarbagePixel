package cn.shycoder.garbagepixel.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.shycoder.garbagepixel.lib.Pixel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        Pixel.with(this)
                .from("http://www.shycoder.cn/usr/themes/handsome/usr/img/my/head.png")
                .into(iv)
    }
}
