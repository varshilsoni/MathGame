package com.varshil.mathgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.*
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    lateinit var textViewScore : TextView
    lateinit var textViewLife: TextView
    lateinit var textViewTime : TextView

    lateinit var textViewQuestion : TextView
    lateinit var editTextAnswer : EditText

    lateinit var buttonOk : Button
    lateinit var buttonNext : Button

    var correctAnswer = 0
    var userScore = 0
    var userLife = 3

    lateinit var timer : CountDownTimer
    private val startTimerInMillis : Long = 20000
    var timeLeftInMillis : Long = startTimerInMillis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        supportActionBar!!.title = "Addition"

        textViewScore = findViewById(R.id.textViewScore)
        textViewLife = findViewById(R.id.textViewLife)
        textViewTime = findViewById(R.id.textViewTime)

        textViewQuestion = findViewById(R.id.textViewQuestion)
        editTextAnswer = findViewById(R.id.editTextAnswer)

        buttonOk = findViewById(R.id.buttonOk)
        buttonNext = findViewById(R.id.buttonNext)

        gameContinue()

        buttonOk.setOnClickListener {

            val input = editTextAnswer.text.toString()

            if(input == ""){
                Toast.makeText(applicationContext, "Please insert answer or click on next button",
                Toast.LENGTH_SHORT).show()
            }
            else{

                pauseTimer()

                val userAnswer = input.toInt()

                if(userAnswer == correctAnswer){

                    textViewQuestion.text = "Congratulations.."
                    userScore += 10
                    textViewScore.text = userScore.toString()

                }
                else{
                    textViewQuestion.text = "Sorry, wrong answer"
                    userLife --
                    textViewLife.text = userLife.toString()
                }

            }

        }
        buttonNext.setOnClickListener {

            pauseTimer()
            resetTimer()

            editTextAnswer.setText("")

            if(userLife == 0)
            {
                Toast.makeText(applicationContext, "Game Over", Toast.LENGTH_SHORT).show()
                var intent = Intent(this@GameActivity, ResultActivity::class.java)
                intent.putExtra("score", userScore)
                startActivity(intent)
                finish()
            }
            else{
                gameContinue()
            }

        }
    }

    fun gameContinue(){

        var number1 = Random.nextInt(0,100)
        var number2 = Random.nextInt(0,100)

        textViewQuestion.text = "$number1 + $number2"

        correctAnswer = number1 + number2

        startTimer()
    }

    fun startTimer(){

        timer = object : CountDownTimer(timeLeftInMillis, 1000){
            override fun onTick(millisUntilFinished: Long) {

                timeLeftInMillis = millisUntilFinished
                updateText()
            }

            override fun onFinish() {

                pauseTimer()
                resetTimer()
                updateText()

                userLife--
                textViewLife.text = userLife.toString()
                textViewQuestion.text = "sorry, your time is up"
            }

        }.start()
    }

    fun updateText(){
        val remainingTime : Int = (timeLeftInMillis / 1000).toInt()
        textViewTime.text = String.format(Locale.getDefault(), "%02d", remainingTime)
    }

    fun pauseTimer(){
        timer.cancel()
    }

    fun resetTimer(){
        timeLeftInMillis = startTimerInMillis
        updateText()
    }
}