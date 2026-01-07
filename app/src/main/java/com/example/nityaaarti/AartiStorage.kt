package com.example.nityaaarti

import android.content.Context
import androidx.core.content.edit

object AartiStorage {
    private const val PREF_NAME = "MyAartiPrefs"
    private const val KEY_AARTIS_ORDER = "saved_aartis_ordered"

    // Save the entire list in its current order
    fun saveAartiList(context: Context, list: List<String>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        // Join list into a single string "Aarti1,Aarti2,Aarti3"
        val serialized = list.joinToString(",")
        prefs.edit { putString(KEY_AARTIS_ORDER, serialized) }
    }

    // Get the list in the exact saved order
    fun getSavedAartis(context: Context): MutableList<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val serialized = prefs.getString(KEY_AARTIS_ORDER, "") ?: ""

        if (serialized.isEmpty()) return mutableListOf()

        // Convert string back to list
        return serialized.split(",").toMutableList()
    }

    // Add a single item to the end
    fun addAarti(context: Context, aartiName: String) {
        val currentList = getSavedAartis(context)
        if (!currentList.contains(aartiName)) {
            currentList.add(aartiName)
            saveAartiList(context, currentList)
        }
    }

    // Remove an item
    fun removeAarti(context: Context, aartiName: String) {
        val currentList = getSavedAartis(context)
        if (currentList.remove(aartiName)) {
            saveAartiList(context, currentList)
        }
    }
}