package vcmsa.projects.wordleapplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://wordle20250313105430.azurewebsites.net/"

    val instance: GameApiService by lazy {
        val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

        retrofit.create(GameApiService::class.java)
        }
}