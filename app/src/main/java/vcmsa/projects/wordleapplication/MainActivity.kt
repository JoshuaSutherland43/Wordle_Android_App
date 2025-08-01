package vcmsa.projects.wordleapplication

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vcmsa.projects.wordleapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var wordToGuess: String = ""
    private var currentRow = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchWordFromAPI()

        keepPassingFocus()
        setupTextWatchers()

        binding.btnPlayAgain.setOnClickListener {
            resetGame()
        }

        binding.btnPlayAgain.visibility = View.GONE;

    }

    private fun fetchWordFromAPI(onWordFetched: (() -> Unit)? = null) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://wordle20250313105430.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(WordleApi::class.java)

        apiService.getWord().enqueue(object : Callback<WordResponse> {
            override fun onResponse(call: Call<WordResponse>, response: Response<WordResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    wordToGuess = response.body()!!.word.uppercase()
                    Log.d("API_RESPONSE", "Word fetched: $wordToGuess")
                    onWordFetched?.invoke() // Invoke the callback
                } else {
                    Log.e("API_RESPONSE", "Failed to fetch word. Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                    Toast.makeText(applicationContext, "Failed to fetch word", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WordResponse>, t: Throwable) {
                Log.e("API_FAILURE", "Error: ${t.message}", t)
                Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // clear all text of the row before setting focus to the first edit text
    private fun setupTextWatchers() {
        val lastEdits = listOf(
            binding.edt15, binding.edt25, binding.edt35, binding.edt45, binding.edt55, binding.edt65
        )

        lastEdits.forEachIndexed { rowIndex, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        val rowEditTexts = getRowEditTexts(rowIndex + 1)
                        validateRow(rowEditTexts)
                    }
                }
            })
        }
    }

    private fun getRowEditTexts(rowNumber: Int): List<EditText> {
        return when (rowNumber) {
            1 -> listOf(binding.edt11, binding.edt12, binding.edt13, binding.edt14, binding.edt15)
            2 -> listOf(binding.edt21, binding.edt22, binding.edt23, binding.edt24, binding.edt25)
            3 -> listOf(binding.edt31, binding.edt32, binding.edt33, binding.edt34, binding.edt35)
            4 -> listOf(binding.edt41, binding.edt42, binding.edt43, binding.edt44, binding.edt45)
            5 -> listOf(binding.edt51, binding.edt52, binding.edt53, binding.edt54, binding.edt55)
            6 -> listOf(binding.edt61, binding.edt62, binding.edt63, binding.edt64, binding.edt65)
            else -> emptyList()
        }
    }

    private fun validateRow(editTexts: List<EditText>) {
        if (wordToGuess.isEmpty()) {
            Toast.makeText(applicationContext, "Word not loaded yet!", Toast.LENGTH_SHORT).show()
            return
        }

        val userGuess = editTexts.joinToString("") { it.text.toString().uppercase() }

        editTexts.forEachIndexed { index, editText ->
            when {
                userGuess[index] == wordToGuess[index] -> editText.setBackgroundColor(Color.GREEN)
                wordToGuess.contains(userGuess[index]) -> editText.setBackgroundColor(Color.YELLOW)
                else -> editText.setBackgroundColor(Color.RED)
            }
        }

        if (userGuess == wordToGuess) {
            binding.idTVCongo.text = "Congratulations! You guessed the word!"
            binding.idTVCongo.visibility = View.VISIBLE
            makeGameInactive()
        } else {
            if (currentRow < 6) {
                currentRow++
                getRowEditTexts(currentRow).firstOrNull()?.requestFocus()
            } else {
                //handle game over where the player lost.
                binding.idTVCongo.text = "Game Over. The word was $wordToGuess"
                binding.idTVCongo.visibility = View.VISIBLE
                makeGameInactive()
            }
        }
    }

    private fun makeGameInactive() {
        listOf(
            binding.edt11, binding.edt12, binding.edt13, binding.edt14, binding.edt15,
            binding.edt21, binding.edt22, binding.edt23, binding.edt24, binding.edt25,
            binding.edt31, binding.edt32, binding.edt33, binding.edt34, binding.edt35,
            binding.edt41, binding.edt42, binding.edt43, binding.edt44, binding.edt45,
            binding.edt51, binding.edt52, binding.edt53, binding.edt54, binding.edt55,
            binding.edt61, binding.edt62, binding.edt63, binding.edt64, binding.edt65
        ).forEach { it.isEnabled = false }
        binding.btnPlayAgain.visibility = View.VISIBLE;
    }

    private fun keepPassingFocus() {
        listOf(
            binding.edt11 to binding.edt12, binding.edt12 to binding.edt13, binding.edt13 to binding.edt14, binding.edt14 to binding.edt15,
            binding.edt21 to binding.edt22, binding.edt22 to binding.edt23, binding.edt23 to binding.edt24, binding.edt24 to binding.edt25,
            binding.edt31 to binding.edt32, binding.edt32 to binding.edt33, binding.edt33 to binding.edt34, binding.edt34 to binding.edt35,
            binding.edt41 to binding.edt42, binding.edt42 to binding.edt43, binding.edt43 to binding.edt44, binding.edt44 to binding.edt45,
            binding.edt51 to binding.edt52, binding.edt52 to binding.edt53, binding.edt53 to binding.edt54, binding.edt54 to binding.edt55,
            binding.edt61 to binding.edt62, binding.edt62 to binding.edt63, binding.edt63 to binding.edt64, binding.edt64 to binding.edt65
        ).forEach { (current, next) ->
            current.setOnKeyListener { _, _, _ ->
                if (current.text.length == 1) {
                    if (current != binding.edt15 && current != binding.edt25 && current != binding.edt35 && current != binding.edt45 && current != binding.edt55 && current != binding.edt65){
                        next.requestFocus()
                    }
                }
                false
            }
        }
    }
    private fun resetGame() {
        // Clear all EditText fields
        listOf(
            binding.edt11, binding.edt12, binding.edt13, binding.edt14, binding.edt15,
            binding.edt21, binding.edt22, binding.edt23, binding.edt24, binding.edt25,
            binding.edt31, binding.edt32, binding.edt33, binding.edt34, binding.edt35,
            binding.edt41, binding.edt42, binding.edt43, binding.edt44, binding.edt45,
            binding.edt51, binding.edt52, binding.edt53, binding.edt54, binding.edt55,
            binding.edt61, binding.edt62, binding.edt63, binding.edt64, binding.edt65
        ).forEach {
            it.text.clear()
            it.setBackgroundColor(Color.WHITE)
            it.isEnabled = true
        }

        // Reset currentRow
        currentRow = 1

        // Fetch a new word and update UI after the word is fetched
        fetchWordFromAPI {
            // Hide the game end message
            binding.idTVCongo.visibility = View.GONE

            // Hide the "Play Again" button
            binding.btnPlayAgain.visibility = View.GONE

            // Set focus to the first EditText
            binding.edt11.requestFocus()
        }
    }
}
