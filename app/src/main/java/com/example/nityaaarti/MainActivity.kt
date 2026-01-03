package com.example.nityaaarti

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, AddAartiActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadSavedAartis()
    }

    private fun loadSavedAartis() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAartis)
        val emptyState = findViewById<LinearLayout>(R.id.emptyStateLayout)

        val savedList = AartiStorage.getSavedAartis(this)

        if (savedList.isNotEmpty()) {
            // SHOW LIST
            emptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = HomeAdapter(savedList) { selectedName ->
                // Open Read Screen when clicked
                val intent = Intent(this, ReadAartiActivity::class.java)
                intent.putExtra("AARTI_NAME", selectedName)
                startActivity(intent)
            }
        } else {
            emptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }
}

class HomeAdapter(
    private val aartiList: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvAartiName)
        val btnAdd: ImageView = view.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_aarti_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val name = aartiList[position]
        holder.tvName.text = name
        holder.btnAdd.visibility = View.GONE
        holder.itemView.setOnClickListener {
            onClick(name)
        }
    }

    override fun getItemCount() = aartiList.size
}