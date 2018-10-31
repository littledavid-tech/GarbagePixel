package cn.shycoder.garbagepixel.app

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import cn.shycoder.garbagepixel.lib.Pixel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Pixel.with(this).from(R.drawable.background).into(ivWallBack)

        val list = listOf<String>("https://alpha.wallhaven.cc/wallpapers/thumb/small/th-91620.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-85647.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-81153.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-76214.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-671847.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-628429.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-579674.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-474982.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-47184.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-439768.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-411072.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-408520.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-401081.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-391239.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-317630.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-227704.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-222338.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-211377.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-200274.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-199573.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-198706.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-12241.jpg",
                "https://alpha.wallhaven.cc/wallpapers/thumb/small/th-117986.jpg")

        val adapter = ImgAdapter(this, list)
        gvImg.adapter = adapter

    }

    class ImgAdapter(val context: Context, val urlList: List<String>) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var tempView = convertView
            if (tempView == null) {
                tempView = LayoutInflater.from(context).inflate(R.layout.gv_item_img, parent, false)
                val iv = tempView!!.findViewById<ImageView>(R.id.iv)
                val holder = ViewHolder(iv)
                tempView.tag = holder
            }
            val holder = tempView.tag as ViewHolder
            Pixel.with(context)
                    .from(getItem(position).toString())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(holder.imageView)
            return tempView
        }

        override fun getItem(position: Int): Any {
            return urlList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return urlList.size
        }

        class ViewHolder(val imageView: ImageView)
    }
}
