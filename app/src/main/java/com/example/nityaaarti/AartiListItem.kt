package com.example.nityaaarti

data class AartiListItem(
    val type: Int,       // 0 for Header, 1 for Aarti
    val name: String     // The text to display
) {
    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_AARTI = 1
    }
}