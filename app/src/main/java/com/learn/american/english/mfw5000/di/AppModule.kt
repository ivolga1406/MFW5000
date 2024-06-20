package com.learn.american.english.mfw5000.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.learn.american.english.mfw5000.Constants.WORDS
import com.learn.american.english.mfw5000.data.repository.RepositoryImpl
import com.learn.american.english.mfw5000.ui.theme.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
//    @Provides
//    fun provideSharedPreferencesHelper(@ApplicationContext context: Context): SharedPreferencesHelper {
//        return SharedPreferencesHelper(context)
//    }

    @Provides
    @Named("WordsRef")
    fun provideWordsRef() = Firebase.firestore.collection(WORDS)

    @Provides
    fun provideRepository(
        @Named("WordsRef") wordsRef: CollectionReference
    ): Repository = RepositoryImpl(wordsRef)
}