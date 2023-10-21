package vn.alphalabs.worldtimeapi

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val WORLD_TIME_BASE_URL = "https://worldtimeapi.org/"
class WorldTimeApi {
    private val client = OkHttpClient().newBuilder()
        .callTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(WORLD_TIME_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun getWorldTimeService(): WorldTimeService {
        return retrofit.create(WorldTimeService::class.java)
    }

    fun cancelAll() {
        client.dispatcher().cancelAll()
    }
}