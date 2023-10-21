package alphalabs.extensions

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat

private fun Context.getSharedPreferences() =
    getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

fun Context.getBoolean(key: String, defaultValue: Boolean = false) =
    getSharedPreferences().getBoolean(key, defaultValue)

fun Context.getInt(key: String, defaultValue: Int = -1) =
    getSharedPreferences().getInt(key, defaultValue)

fun Context.getFloat(key: String, defaultValue: Float = -1f) =
    getSharedPreferences().getFloat(key, defaultValue)

fun Context.getLong(key: String, defaultValue: Long = -1) =
    getSharedPreferences().getLong(key, defaultValue)

fun Context.getString(key: String, defaultValue: String = "") =
    getSharedPreferences().getString(key, defaultValue)

fun Context.putExtra(key: String, value: Any) {
    val editor = getSharedPreferences().edit()
    when (value) {
        is Boolean -> editor.putBoolean(key, value)
        is Int -> editor.putInt(key, value)
        is String -> editor.putString(key, value)
        is Float -> editor.putFloat(key, value)
        is Long -> editor.putLong(key, value)
    }
    editor.apply()
}

fun Context.isNetworkConnected(): Boolean {
    ContextCompat.getSystemService(this, ConnectivityManager::class.java)?.let {
        return it.activeNetworkInfo != null && it.activeNetworkInfo!!.isConnected
    } ?: return true
}