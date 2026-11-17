package com.example.stylesync.data.remote

import android.net.Uri
import com.example.stylesync.data.local.entity.GarmentEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

data class RemoteGarment(
    val remoteId: String,
    val name: String,
    val category: String,
    val colorHex: String,
    val imageUrl: String,
    val updatedAt: Long
)

class GarmentRemoteDataSource {
    // lazy: si Storage no esta habilitado, la app no se cae al arrancar
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }

    private suspend fun uid(): String =
        auth.currentUser?.uid
            ?: auth.signInAnonymously().await().user?.uid
            ?: error("Sin sesion en Firebase")

    private suspend fun col() =
        db.collection("users").document(uid()).collection("garments")

    suspend fun upsert(g: GarmentEntity) = withTimeout(TIMEOUT_MS) {
        col().document(g.remoteId).set(
            mapOf(
                "name" to g.name,
                "category" to g.category,
                "colorHex" to g.colorHex,
                "imageUrl" to g.imageUrl,
                "updatedAt" to g.updatedAt
            )
        ).await()
        Unit
    }

    suspend fun delete(remoteId: String) = withTimeout(TIMEOUT_MS) {
        col().document(remoteId).delete().await()
        Unit
    }

    // Source.SERVER: sin internet falla en vez de devolver una cache vacia
    suspend fun fetchAll(): List<RemoteGarment> = withTimeout(TIMEOUT_MS) {
        col().get(Source.SERVER).await().documents.map { d ->
            RemoteGarment(
                remoteId = d.id,
                name = d.getString("name").orEmpty(),
                category = d.getString("category").orEmpty(),
                colorHex = d.getString("colorHex").orEmpty(),
                imageUrl = d.getString("imageUrl").orEmpty(),
                updatedAt = d.getLong("updatedAt") ?: 0L
            )
        }
    }

    suspend fun uploadImage(remoteId: String, uri: Uri): String = withTimeout(UPLOAD_TIMEOUT_MS) {
        val ref = storage.reference.child("users/${uid()}/garments/$remoteId.jpg")
        ref.putFile(uri).await()
        ref.downloadUrl.await().toString()
    }

    private companion object {
        const val TIMEOUT_MS = 8_000L
        const val UPLOAD_TIMEOUT_MS = 30_000L
    }
}