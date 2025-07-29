package com.example.taskmanagementapp.data.remote.model

import com.example.taskmanagementapp.domain.model.Quote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteResponse(
    @SerialName("q") val content: String,
    @SerialName("a") val author: String,
    @SerialName("h") val html: String? = null
) {
    fun toDomain(): Quote {
        return Quote(
            content = content,
            author = author
        )
    }
}