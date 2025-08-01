package vcmsa.projects.wordleapplication

class GameResponse {

    data class GameResponse(
        val maskedWord: String?,
        val attemptsLeft: Int,
        val isGameOver: Boolean
    )
}