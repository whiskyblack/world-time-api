package alphalabs.ads.banner

import android.content.Context
import com.google.android.gms.ads.*
import alphalabs.base.BuildConfig

class AdmobAdaptiveBanner {
    var listener: AdmobAdaptiveBannerListener? = null
    private var adId: String = "ca-app-pub-3940256099942544/6300978111"
    private var adView: AdView? = null

    fun loadAds(context: Context) {
        adView = AdView(context).apply {
            adUnitId = adId
            val adSize = getAdSize(context)
            setAdSize(adSize)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    adView?.apply {
                        listener?.onAdLoaded(this)
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    listener?.onAdFailToLoad(loadAdError)
                }
            }
            loadAd(AdRequest.Builder().build())
        }
    }

    fun setAdsId(adId: String) {
        if (!BuildConfig.DEBUG) this.adId = adId
    }

    private fun getAdSize(context: Context): AdSize {
        val displayMetrics = context.resources.displayMetrics
        val density: Float = displayMetrics.density
        val adWidth: Int = (displayMetrics.widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }
}