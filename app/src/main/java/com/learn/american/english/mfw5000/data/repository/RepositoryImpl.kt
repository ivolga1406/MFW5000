package com.learn.american.english.mfw5000.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word
import com.learn.american.english.mfw5000.viewModel.Repository
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
    @Named("WordsRef") private val wordsRef: CollectionReference,
    context: Context
) : Repository {

    private val wordsCache = mutableMapOf<Int, MutableList<Word>>()
    private val collectionCounters = mutableMapOf<Int, Int>()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("words_cache", Context.MODE_PRIVATE)
    private val gson: Gson = GsonBuilder().serializeNulls().create()

    init {
        loadCache()
        loadCounters()
    }

    private fun loadCache() {
        val cachedJson = sharedPreferences.getString("wordsCache", null)
        if (!cachedJson.isNullOrEmpty()) {
            val type = object : TypeToken<MutableMap<Int, MutableList<Word>>>() {}.type
            try {
                val cachedData: MutableMap<Int, MutableList<Word>> = gson.fromJson(cachedJson, type)
                wordsCache.putAll(cachedData)
            } catch (e: Exception) {
                e.printStackTrace() // Handle the exception as needed
            }
        }
    }

    private fun loadCounters() {
        val countersJson = sharedPreferences.getString("collectionCounters", null)
        if (!countersJson.isNullOrEmpty()) {
            val type = object : TypeToken<MutableMap<Int, Int>>() {}.type
            try {
                val cachedCounters: MutableMap<Int, Int> = gson.fromJson(countersJson, type)
                collectionCounters.putAll(cachedCounters)
            } catch (e: Exception) {
                e.printStackTrace() // Handle the exception as needed
            }
        }
    }

    private fun saveCache() {
        val editor = sharedPreferences.edit()
        try {
            val type = object : TypeToken<MutableMap<Int, MutableList<Word>>>() {}.type
            val json = gson.toJson(wordsCache, type)
            editor.putString("wordsCache", json)
            editor.apply()
        } catch (e: Exception) {
            Log.e("RepositoryImpl", "Error serializing wordsCache: ${e.message}", e)
        }
    }

    private fun saveCounters() {
        val editor = sharedPreferences.edit()
        try {
            val type = object : TypeToken<MutableMap<Int, Int>>() {}.type
            val json = gson.toJson(collectionCounters, type)
            editor.putString("collectionCounters", json)
            editor.apply()
        } catch (e: Exception) {
            Log.e("RepositoryImpl", "Error serializing collectionCounters: ${e.message}", e)
        }
    }

    override fun getWordsCollection(collectionNumber: Int): Flow<Response<List<Word>>> {
        return flow {
            emit(Response.Loading)
            try {
                val cachedWords = wordsCache[collectionNumber]
                if (!cachedWords.isNullOrEmpty()) {
                    emit(Response.Success(cachedWords))
                } else {
                    val start = collectionNumber * 50 + 1
                    val end = (collectionNumber + 1) * 50
                    val snapshot = wordsRef.orderBy("number")
                        .whereGreaterThanOrEqualTo("number", start)
                        .whereLessThanOrEqualTo("number", end)
                        .get().await()
                    if (!snapshot.isEmpty) {
                        val words = snapshot.documents.mapNotNull { document ->
                            document.toObject<Word>()?.apply { id = document.id }
                        }
                        wordsCache[collectionNumber] = words.toMutableList()
                        saveCache()
                        emit(Response.Success(words))
                    } else {
                        emit(Response.Failure(Exception("No data found")))
                    }
                }
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
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
            if (!mp3Dir.exists()) jpgDir.mkdirs()

            val jpgZipRef = storage.reference.child("5000_words/all_jpg.zip")
            val mp3ZipRef = storage.reference.child("5000_words/all_mp3.zip")

            downloadAndExtractZip(jpgZipRef, jpgDir)
            downloadAndExtractZip(mp3ZipRef, mp3Dir)

            // After downloading, get all word collections and save to cache
            for (collectionNumber in 0..79) {
                getWordsCollection(collectionNumber).collect { response ->
                    if (response is Response.Success) {
                        wordsCache[collectionNumber] = response.data.toMutableList()
                    }
                }
            }
            saveCache()

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

    override fun excludeWordFromRange(wordId: String, collectionNumber: Int) {
        wordsCache[collectionNumber] = wordsCache[collectionNumber]?.filterNot { it.id == wordId }?.toMutableList() ?: mutableListOf()
        saveCache()
    }

    override fun incrementCounter(collectionNumber: Int) {
        collectionCounters[collectionNumber] = (collectionCounters[collectionNumber] ?: 0) + 1
        saveCounters()
    }

    override fun getCounter(collectionNumber: Int): Int {
        return collectionCounters[collectionNumber] ?: 0
    }
}
