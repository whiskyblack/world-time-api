package alphalabs.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import alphalabs.ASSETS_DIR

fun ImageView.load(@DrawableRes drawableRes: Int) {
    Glide.with(context.applicationContext)
        .load(drawableRes).into(this)
}

fun ImageView.load(data: Any) {
    Glide.with(context.applicationContext)
        .load(data).into(this)
}

fun ImageView.loadAssets(assetsPath: String) {
    Glide.with(context.applicationContext)
        .load("$ASSETS_DIR$assetsPath").into(this)
}