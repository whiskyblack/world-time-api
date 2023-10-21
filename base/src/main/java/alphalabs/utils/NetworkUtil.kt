package alphalabs.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress

fun isInternetAvailable(callback: (Boolean) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            withContext(Dispatchers.IO) {
                InetAddress.getByName("google.com")
                withContext(Dispatchers.Main) { callback(true) }
            }
        } catch (e: Exception) {
            Log.i("AIKO", "$e")
            withContext(Dispatchers.Main) { callback(false) }
        }
    }
}