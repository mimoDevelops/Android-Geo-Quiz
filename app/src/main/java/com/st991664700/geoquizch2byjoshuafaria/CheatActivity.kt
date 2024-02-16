package com.st991664700.geoquizch2byjoshuafaria

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.widget.Button
import android.widget.TextView

class CheatActivity : Activity() {

    companion object {
        const val EXTRA_ANSWER_IS_TRUE = "com.st991664700.geoquizch2byjoshuafaria.answer_is_true"
        const val EXTRA_ANSWER_SHOWN = "com.st991664700.geoquizch2byjoshuafaria.answer_shown"
    }

    private var mAnswerIsTrue = false

    private lateinit var mAnswerTextView: TextView
    private lateinit var mShowAnswerButton: Button
    private lateinit var mBackButton: Button  // Declare the back button

    private lateinit var mApiLevelTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        if (savedInstanceState != null) {
            mAnswerIsTrue = savedInstanceState.getBoolean(EXTRA_ANSWER_SHOWN, false)
        } else {
            mAnswerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        }

        mAnswerTextView = findViewById(R.id.answer_text_view)
        mShowAnswerButton = findViewById(R.id.show_answer_button)
        mShowAnswerButton.setOnClickListener {
            if (mAnswerIsTrue) {
                mAnswerTextView.setText(R.string.true_button)
            } else {
                mAnswerTextView.setText(R.string.false_button)
            }
            setAnswerShownResult(true)
        }

        // Initialize and set up the back button
        mBackButton = findViewById(R.id.back_button)
        mBackButton.setOnClickListener {
            finish()  // Finish the activity to return to the previous one
        }

        setAnswerShownResult(false)
        // Set up API level TextView
        mApiLevelTextView = findViewById(R.id.api_level_text_view)
        mApiLevelTextView.text = "API level " + Build.VERSION.SDK_INT
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(EXTRA_ANSWER_SHOWN, mAnswerIsTrue)
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent()
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        setResult(RESULT_OK, data)
    }
}