package com.example.taskmanagementapp.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.taskmanagementapp.data.local.TaskDatabase
import com.example.taskmanagementapp.data.remote.QuoteRemoteSource
import com.example.taskmanagementapp.data.remote.api.QuoteService
import com.example.taskmanagementapp.data.repository.TaskRepositoryImpl
import com.example.taskmanagementapp.domain.repository.TaskRepository
import com.example.taskmanagementapp.domain.usecase.*
import com.example.taskmanagementapp.presentation.viewmodel.TaskViewModelFactory
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object AppModule {

    private lateinit var taskRepository: TaskRepository
    lateinit var taskUseCases: TaskUseCases
    lateinit var viewModelFactory: ViewModelProvider.Factory

    fun init(context: Context) {
        val database = provideDatabase(context)
        val api = provideQuoteApi()
        val quoteRemoteSource = QuoteRemoteSource(api)

        taskRepository = TaskRepositoryImpl(
            dao = database.taskDao(),
            quoteRemoteSource = quoteRemoteSource
        )

        taskUseCases = provideUseCases(taskRepository)

        viewModelFactory = TaskViewModelFactory(taskUseCases)
    }

    private fun provideDatabase(context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            "task_db"
        ).build()
    }

    private fun provideQuoteApi(): QuoteService {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        // Add logging interceptor for debugging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://zenquotes.io/api/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(QuoteService::class.java)
    }

    private fun provideUseCases(repo: TaskRepository): TaskUseCases {
        return TaskUseCases(
            addTask = AddTask(repo),
            updateTask = UpdateTask(repo),
            deleteTask = DeleteTask(repo),
            getAllTasks = GetAllTasks(repo),
            getTaskById = GetTaskById(repo),
            getQuote = GetQuote(repo),
            getCategories = GetCategories(repo),
            filterTasks = FilterTasks(repo)
        )
    }
}