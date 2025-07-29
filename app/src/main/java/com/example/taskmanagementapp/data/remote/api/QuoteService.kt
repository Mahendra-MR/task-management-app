package com.example.taskmanagementapp.data.remote.api

import com.example.taskmanagementapp.data.remote.model.QuoteResponse
import retrofit2.http.GET

interface QuoteService {
    @GET("random")
    suspend fun getRandomQuote(): QuoteResponse
}