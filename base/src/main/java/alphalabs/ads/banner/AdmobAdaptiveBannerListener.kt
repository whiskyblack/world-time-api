package alphalabs.ads.banner

import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

open class AdmobAdaptiveBannerListener {
    open fun onAdLoaded(adView: AdView) {}
    open fun onAdFailToLoad(loadAdError: LoadAdError) {}
}