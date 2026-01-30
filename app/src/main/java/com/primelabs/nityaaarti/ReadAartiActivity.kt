package com.primelabs.nityaaarti

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
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

        tvTitle = findViewById(R.id.tvTitle)
        tvLyrics = findViewById(R.id.tvLyrics)
        scrollView = findViewById(R.id.scrollView)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnPrev = findViewById<TextView>(R.id.btnPrev)
        val btnNext = findViewById<TextView>(R.id.btnNext)
        val bottomBar = findViewById<LinearLayout>(R.id.bottomBar)
        val overlayTip = findViewById<FrameLayout>(R.id.overlayTip)

        prefs = getSharedPreferences("AartiSettings", Context.MODE_PRIVATE)
        currentTextSizeSp = prefs.getFloat("FONT_SIZE", 20f)

        tvLyrics.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentTextSizeSp)

        val tipShowCount = prefs.getInt("TIP_SHOW_COUNT", 0)

        if (tipShowCount < 5) {
            overlayTip.visibility = View.VISIBLE

            prefs.edit { putInt("TIP_SHOW_COUNT", tipShowCount + 1) }

            overlayTip.setOnClickListener {
                overlayTip.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        overlayTip.visibility = View.GONE
                    }
            }
        } else {
            overlayTip.visibility = View.GONE
        }

        val shouldHideNav = intent.getBooleanExtra("HIDE_NAV", false)
        if (shouldHideNav) {
            bottomBar.visibility = View.GONE
        } else {
            bottomBar.visibility = View.VISIBLE
        }

        aartiList = AartiStorage.getSavedAartis(this)
        val initialName = intent.getStringExtra("AARTI_NAME") ?: ""
        currentIndex = aartiList.indexOf(initialName)

        if (currentIndex != -1) {
            loadAartiData(currentIndex)
        } else {
            tvTitle.text = initialName
            tvLyrics.text = AartiRepository.getAartiLyrics(initialName)
            btnPrev.visibility = View.GONE
            btnNext.visibility = View.GONE
        }

        btnPrev.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                loadAartiData(currentIndex)
                scrollView.scrollTo(0, 0)
            }
        }

        btnNext.setOnClickListener {
            if (currentIndex < aartiList.size - 1) {
                currentIndex++
                loadAartiData(currentIndex)
                scrollView.scrollTo(0, 0)
            }
        }

        btnBack.setOnClickListener {
            finish()
        }

        initializeZoom()
    }

    private fun loadAartiData(index: Int) {
        val name = aartiList[index]
        tvTitle.text = name
        tvLyrics.text = AartiRepository.getAartiLyrics(name)

        val btnPrev = findViewById<TextView>(R.id.btnPrev)
        val btnNext = findViewById<TextView>(R.id.btnNext)

        if (index == 0) {
            btnPrev.alpha = 0.3f
            btnPrev.isEnabled = false
        } else {
            btnPrev.alpha = 1.0f
            btnPrev.isEnabled = true
        }

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