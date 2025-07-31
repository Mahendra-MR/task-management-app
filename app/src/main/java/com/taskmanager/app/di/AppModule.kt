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
import com.taskmanager.app.BuildConfig

object AppModule {

    private var isInitialized = false

    // Lazy initialization for better performance
    private val database by lazy { provideDatabase(context!!) }
    private val api by lazy { provideQuoteApi() }
    private val quoteRemoteSource by lazy { QuoteRemoteSource(api) }

    private lateinit var taskRepository: TaskRepository
    private var context: Context? = null

    lateinit var taskUseCases: TaskUseCases
        private set

    lateinit var viewModelFactory: ViewModelProvider.Factory
        private set

    fun init(context: Context) {
        if (isInitialized) return

        this.context = context.applicationContext

        // Initialize repository with lazy-loaded dependencies
        taskRepository = TaskRepositoryImpl(
            taskDao = database.taskDao(),
            categoryDao = database.categoryDao(),
            quoteRemoteSource = quoteRemoteSource,
            quoteDao = database.quoteDao()
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
            // Add database optimizations
            .setJournalMode(androidx.room.RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .enableMultiInstanceInvalidation()
            .build()
    }

    private fun provideQuoteApi(): QuoteService {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true // Handle null values gracefully
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Use BASIC level in production for better performance
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            }
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            // Optimize timeouts for better performance
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            // Add connection pooling
            .connectionPool(okhttp3.ConnectionPool(5, 5, TimeUnit.MINUTES))
            // Enable response caching
            .cache(okhttp3.Cache(context!!.cacheDir, 10 * 1024 * 1024)) // 10MB cache
            .build()

        return Retrofit.Builder()
            .baseUrl("https://quotable-proxy.onrender.com/")
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