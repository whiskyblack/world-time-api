package alphalabs.ads.nativeadvanced

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd

class AdmobNative {
    var adId = "ca-app-pub-3940256099942544/2247696110"
    var listener: AdmobNativeListener? = null

    fun load(context: Context) {
        val builder = AdLoader.Builder(context, adId).forNativeAd { nativeAd: NativeAd? ->
            listener?.onAdLoaded(nativeAd)
        }

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                listener?.onAdLoadFailed(p0.message)
            }
        }).build()

        val request = AdRequest.Builder().build()
        adLoader.loadAd(request)
    }

}