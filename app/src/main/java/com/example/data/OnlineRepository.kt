package com.example.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class OnlineRepository {
    private val db = FirebaseFirestore.getInstance()

    val channelLinks: Flow<List<ChannelLink>> = callbackFlow {
        val listener = db.collection("channelLinks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val links = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(ChannelLink::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                trySend(links)
            }
        awaitClose { listener.remove() }
    }

    val videoPosts: Flow<List<VideoPost>> = callbackFlow {
        val listener = db.collection("videoPosts")
            .orderBy("uploadDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val posts = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(VideoPost::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                trySend(posts)
            }
        awaitClose { listener.remove() }
    }

    val feedbackMsgs: Flow<List<FeedbackMsg>> = callbackFlow {
        val listener = db.collection("feedbackMsgs")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val msgs = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(FeedbackMsg::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                trySend(msgs)
            }
        awaitClose { listener.remove() }
    }

    val salesLinks: Flow<List<SalesLink>> = callbackFlow {
        val listener = db.collection("salesLinks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val sales = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(SalesLink::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                trySend(sales)
            }
        awaitClose { listener.remove() }
    }

    suspend fun searchVideos(query: String): List<VideoPost> {
        val allVideos = db.collection("videoPosts")
            .orderBy("uploadDate", Query.Direction.DESCENDING)
            .get().await()
            .documents.mapNotNull { it.toObject(VideoPost::class.java)?.apply { id = it.id } }

        val lowerQuery = query.lowercase()
        return allVideos.filter {
            it.title.lowercase().contains(lowerQuery) ||
            it.description.lowercase().contains(lowerQuery) ||
            it.tags.lowercase().contains(lowerQuery) ||
            it.creator.lowercase().contains(lowerQuery)
        }
    }

    suspend fun uploadVideo(post: VideoPost) {
        // Create a new document reference so we can get its ID if we needed it,
        // or just add it.
        post.uploadDate = System.currentTimeMillis()
        db.collection("videoPosts").add(post).await()
    }
}
