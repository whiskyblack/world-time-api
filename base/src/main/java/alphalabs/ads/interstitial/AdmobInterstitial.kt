package alphalabs.ads.interstitial

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import alphalabs.base.BuildConfig

class AdmobInterstitial {
    private var interstitial: InterstitialAd? = null
    var adId = "ca-app-pub-3940256099942544/1033173712"
    var listener: AdmobInterstitialListener? = null
    var isShowing = false
    var isError = false
    private var timeAdLoaded = 0L

    fun setAdsId(adId: String) {
        if (!BuildConfig.DEBUG)
            this.adId = adId
    }

    fun isLoaded(): Boolean {
        return interstitial != null
    }

    fun loadAds(context: Context) {
        interstitial = null
        InterstitialAd.load(
            context.applicationContext,
            adId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    isError = true
                    timeAdLoaded = 0
                    listener?.onAdFailToLoad(loadAdError)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    isError = false
                    timeAdLoaded = System.currentTimeMillis()
                    interstitialAd.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent()
                                timeAdLoaded = 0
                                isShowing = true
                                listener?.onAdShowedFullScreen()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                timeAdLoaded = 0
                                isShowing = false
                                listener?.onAdFailToShow(adError)
                            }

                            override fun onAdDismissedFullScreenContent() {
                                timeAdLoaded = 0
                                super.onAdDismissedFullScreenContent()
                                isShowing = false
                                listener?.onAdDismissedFullScreen()
                            }
                        }
                    interstitial = interstitialAd
                }
            })
    }

    fun show(activity: AppCompatActivity) {
        if (isLoaded()) {
            isShowing = true
            interstitial?.show(activity)
        } else listener?.onAdFailToShow(AdError(0, "interstitial not loaded", "unknown"))
    }

    fun isReadyAd(): Boolean {
        return if (timeAdLoaded == 0L) false else (System.currentTimeMillis() - timeAdLoaded) < 2 * 60 * 60 * 1000
    }

    fun clear() {
        interstitial = null
        isShowing = false
    }
}