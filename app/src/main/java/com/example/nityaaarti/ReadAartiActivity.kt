package com.example.nityaaarti

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.max
import kotlin.math.min
import androidx.core.content.edit

class ReadAartiActivity : AppCompatActivity() {

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var currentTextSizeSp = 20f
    private lateinit var tvLyrics: TextView
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_read_aarti)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        tvLyrics = findViewById<TextView>(R.id.tvLyrics)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        prefs = getSharedPreferences("AartiSettings", Context.MODE_PRIVATE)
        currentTextSizeSp = prefs.getFloat("FONT_SIZE", 20f)

        tvLyrics.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentTextSizeSp)

        val aartiName = intent.getStringExtra("AARTI_NAME") ?: "Aarti"
        tvTitle.text = aartiName
        tvLyrics.text = AartiRepository.getAartiLyrics(aartiName)
        initializeZoom()
        btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        prefs.edit { putFloat("FONT_SIZE", currentTextSizeSp) }
    }

    private fun initializeZoom() {
        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val scaleFactor = detector.scaleFactor
                currentTextSizeSp *= scaleFactor
                currentTextSizeSp = max(14f, min(currentTextSizeSp, 60f))

                tvLyrics.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentTextSizeSp)
                return true
            }
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            scaleGestureDetector.onTouchEvent(it)
        }
        return super.dispatchTouchEvent(ev)
    }
}