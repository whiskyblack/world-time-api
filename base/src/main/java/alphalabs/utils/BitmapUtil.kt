package alphalabs.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.util.Size
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File

fun loadBitmapFromAssets(context: Context, size: Size, assetPath: String, callback: (Bitmap?) -> Unit) {
    Glide.with(context.applicationContext)
        .asBitmap()
        .load(assetPath)
        .listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                callback(null)
                return true
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.let(callback) ?: callback(null)
                return true
            }

        }).submit(size.width, size.height)
}


fun loadFileFromUrl(context: Context, size: Size, url: String, callback: (File?) -> Unit) {
    Glide.with(context.applicationContext)
        .asFile()
        .load(url)
        .listener(object : RequestListener<File> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<File>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.i("AIKO", "$e")
                callback(null)
                return true
            }

            override fun onResourceReady(
                file: File?,
                model: Any?,
                target: Target<File>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                file?.let { imageFile ->
                    callback(imageFile)
                } ?: callback(null)
                return true
            }
        }).submit(size.width, size.height)
}
