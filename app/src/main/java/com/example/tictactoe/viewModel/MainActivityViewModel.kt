package com.example.tictactoe.viewModel

import android.widget.ImageButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tictactoe.enums.PlayerTurn
import com.example.tictactoe.R
import com.example.tictactoe.enums.GameStatus

class MainActivityViewModel : ViewModel() {
    private var valueFields: Array<Array<Char?>>
    private var playerTurn: PlayerTurn = PlayerTurn.PLAYER_1
    private var roundCount: Int = 0
    private var _player1Points = MutableLiveData<Int>(0)
    private var _player2Points = MutableLiveData<Int>(0)
    private val TOTAL_ROUNDS = 9
    private var _clearBorad = MutableLiveData<Boolean>(true)
    var _gameStatus = MutableLiveData<GameStatus>()


    init {
        valueFields =
            arrayOf(arrayOf(null, null, null), arrayOf(null, null, null), arrayOf(null, null, null))
    }

    fun onImageButtonClicked(imageButton: ImageButton) {
        if (imageButton.drawable != null) return
        val tag = imageButton.tag as Pair<*, *>
        when (playerTurn) {
            PlayerTurn.PLAYER_1 -> {
                imageButton.setImageResource(R.drawable.ic_cross)
                valueFields[tag.first as Int][tag.second as Int] = 'x'
            }
            PlayerTurn.PLAYER_2 -> {
                imageButton.setImageResource(R.drawable.ic_zero_green)
                valueFields[tag.first as Int][tag.second as Int] = 'o'
            }
        }
        roundCount++

        if (checkForWin()) {
            when (playerTurn) {
                PlayerTurn.PLAYER_1 -> {
                    winnerPlayer(GameStatus.PLAYER1_WON)
                }
                PlayerTurn.PLAYER_2 -> {
                    winnerPlayer(GameStatus.PLAYER2_WON)
                }
            }
        } else if (roundCount == TOTAL_ROUNDS) {
            matchDrawn()
        } else {
            playerTurn = when (playerTurn) {
                PlayerTurn.PLAYER_1 -> PlayerTurn.PLAYER_2
                PlayerTurn.PLAYER_2 -> PlayerTurn.PLAYER_1
            }
        }
    }

    private fun checkForWin(): Boolean {
        if (checkForRowWiseWin(valueFields)
            || checkForColumnWiseWin(valueFields)
            || checkForDiagonalWin(valueFields)
        )
            return true

        return false
    }

    private fun checkForRowWiseWin(fields: Array<Array<Char?>>): Boolean {
        for (i in 0..2) {
            if ((fields[i][0] == fields[i][1]) &&
                (fields[i][0] == fields[i][2]) &&
                (fields[i][0] != null)
            ) return true
        }
        return false
    }

    private fun checkForColumnWiseWin(fields: Array<Array<Char?>>): Boolean {
        for (i in 0..2) {
            if ((fields[0][i] == fields[1][i]) &&
                (fields[0][i] == fields[2][i]) &&
                (fields[0][i] != null)
            ) return true
        }
        return false
    }

    private fun checkForDiagonalWin(fields: Array<Array<Char?>>): Boolean {
        if ((fields[0][0] == fields[1][1]) &&
            (fields[0][0] == fields[2][2]) &&
            (fields[0][0] != null)
        ) return true
        if ((fields[0][2] == fields[1][1]) &&
            (fields[0][2] == fields[2][0]) &&
            (fields[0][2] != null)
        ) return true
        return false
    }


    private fun winnerPlayer(player: GameStatus) {
        if (player == GameStatus.PLAYER1_WON) {
            _player1Points.value = _player1Points.value?.plus(1)
            _gameStatus.postValue(GameStatus.PLAYER1_WON)
        } else {
            _player2Points.value = _player2Points.value?.plus(1)
            _gameStatus.postValue(GameStatus.PLAYER2_WON)
        }
        clearBoard()

    }

    private fun clearBoard() {
        _clearBorad.postValue(true)
        roundCount = 0
        playerTurn = PlayerTurn.PLAYER_1
        valueFields =
            arrayOf(arrayOf(null, null, null), arrayOf(null, null, null), arrayOf(null, null, null))
    }


    private fun matchDrawn() {
        _gameStatus.postValue(GameStatus.DRAW)
        clearBoard()
    }

    fun boardCleared() {
        _clearBorad.postValue(false)
    }

    fun resetBoard() {
        _player1Points.value = 0
        _player2Points.value = 0
        clearBoard()
    }

    val player1Points: LiveData<Int>
        get() = _player1Points
    val player2Points: LiveData<Int>
        get() = _player2Points
    val clearBoard: LiveData<Boolean>
        get() = _clearBorad
    val gameStatus: LiveData<GameStatus>
        get() = _gameStatus
}