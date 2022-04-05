package com.example.tictactoe

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.example.tictactoe.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var buttons: Array<Array<ImageButton>>
    private lateinit var binding: ActivityMainBinding

    private var playerTurn: PlayerTurn = PlayerTurn.PLAYER_1
    private var roundCount: Int = 0
    private var player1Points: Int = 0
    private var player2Points: Int = 0
    private  val TOTAL_ROUNDS=9
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        buttons = Array(3) { row ->
            Array(3) { column ->
                initializeImageButton(row, column)
            }
        }
        binding.btnReset.setOnClickListener {
            player1Points = 0
            player2Points = 0
            updateScore()
            clearBoard()
        }
    }

    private fun initializeImageButton(row: Int, column: Int): ImageButton {
        val btn: ImageButton =
            findViewById(resources.getIdentifier("btn$row$column", "id", packageName))
        btn.setOnClickListener {
            onImageButtonClicked(btn)
        }
        return btn
    }

    private fun onImageButtonClicked(imageButton: ImageButton) {
        if (imageButton.drawable != null) return
        when (playerTurn) {
            PlayerTurn.PLAYER_1 -> {
                imageButton.setImageResource(R.drawable.ic_cross)
            }
            PlayerTurn.PLAYER_2 -> {
                imageButton.setImageResource(R.drawable.ic_zero_green)
            }
        }
        roundCount++

        if (checkForWin()) {
            when (playerTurn) {
                PlayerTurn.PLAYER_1 -> {
                    winnerPlayer(1)
                }
                PlayerTurn.PLAYER_2 -> {
                    winnerPlayer(2)
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
        val fields = Array(3) { r ->
            Array(3) { c ->
                getField(buttons[r][c])
            }
        }
        if (checkForRowWiseWin(fields)
            || checkForColumnWiseWin(fields)
            || checkForDiagonalWin(fields))
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

    private fun getField(btn: ImageButton): Char? {
        val drw: Drawable? = btn.drawable
        val drwCross: Drawable? =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_cross, null)
        val drwHeart: Drawable? =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_zero_green, null)

        return when (drw?.constantState) {
            drwCross?.constantState -> 'x'
            drwHeart?.constantState -> 'o'
            else -> null
        }

    }

    private fun winnerPlayer(player: Int) {
        if (player == 1) player1Points++ else player2Points++
        Toast.makeText(applicationContext, "Player $player Won!", Toast.LENGTH_LONG).show()
        Snackbar.make(binding.root, "Player $player Won!", Snackbar.LENGTH_LONG).show()
        updateScore()
        clearBoard()

    }

    private fun clearBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].setImageResource(0)
            }
        }
        roundCount = 0
        playerTurn = PlayerTurn.PLAYER_1
    }

    private fun updateScore() {
        binding.apply {
            player1Score.setText("Player 1 Score : $player1Points")
            player2Score.setText("Player 2 Score : $player2Points")
        }
    }

    private fun matchDrawn() {
        Toast.makeText(applicationContext, "Match Draw!", Toast.LENGTH_SHORT).show()
         Snackbar.make(binding.root, "Match Draw!", Snackbar.LENGTH_LONG).show()
        clearBoard()
    }
}