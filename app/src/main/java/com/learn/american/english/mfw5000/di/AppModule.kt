package com.learn.american.english.mfw5000.di

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.learn.american.english.mfw5000.Constants.WORDS
import com.learn.american.english.mfw5000.data.repository.RepositoryImpl
import com.learn.american.english.mfw5000.viewModel.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("WordsRef")
    fun provideWordsRef() = Firebase.firestore.collection(WORDS)

    @Provides
    @Singleton
    fun provideRepository(
        @Named("WordsRef") wordsRef: CollectionReference,
        @ApplicationContext context: Context
    ): Repository = RepositoryImpl(wordsRef, context)
}
