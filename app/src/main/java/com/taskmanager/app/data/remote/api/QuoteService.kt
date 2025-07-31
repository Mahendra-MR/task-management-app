package com.taskmanager.app.data.remote.api

import com.taskmanager.app.data.remote.model.QuoteResponse
import retrofit2.http.GET

interface QuoteService {
    @GET("random")
    suspend fun getRandomQuote(): QuoteResponse
}