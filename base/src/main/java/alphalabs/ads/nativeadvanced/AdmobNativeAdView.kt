package alphalabs.ads.nativeadvanced

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.gms.ads.nativead.NativeAd
import alphalabs.base.databinding.NativeAdMediumBinding
import alphalabs.extensions.gone
import alphalabs.extensions.visible

class AdmobNativeAdView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private var nativeAd: NativeAd? = null
    private val binding = NativeAdMediumBinding.inflate(LayoutInflater.from(context), this, false)
    init {
        addView(binding.root)
    }

    fun show(nativeAd: NativeAd) {
        this.nativeAd = nativeAd

        val store = nativeAd.store
        val advertiser = nativeAd.advertiser
        val headline = nativeAd.headline
        val body = nativeAd.body
        val cta = nativeAd.callToAction
        val starRating = nativeAd.starRating
        val icon = nativeAd.icon

        binding.nativeAdView.callToActionView = binding.tvCta
        binding.nativeAdView.headlineView = binding.tvPrimary
        binding.nativeAdView.mediaView = binding.mediaView

        binding.tvSecondary.visible()
        binding.tvCta.visible()
        binding.tvAdTag.visible()

        val secondaryText = if (adHasOnlyStore(nativeAd)) {
            binding.nativeAdView.storeView = binding.tvSecondary
            store
        } else if (!TextUtils.isEmpty(advertiser)) {
            binding.nativeAdView.advertiserView = binding.tvSecondary
            advertiser
        } else {
            ""
        }

        binding.tvPrimary.text = headline
        binding.tvCta.text = cta

        //  Set the secondary view to be the star rating if available.
        if (starRating != null && starRating > 0) {
            binding.tvSecondary.gone()
            binding.ratingBar.visible()
            binding.ratingBar.rating = starRating.toFloat()
            binding.nativeAdView.starRatingView = binding.ratingBar
        } else {
            binding.tvSecondary.text = secondaryText
            binding.tvSecondary.visible()
            binding.ratingBar.gone()
        }

        if (icon != null) {
            binding.icon.visible()
            binding.icon.setImageDrawable(icon.drawable)
        } else binding.icon.gone()

        if (!body.isNullOrEmpty()) {
            binding.tvBody.text = body
            binding.nativeAdView.bodyView = binding.tvBody
        } else binding.tvBody.gone()

        binding.nativeAdView.setNativeAd(nativeAd)
    }

    private fun adHasOnlyStore(nativeAd: NativeAd): Boolean {
        val store = nativeAd.store
        val advertiser = nativeAd.advertiser
        return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser)
    }
}