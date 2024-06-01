import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word
import com.learn.american.english.mfw5000.ui.theme.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
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
}

