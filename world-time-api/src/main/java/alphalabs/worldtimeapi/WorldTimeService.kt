package alphalabs.worldtimeapi

import retrofit2.Response
import retrofit2.http.GET
import alphalabs.worldtimeapi.model.WorldTimeResponse

interface WorldTimeService {
    @GET("api/timezone/Asia/Bangkok")
    suspend fun getWorldTime() : Response<WorldTimeResponse>
}