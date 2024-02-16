package com.st991664700.geoquizch2byjoshuafaria

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.st991664700.geoquizch2byjoshuafaria.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var cheatedOnQuestions: BooleanArray

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    private var currentIndex = 0
    private var mIsCheater = false

    companion object {
        private const val REQUEST_CODE_CHEAT = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cheatedOnQuestions = BooleanArray(questionBank.size)

        if (savedInstanceState != null) {
            mIsCheater = savedInstanceState.getBoolean("mIsCheater", false)
            cheatedOnQuestions = savedInstanceState.getBooleanArray("cheatedOnQuestions") ?: BooleanArray(questionBank.size)
        }

        val listener = View.OnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        binding.trueButton.setOnClickListener {
            checkAnswer(true)
        }

        binding.falseButton.setOnClickListener {
            checkAnswer(false)
        }

        binding.prevButton.setOnClickListener {
            currentIndex = if (currentIndex - 1 < 0) questionBank.size - 1 else currentIndex - 1
            updateQuestion()
        }

        binding.nextButton.setOnClickListener(listener)

        binding.questionTextView.setOnClickListener(listener)

        binding.cheatButton.setOnClickListener {
            val answerIsTrue = questionBank[currentIndex].answer
            val intent = Intent(this, CheatActivity::class.java)
            intent.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        updateQuestion()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("mIsCheater", mIsCheater)
        outState.putBooleanArray("cheatedOnQuestions", cheatedOnQuestions)
    }

    private fun updateQuestion() {
        val question = questionBank[currentIndex]
        binding.questionTextView.setText(question.textResId)
        if (cheatedOnQuestions[currentIndex]) {
            mIsCheater = true
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return
            }
            mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false)
            cheatedOnQuestions[currentIndex] = mIsCheater
        }
    }
}
