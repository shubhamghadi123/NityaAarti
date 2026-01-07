package com.example.nityaaarti

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Collections
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity() {

    private var aartiList = mutableListOf<String>()
    private lateinit var adapter: HomeAdapter
    // 1. Declare helper here so we can use it in the Adapter callback
    private lateinit var itemTouchHelper: ItemTouchHelper

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

        aartiList = AartiStorage.getSavedAartis(this)

        if (aartiList.isNotEmpty()) {
            emptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.layoutManager = LinearLayoutManager(this)

            // 2. Initialize Adapter with ALL 4 arguments
            adapter = HomeAdapter(
                aartiList = aartiList,
                onReadClick = { name ->
                    val intent = Intent(this, ReadAartiActivity::class.java)
                    intent.putExtra("AARTI_NAME", name)
                    startActivity(intent)
                },
                onDeleteClick = { name ->
                    AartiStorage.removeAarti(this, name)
                    loadSavedAartis()
                },
                onStartDrag = { viewHolder ->
                    // This starts the drag immediately when handle is touched
                    itemTouchHelper.startDrag(viewHolder)
                }
            )
            recyclerView.adapter = adapter

            setupDragAndDrop(recyclerView)

        } else {
            emptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }

    private fun setupDragAndDrop(recyclerView: RecyclerView) {
        val callback = object : ItemTouchHelper.Callback() {

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                // Enable Up and Down dragging
                return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                Collections.swap(aartiList, fromPos, toPos)
                adapter.notifyItemMoved(fromPos, toPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { /* Not used */ }

            // 3. Highlight Logic: Change color when picked up
            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    // Darker Orange when dragging
                    if (viewHolder?.itemView is CardView) {
                        (viewHolder.itemView as CardView).setCardBackgroundColor("#FFE0B2".toColorInt())
                    }
                }
            }

            // 4. Restore Color: Reset when dropped
            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                AartiStorage.saveAartiList(this@MainActivity, aartiList)

                // Back to Light Orange
                if (viewHolder.itemView is CardView) {
                    (viewHolder.itemView as CardView).setCardBackgroundColor("#FFF3E0".toColorInt())
                }
            }
        }

        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}

// --- FIXED ADAPTER ---
class HomeAdapter(
    private val aartiList: List<String>,
    private val onReadClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit,
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvAartiName)
        val btnAction: ImageView = view.findViewById(R.id.btnAdd)
        val imgDrag: ImageView = view.findViewById(R.id.imgDragHandle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_aarti_row, parent, false)
        return HomeViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val name = aartiList[position]
        holder.tvName.text = name

        holder.btnAction.setImageResource(R.drawable.round_delete_forever_24)
        holder.btnAction.setColorFilter(Color.RED)

        // --- FIXED DELETE LOGIC ---
        holder.btnAction.setOnClickListener {
            onDeleteClick(name)
        }

        // Read Click
        holder.itemView.setOnClickListener {
            onReadClick(name)
        }

        // Drag Handle Logic
        holder.imgDrag.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                onStartDrag(holder)
            }
            false
        }
    }

    override fun getItemCount() = aartiList.size
}