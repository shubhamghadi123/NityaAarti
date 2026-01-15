package com.primelabs.nityaaarti

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
import android.widget.Toast
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
    private lateinit var itemTouchHelper: ItemTouchHelper

    // --- 1. STATE VARIABLES FOR MODES ---
    private var isSortActive = false
    private var isDeleteActive = false

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

        // --- 2. SETUP MODE BUTTONS ---
        val btnSort = findViewById<ImageView>(R.id.btnToggleSort)
        val btnDelete = findViewById<ImageView>(R.id.btnToggleDelete)

        btnSort.setOnClickListener {
            // Toggle Sort, Disable Delete
            isSortActive = !isSortActive
            isDeleteActive = false
            updateUIModes(btnSort, btnDelete)
            if (isSortActive) {
                Toast.makeText(this, "Sort Mode ON: Long press to drag rows", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Sort Mode OFF", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            // Toggle Delete, Disable Sort
            isDeleteActive = !isDeleteActive
            isSortActive = false
            updateUIModes(btnSort, btnDelete)
            if (isDeleteActive) {
                Toast.makeText(this, "Delete Mode ON", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Delete Mode OFF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadSavedAartis()
    }

    private fun updateUIModes(btnSort: ImageView, btnDelete: ImageView) {
        // Update Adapter if it exists
        if (::adapter.isInitialized) {
            adapter.updateModes(isSortActive, isDeleteActive)
        }

        // Visual Feedback for Buttons (Highlight active one)
        if (isSortActive) btnSort.alpha = 1.0f else btnSort.alpha = 0.5f
        if (isDeleteActive) btnDelete.alpha = 1.0f else btnDelete.alpha = 0.5f
    }

    private fun loadSavedAartis() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAartis)
        val emptyState = findViewById<LinearLayout>(R.id.emptyStateLayout)
        val btnSort = findViewById<ImageView>(R.id.btnToggleSort)
        val btnDelete = findViewById<ImageView>(R.id.btnToggleDelete)

        aartiList = AartiStorage.getSavedAartis(this)

        if (aartiList.isNotEmpty()) {
            emptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.layoutManager = LinearLayoutManager(this)

            adapter = HomeAdapter(
                aartiList = aartiList,
                onReadClick = { name ->
                    val intent = Intent(this, ReadAartiActivity::class.java)
                    intent.putExtra("AARTI_NAME", name)
                    startActivity(intent)
                },
                onDeleteClick = { name ->
                    AartiStorage.removeAarti(this, name)
                    loadSavedAartis() // Refresh immediately
                },
                onStartDrag = { viewHolder ->
                    itemTouchHelper.startDrag(viewHolder)
                }
            )
            recyclerView.adapter = adapter

            // --- 4. APPLY CURRENT MODE ON LOAD ---
            adapter.updateModes(isSortActive, isDeleteActive)
            updateUIModes(btnSort, btnDelete) // Keep button opacity correct

            setupDragAndDrop(recyclerView)

        } else {
            emptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }

    private fun setupDragAndDrop(recyclerView: RecyclerView) {
        val callback = object : ItemTouchHelper.Callback() {

            override fun isLongPressDragEnabled(): Boolean {
                return false
            }

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                if (!isSortActive) {
                    return makeMovementFlags(0, 0)
                }
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

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    if (viewHolder?.itemView is CardView) {
                        (viewHolder.itemView as CardView).setCardBackgroundColor("#FFE0B2".toColorInt())
                    }
                }
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                AartiStorage.saveAartiList(this@MainActivity, aartiList)
                if (viewHolder.itemView is CardView) {
                    (viewHolder.itemView as CardView).setCardBackgroundColor("#FFF3E0".toColorInt())
                }
            }
        }

        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}

class HomeAdapter(
    private val aartiList: List<String>,
    private val onReadClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit,
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    var isSortMode = false
    var isDeleteMode = false

    fun updateModes(sort: Boolean, delete: Boolean) {
        isSortMode = sort
        isDeleteMode = delete
        notifyDataSetChanged()
    }

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

        if (holder.itemView is CardView) {
            (holder.itemView as CardView).setCardBackgroundColor("#FFD8BF".toColorInt())
        }

        holder.btnAction.setImageResource(R.drawable.round_delete_forever_24)
        holder.btnAction.setColorFilter(Color.RED)

        if (isDeleteMode) {
            holder.btnAction.visibility = View.VISIBLE
            holder.btnAction.setOnClickListener { onDeleteClick(name) }
        } else {
            holder.btnAction.visibility = View.GONE
        }

        if (isSortMode) {
            holder.imgDrag.visibility = View.VISIBLE
            holder.imgDrag.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    onStartDrag(holder)
                }
                false
            }
        } else {
            holder.imgDrag.visibility = View.GONE
        }

        when {
            isSortMode -> {
                holder.itemView.setOnClickListener(null)
                holder.itemView.setOnLongClickListener {
                    onStartDrag(holder)
                    true
                }
                holder.itemView.setOnTouchListener(null)
            }
            isDeleteMode -> {
                holder.itemView.setOnClickListener(null)
                holder.itemView.setOnLongClickListener(null)
                holder.itemView.isClickable = false
            }
            else -> {
                holder.itemView.setOnLongClickListener(null)
                holder.itemView.setOnClickListener {
                    onReadClick(name)
                }
                holder.itemView.isClickable = true
            }
        }
    }

    override fun getItemCount() = aartiList.size
}