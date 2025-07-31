package com.taskmanager.app.data.remote.model

import com.taskmanager.app.domain.model.Quote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteResponse(
    val content: String,
    val author: String,
    @SerialName("tags") val tags: List<String> = emptyList()
) {
    fun toDomain(): Quote {
        return Quote(
            content = content,
            author = author
        )
    }
}
