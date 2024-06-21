// File: C:\Users\DellNotebookUser\AndroidStudioProjects\MFW5000\app\src\main\java\com\learn\american\english\mfw5000\data\repository\RepositoryImpl.kt

package com.learn.american.english.mfw5000.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word
import com.learn.american.english.mfw5000.ui.theme.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
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

            val jpgDir = File(localDir, "jpg")
            val mp3Dir = File(localDir, "mp3")

            if (!jpgDir.exists()) jpgDir.mkdirs()
            if (!mp3Dir.exists()) mp3Dir.mkdirs()

            val jpgZipRef = storage.reference.child("5000_words/all_jpg.zip")
            val mp3ZipRef = storage.reference.child("5000_words/all_mp3.zip")

            downloadAndExtractZip(jpgZipRef, jpgDir)
            downloadAndExtractZip(mp3ZipRef, mp3Dir)

            emit(Response.Success(Unit))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    private suspend fun downloadAndExtractZip(zipRef: StorageReference, outputDir: File) {
        val localZipFile = File(outputDir, zipRef.name)
        zipRef.getFile(localZipFile).await()
        unzip(localZipFile, outputDir)
    }

    private fun unzip(zipFile: File, targetDirectory: File) {
        ZipInputStream(zipFile.inputStream()).use { zis ->
            var entry: ZipEntry?
            while (zis.nextEntry.also { entry = it } != null) {
                val newFile = File(targetDirectory, entry!!.name)
                if (entry!!.isDirectory) {
                    newFile.mkdirs()
                } else {
                    newFile.parentFile?.mkdirs()
                    FileOutputStream(newFile).use { fos ->
                        zis.copyTo(fos)
                    }
                }
            }
        }
        zipFile.delete() // Optionally delete the zip file after extraction
    }
}
