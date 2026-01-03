package com.example.nityaaarti

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ReadAartiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_read_aarti)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val aartiName = intent.getStringExtra("AARTI_NAME") ?: "Aarti"

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvLyrics = findViewById<TextView>(R.id.tvLyrics)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        tvTitle.text = aartiName

        tvLyrics.text = AartiRepository.getAartiLyrics(aartiName)

        btnBack.setOnClickListener {
            finish()
        }
    }
}