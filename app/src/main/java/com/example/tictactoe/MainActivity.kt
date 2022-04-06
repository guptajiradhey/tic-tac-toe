package com.example.tictactoe

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.tictactoe.databinding.ActivityMainBinding
import com.example.tictactoe.enums.GameStatus
import com.example.tictactoe.enums.PlayerTurn
import com.example.tictactoe.viewModel.MainActivityViewModel
import com.example.tictactoe.viewModel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var buttons: Array<Array<ImageButton>>
    private lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainActivityViewModel


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        buttons = Array(3) { row ->
            Array(3) { column ->
                initializeImageButton(row, column)
            }
        }
        mainViewModel=ViewModelProvider(this,MainViewModelFactory()).get(MainActivityViewModel::class.java)
//        mainViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        mainViewModel.clearBoard.observe(this) {
            if (it) {
                clearBoard()
                mainViewModel.boardCleared()
            }
        }
        mainViewModel.gameStatus.observe(this) {
            when (it) {
                GameStatus.PLAYER1_WON -> {
                    Snackbar.make(binding.root, "Player 1 Won!", Snackbar.LENGTH_LONG).show()
                }
                GameStatus.PLAYER2_WON -> {
                    Snackbar.make(binding.root, "Player 2 Won!", Snackbar.LENGTH_LONG).show()
                }
                GameStatus.DRAW -> {
                    Snackbar.make(binding.root, "Match Draw!", Snackbar.LENGTH_LONG).show()
                }
            }
        }
        mainViewModel.player1Points.observe(this) {
            binding.player1Score.setText("Player 1 Score : $it")
        }
        mainViewModel.player2Points.observe(this) {
            binding.player2Score.setText("Player 2 Score : $it")
        }
        binding.btnReset.setOnClickListener {
            mainViewModel.resetBoard()
        }
    }

    private fun initializeImageButton(row: Int, column: Int): ImageButton {
        val btn: ImageButton =
            findViewById(resources.getIdentifier("btn$row$column", "id", packageName))
        btn.tag = Pair(row, column)
        btn.setOnClickListener {
            mainViewModel.onImageButtonClicked(btn)
        }
        return btn
    }

    private fun clearBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].setImageResource(0)
            }
        }
    }


}