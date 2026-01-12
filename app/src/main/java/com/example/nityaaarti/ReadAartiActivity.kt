package com.example.nityaaarti

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.max
import kotlin.math.min

class ReadAartiActivity : AppCompatActivity() {

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var currentTextSizeSp = 20f
    private lateinit var tvLyrics: TextView
    private lateinit var tvTitle: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var prefs: SharedPreferences

    // --- NAVIGATION VARIABLES ---
    private var aartiList = listOf<String>()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_read_aarti)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Views
        tvTitle = findViewById(R.id.tvTitle)
        tvLyrics = findViewById(R.id.tvLyrics)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // New Buttons
        val btnPrev = findViewById<TextView>(R.id.btnPrev)
        val btnNext = findViewById<TextView>(R.id.btnNext)
        scrollView = findViewById<ScrollView>(R.id.scrollView)

        // Load Zoom Settings
        prefs = getSharedPreferences("AartiSettings", Context.MODE_PRIVATE)
        currentTextSizeSp = prefs.getFloat("FONT_SIZE", 20f)
        tvLyrics.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentTextSizeSp)

        // --- NAVIGATION SETUP ---
        // 1. Get the list of saved Aartis (in correct order)
        aartiList = AartiStorage.getSavedAartis(this)

        // 2. Find which Aarti was clicked
        val initialName = intent.getStringExtra("AARTI_NAME") ?: ""
        currentIndex = aartiList.indexOf(initialName)

        // 3. Load the data
        if (currentIndex != -1) {
            loadAartiData(currentIndex)
        } else {
            // Fallback (Safe mode if something goes wrong)
            tvTitle.text = initialName
            tvLyrics.text = AartiRepository.getAartiLyrics(initialName)
            btnPrev.visibility = View.GONE
            btnNext.visibility = View.GONE
        }

        // --- BUTTON CLICKS ---
        btnPrev.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                loadAartiData(currentIndex)
                scrollView.scrollTo(0, 0) // Scroll to top
            }
        }

        btnNext.setOnClickListener {
            if (currentIndex < aartiList.size - 1) {
                currentIndex++
                loadAartiData(currentIndex)
                scrollView.scrollTo(0, 0) // Scroll to top
            }
        }

        btnBack.setOnClickListener {
            finish()
        }

        initializeZoom()
    }

    // --- HELPER FUNCTION TO UPDATE SCREEN ---
    private fun loadAartiData(index: Int) {
        val name = aartiList[index]

        // Update Text
        tvTitle.text = name
        tvLyrics.text = AartiRepository.getAartiLyrics(name)

        // Update Buttons (Show/Hide/Fade)
        val btnPrev = findViewById<TextView>(R.id.btnPrev)
        val btnNext = findViewById<TextView>(R.id.btnNext)

        // Disable "Prev" if at start
        if (index == 0) {
            btnPrev.alpha = 0.3f
            btnPrev.isEnabled = false
        } else {
            btnPrev.alpha = 1.0f
            btnPrev.isEnabled = true
        }

        // Disable "Next" if at end
        if (index == aartiList.size - 1) {
            btnNext.alpha = 0.3f
            btnNext.isEnabled = false
        } else {
            btnNext.alpha = 1.0f
            btnNext.isEnabled = true
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