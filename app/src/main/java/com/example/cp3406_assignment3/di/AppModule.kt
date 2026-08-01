package com.example.cp3406_assignment3.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cp3406_assignment3.data.local.SampleData
import com.example.cp3406_assignment3.data.local.StudyBuddyDatabase
import com.example.cp3406_assignment3.data.local.dao.FlashcardDao
import com.example.cp3406_assignment3.data.local.dao.QuizResultDao
import com.example.cp3406_assignment3.data.local.dao.UserStatsDao
import com.example.cp3406_assignment3.data.remote.DictionaryApiService
import com.example.cp3406_assignment3.data.remote.TriviaApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Provider
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TriviaRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DictionaryRetrofit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext ctx: Context,
        // Provider defers resolution until the callback actually fires, avoiding a
        // circular dependency between the database and its own DAOs at construction time.
        dbProvider: Provider<StudyBuddyDatabase>
    ): StudyBuddyDatabase =
        Room.databaseBuilder(ctx, StudyBuddyDatabase::class.java, "studybuddy.db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Runs exactly once: the first time the .db file is created on device.
                    CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                        dbProvider.get().flashcardDao().insertAll(SampleData.flashcards)
                    }
                }
            })
            .build()

    @Provides fun provideFlashcardDao(db: StudyBuddyDatabase): FlashcardDao = db.flashcardDao()
    @Provides fun provideQuizResultDao(db: StudyBuddyDatabase): QuizResultDao = db.quizResultDao()
    @Provides fun provideUserStatsDao(db: StudyBuddyDatabase): UserStatsDao = db.userStatsDao()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()) // required for Moshi to parse Kotlin data classes correctly
        .build()

    @Provides
    @Singleton
    @TriviaRetrofit
    fun provideTriviaRetrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    @DictionaryRetrofit
    fun provideDictionaryRetrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.dictionaryapi.dev/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideTriviaApi(@TriviaRetrofit retrofit: Retrofit): TriviaApiService =
        retrofit.create(TriviaApiService::class.java)

    @Provides
    @Singleton
    fun provideDictionaryApi(@DictionaryRetrofit retrofit: Retrofit): DictionaryApiService =
        retrofit.create(DictionaryApiService::class.java)

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext ctx: Context) =
        com.example.cp3406_assignment3.data.UserPreferences(ctx)
}
