package alphalabs.tracking

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object Tracking {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun init(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    fun post(key: String) {
        firebaseAnalytics.logEvent(key, null)
    }

    fun postParam(key: String, paramName: String, value: String) {
        val bundle = Bundle()
        bundle.putString(paramName, value)
        firebaseAnalytics.logEvent(key, bundle)
    }
}