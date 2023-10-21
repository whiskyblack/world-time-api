package alphalabs.ads.appopen

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import java.util.Date

class SplashAppOpen {
    private var appOpenAd: AppOpenAd? = null
    private var adsStatus = ADS_READY_TO_LOAD
    private var loadTime: Long = 0

    var listener: SplashAppOpenListener? = null
    var adId = "ca-app-pub-3940256099942544/3419835294"

    private var loadCallback = object : AppOpenAdLoadCallback() {
        override fun onAdLoaded(appOpenAd: AppOpenAd) {
            super.onAdLoaded(appOpenAd)
            this@SplashAppOpen.appOpenAd = appOpenAd
            loadTime = Date().time
            adsStatus = ADS_LOADED
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            super.onAdFailedToLoad(loadAdError)
            adsStatus = ADS_READY_TO_LOAD
            listener?.onAppOpenFailToLoad(loadAdError.message)
        }
    }

    val isAdAvailable get() = adsStatus == ADS_LOADED && appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)

    fun showAdIfAvailable(activity: Activity) {
        if (isAdAvailable) {
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdShowedFullScreenContent() {
                        listener?.onAppOpenShowed()
                    }

                    override fun onAdDismissedFullScreenContent() {
                        listener?.onAppOpenDismiss()
                        adsStatus = ADS_READY_TO_LOAD
                        appOpenAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        listener?.onAppOpenFailedToShow()
                        adsStatus = ADS_READY_TO_LOAD
                    }
                }
            appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd?.show(activity)
        } else {
            listener?.onAppOpenFailedToShow()
        }
    }

    fun loadAds(activity: Activity) {
        if (isAdAvailable || adsStatus == ADS_LOADING) {
            return
        }

        val request = AdRequest.Builder().build()
        AppOpenAd.load(activity, adId, request, loadCallback)
        adsStatus = ADS_LOADING
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    companion object {
        const val ADS_READY_TO_LOAD = 0
        const val ADS_LOADING = 1
        const val ADS_LOADED = 2
    }
}