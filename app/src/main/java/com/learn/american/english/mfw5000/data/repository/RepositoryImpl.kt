import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.snapshots
import com.learn.american.english.mfw5000.data.model.Response
import com.learn.american.english.mfw5000.data.model.Word
import com.learn.american.english.mfw5000.ui.theme.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.flow.internal.NopCollector.emit
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    @Named("WordsRef") private val wordsRef: CollectionReference
) : Repository {

    override fun getWordsFromFirestore(start: Int, end: Int): Flow<Response<List<Word>>> {
        return wordsRef.orderBy("number")
            .whereGreaterThanOrEqualTo("number", start)
            .whereLessThanOrEqualTo("number", end)
            .snapshots().map { snapshot ->
                if (!snapshot.isEmpty) {
                    val words = snapshot.documents.mapNotNull { document ->
                        document.toObject<Word>()?.apply { id = document.id }
                    }
                    Response.Success(words)
                } else {
                    Response.Failure(Exception("No data found"))
                }
            }
    }

    override fun getWordById(wordId: String): Flow<Response<Word>> = flow {
        emit(Response.Loading)
        val documentSnapshot = wordsRef.document(wordId).get().await()
        val word = documentSnapshot.toObject<Word>()
        if (word != null) {
            emit(Response.Success(word))
        } else {
            emit(Response.Failure(Exception("Word not found")))
        }
    }.catch { e ->
//        emit(Response.Failure(e))
    }
}
