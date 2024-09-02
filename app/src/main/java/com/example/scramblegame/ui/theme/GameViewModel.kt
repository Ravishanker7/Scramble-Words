package com.example.scramblegame.ui.theme


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.scramblegame.data.MAX_NO_OF_WORDS
import com.example.scramblegame.data.SCORE_INCREASE
import com.example.scramblegame.data.allWords

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel(){

    var userGuess by mutableStateOf(" ")
        private set

    private val _UiState= MutableStateFlow(GameUiState())

    val uiState : StateFlow<GameUiState> = _UiState.asStateFlow()

    private lateinit var currentword : String

    private val usedWords : MutableSet<String> = mutableSetOf()

    init {
        resetGame()
    }
    private fun pickRandomWordandShuffle() : String{
        currentword= allWords.random()
        if(usedWords.contains(currentword)){
            return pickRandomWordandShuffle()
        }else{
            usedWords.add(currentword)
            return shuffleCurrentWords(currentword)
        }
    }

    private fun shuffleCurrentWords(word : String) : String{
        val tempWord=word.toCharArray()

        tempWord.shuffle()
        return String(tempWord)
    }

    fun resetGame(){
        usedWords.clear()
        _UiState.value=GameUiState(currentScrambledWord = pickRandomWordandShuffle())
    }

    fun updateUserGuess(guessedWord : String){
        userGuess=guessedWord
    }

    fun checkUserGuess(){
        if(userGuess.equals(currentword)){
            val updateScore=_UiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updateScore)
        }else{
            _UiState.update { it->
                it.copy(isGuessedWordWrong = true)
            }
        }
        updateUserGuess("")
    }

    fun updateGameState(updatedscore : Int){
        if (usedWords.size== MAX_NO_OF_WORDS){
            _UiState.update { it ->
                it.copy(
                    isGuessedWordWrong = false,
                    score = updatedscore,
                    isGameOver = true
                )
            }
        }
        _UiState.update { it->
            it.copy(
                isGuessedWordWrong = false,
                score = updatedscore,
                currentWordCount = it.currentWordCount.inc(),
                currentScrambledWord = pickRandomWordandShuffle()
            )
        }
    }

    fun skipWord(){
        updateGameState(_UiState.value.score)
        updateUserGuess("")
    }
}