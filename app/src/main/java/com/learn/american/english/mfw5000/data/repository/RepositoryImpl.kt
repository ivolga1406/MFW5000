package com.learn.american.english.mfw5000.data.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.snapshots
import com.learn.american.english.mfw5000.Constants.NUMBER
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word
import com.learn.american.english.mfw5000.ui.theme.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    @Named("WordsRef") private val wordsRef: CollectionReference
//    @Named("BooksRef") private val booksRef: CollectionReference,
//    @Named("ChaptersRef") private val chaptersRef: CollectionReference
): Repository {

//    override fun getWordsFromFirestore(): Flow<Response<List<Word>>> {
//        return wordsRef.orderBy(NUMBER).snapshots().map { snapshot ->
//            if (!snapshot.isEmpty) {
//                val series = snapshot.documents.mapNotNull { document ->
//                    document.toObject(Word::class.java)?.apply {
//                        id = document.id
//                    }
//                }
//                Response.Success(series)
//            } else {
//                Response.Failure(Exception("No data found"))
//            }
//        }
//    }

    override fun getWordsFromFirestore(): Flow<Response<List<Word>>> {
        return wordsRef.orderBy(NUMBER).limit(400).snapshots().map { snapshot ->
            if (!snapshot.isEmpty) {
                val books = snapshot.documents.mapNotNull { document ->
                    document.toObject(Word::class.java)?.apply {
                        id = document.id
                    }
                }
                Log.d("FirestoreData", "Retrieved documents: ${books.size}") // Log the size of retrieved documents
                Response.Success(books)
            } else {
                Log.d("FirestoreData", "No data found") // Log when no data is found
                Response.Failure(Exception("No data found"))
            }
        }
    }

}



