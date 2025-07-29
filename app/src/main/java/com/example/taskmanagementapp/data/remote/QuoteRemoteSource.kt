package com.example.taskmanagementapp.data.remote

import com.example.taskmanagementapp.data.remote.api.QuoteService
import com.example.taskmanagementapp.domain.model.Quote

class QuoteRemoteSource(
    private val quoteService: QuoteService
) {
    suspend fun fetchRandomQuote(): Quote {
        return quoteService.getRandomQuote().toDomain()
    }
}
