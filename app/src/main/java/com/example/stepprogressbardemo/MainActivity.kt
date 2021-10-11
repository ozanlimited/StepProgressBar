package com.example.stepprogressbardemo

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import com.ozan.progress.StepProgressBar

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<StepProgressBar>(R.id.stepProgressBar).apply {
            max = 5
            step = 2
        }
        findViewById<Button>(R.id.step). setOnClickListener {
            findViewById<StepProgressBar>(R.id.stepProgressBar).apply {
                step += 1
            }
        }

    }
}
