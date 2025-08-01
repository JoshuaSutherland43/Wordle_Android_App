package vcmsa.projects.wordleapplication

import retrofit2.Call
import retrofit2.http.*

interface GameApiService {
    @POST("game/start")
    fun startNewGame(): Call<GameResponse>

    @POST("game/guess/{letter}")
    fun guessLetter(@Path("letter") letter: Char): Call<GameResponse>

    @GET("game/status")
    fun getGameStatus(): Call<GameResponse>
}