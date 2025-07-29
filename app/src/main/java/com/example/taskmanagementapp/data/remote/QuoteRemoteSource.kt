package com.example.taskmanagementapp.data.remote

import com.example.taskmanagementapp.data.remote.api.QuoteService
import com.example.taskmanagementapp.domain.model.Quote

class QuoteRemoteSource(
    private val quoteService: QuoteService
) {
    suspend fun fetchRandomQuote(): Quote {
        val quotes = quoteService.getRandomQuote()
        return quotes.first().toDomain()
    }
}