package com.taskmanager.app.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.taskmanager.app.data.local.MIGRATION_1_2
import com.taskmanager.app.data.local.MIGRATION_2_3
import com.taskmanager.app.data.local.TaskDatabase
import com.taskmanager.app.data.remote.QuoteRemoteSource
import com.taskmanager.app.data.remote.api.QuoteService
import com.taskmanager.app.data.repository.TaskRepositoryImpl
import com.taskmanager.app.domain.repository.TaskRepository
import com.taskmanager.app.domain.usecase.*
import com.taskmanager.app.presentation.viewmodel.TaskViewModelFactory
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object AppModule {

    private var isInitialized = false

    private lateinit var taskRepository: TaskRepository
    lateinit var taskUseCases: TaskUseCases
        private set

    lateinit var viewModelFactory: ViewModelProvider.Factory
        private set

    fun init(context: Context) {
        if (isInitialized) return

        val database = provideDatabase(context)
        val api = provideQuoteApi()
        val quoteRemoteSource = QuoteRemoteSource(api)

        val quoteDao = database.quoteDao()

        taskRepository = TaskRepositoryImpl(
            taskDao = database.taskDao(),
            categoryDao = database.categoryDao(),
            quoteRemoteSource = quoteRemoteSource,
            quoteDao = quoteDao // Injected here
        )

        taskUseCases = provideUseCases(taskRepository)
        viewModelFactory = TaskViewModelFactory(taskUseCases)

        isInitialized = true
    }

    private fun provideDatabase(context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TaskDatabase::class.java,
            "task_db"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    private fun provideQuoteApi(): QuoteService {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

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
            .baseUrl("https://quotable-proxy.onrender.com/") //Render Proxy URL
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
            filterTasks = FilterTasks(repo),
            addCategory = AddCategory(repo),
            deleteCategory = DeleteCategory(repo),
            updateCategory = UpdateCategory(repo)
        )
    }
}