package vcmsa.projects.wordleapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface WordleApi {
    @GET("api/wordie/word")
    fun getWord(): Call<WordResponse>
    @POST("api/wordie/restart")
    fun restartGame(): Call<RestartResponse>
}


