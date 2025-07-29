package com.example.taskmanagementapp.data.remote.model

import com.example.taskmanagementapp.domain.model.Quote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteResponse(
    @SerialName("content") val content: String,
    @SerialName("author") val author: String
) {
    fun toDomain(): Quote {
        return Quote(
            content = content,
            author = author
        )
    }
}
