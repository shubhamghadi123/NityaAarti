package com.example.nityaaarti

import android.content.Intent
import android.graphics.Color
import android.widget.Toast
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
            emptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            recyclerView.layoutManager = LinearLayoutManager(this)

            // UPDATED ADAPTER INITIALIZATION
            recyclerView.adapter = HomeAdapter(
                aartiList = savedList,
                onReadClick = { selectedName ->
                    // Open Read Screen
                    val intent = Intent(this, ReadAartiActivity::class.java)
                    intent.putExtra("AARTI_NAME", selectedName)
                    startActivity(intent)
                },
                onDeleteClick = { nameToDelete ->
                    AartiStorage.removeAarti(this, nameToDelete)
                    loadSavedAartis()
                    Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            emptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }
}

class HomeAdapter(
    private val aartiList: List<String>,
    private val onReadClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvAartiName)
        val btnAction: ImageView = view.findViewById(R.id.btnAdd) // We reuse the existing ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_aarti_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val name = aartiList[position]
        holder.tvName.text = name

        holder.btnAction.visibility = View.VISIBLE
        holder.btnAction.setImageResource(R.drawable.round_delete_forever_24)
        holder.btnAction.setColorFilter(Color.RED)

        holder.btnAction.setOnClickListener {
            onDeleteClick(name)
        }

        holder.itemView.setOnClickListener {
            onReadClick(name)
        }
    }

    override fun getItemCount() = aartiList.size
}