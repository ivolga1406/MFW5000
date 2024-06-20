package com.learn.american.english.mfw5000.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word
import com.learn.american.english.mfw5000.ui.theme.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    @Named("WordsRef") private val wordsRef: CollectionReference
) : Repository {

    override fun getWordsFromFirestore(start: Int, end: Int): Flow<Response<List<Word>>> {
        return flow {
            emit(Response.Loading)
            try {
                val snapshot = wordsRef.orderBy("number")
                    .whereGreaterThanOrEqualTo("number", start)
                    .whereLessThanOrEqualTo("number", end)
                    .get().await()
                if (!snapshot.isEmpty) {
                    val words = snapshot.documents.mapNotNull { document ->
                        document.toObject<Word>()?.apply { id = document.id }
                    }
                    emit(Response.Success(words))
                } else {
                    emit(Response.Failure(Exception("No data found")))
                }
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
        }
    }

    override fun getWordById(wordId: String): Flow<Response<Word>> = flow {
        emit(Response.Loading)
        try {
            val documentSnapshot = wordsRef.document(wordId).get().await()
            val word = documentSnapshot.toObject<Word>()
            if (word != null) {
                emit(Response.Success(word))
            } else {
                emit(Response.Failure(Exception("Word not found")))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun downloadAllMedia(context: Context): Flow<Response<Unit>> = flow {
        emit(Response.Loading)
        try {
            val auth = FirebaseAuth.getInstance()
            if (auth.currentUser == null) {
                auth.signInAnonymously().await()
            }

            val storage = FirebaseStorage.getInstance()
            val localDir = context.filesDir

            val jpgFiles = storage.reference.child("5000_words/jpg").listAll().await()
            for (file in jpgFiles.items) {
                val localFile = File(localDir, "${file.name}.jpg")
                file.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("RepositoryImpl", "Downloading image: ${file.name}")
                    uri.let { url ->
                        file.getFile(localFile).addOnSuccessListener {
                            Log.d("RepositoryImpl", "Downloaded image: ${file.name}")
                        }.addOnFailureListener { e ->
                            Log.e("RepositoryImpl", "Failed to download image: ${file.name}", e)
                        }
                    }
                }.addOnFailureListener { e ->
                    Log.e("RepositoryImpl", "Failed to get download URL for image: ${file.name}", e)
                }
            }

            val mp3Files = storage.reference.child("5000_words/mp3").listAll().await()
            for (file in mp3Files.items) {
                val localFile = File(localDir, "${file.name}.mp3")
                file.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("RepositoryImpl", "Downloading audio: ${file.name}")
                    uri.let { url ->
                        file.getFile(localFile).addOnSuccessListener {
                            Log.d("RepositoryImpl", "Downloaded audio: ${file.name}")
                        }.addOnFailureListener { e ->
                            Log.e("RepositoryImpl", "Failed to download audio: ${file.name}", e)
                        }
                    }
                }.addOnFailureListener { e ->
                    Log.e("RepositoryImpl", "Failed to get download URL for audio: ${file.name}", e)
                }
            }

            emit(Response.Success(Unit))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }
}
