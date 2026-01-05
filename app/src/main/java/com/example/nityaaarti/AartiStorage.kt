package com.example.nityaaarti

import android.content.Context
import androidx.core.content.edit

object AartiStorage {
    private const val PREF_NAME = "MyAartiPrefs"
    private const val KEY_AARTIS = "saved_aartis"

    // Save an Aarti to the list
    fun addAarti(context: Context, aartiName: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val currentSet = prefs.getStringSet(KEY_AARTIS, mutableSetOf()) ?: mutableSetOf()
        val newSet = HashSet(currentSet)
        newSet.add(aartiName)
        prefs.edit().putStringSet(KEY_AARTIS, newSet).apply()
    }

    fun getSavedAartis(context: Context): List<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val set = prefs.getStringSet(KEY_AARTIS, emptySet()) ?: emptySet()
        return set.toList().sorted() // Return sorted list
    }

    fun removeAarti(context: Context, aartiName: String) {
        val prefs = context.getSharedPreferences("MyAartiPrefs", Context.MODE_PRIVATE)
        val currentSet = prefs.getStringSet("saved_aartis", mutableSetOf()) ?: mutableSetOf()

        val newSet = HashSet(currentSet)
        if (newSet.contains(aartiName)) {
            newSet.remove(aartiName)
            prefs.edit { putStringSet("saved_aartis", newSet) }
        }
    }
}