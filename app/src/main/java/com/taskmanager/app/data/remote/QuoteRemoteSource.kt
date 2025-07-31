package com.taskmanager.app.data.remote

import com.taskmanager.app.data.remote.api.QuoteService
import com.taskmanager.app.domain.model.Quote

class QuoteRemoteSource(
    private val quoteService: QuoteService
) {
    suspend fun fetchRandomQuote(): Quote {
        val quoteResponse = quoteService.getRandomQuote()
        return quoteResponse.toDomain()
    }
}
