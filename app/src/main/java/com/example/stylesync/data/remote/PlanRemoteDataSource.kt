package com.example.stylesync.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

data class RemoteDay(val date: String, val occasion: String?, val outfitId: String?)

data class RemoteOutfit(
    val id: String,
    val garmentRemoteIds: List<String>,
    val paletteHex: List<String>,
    val occasion: String?
)

data class RemoteWeek(val days: List<RemoteDay>, val outfits: Map<String, RemoteOutfit>)

class PlanRemoteDataSource {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    private suspend fun uid(): String =
        auth.currentUser?.uid
            ?: auth.signInAnonymously().await().user?.uid
            ?: error("Sin sesion en Firebase")

    suspend fun saveDay(
        date: String,
        occasion: String?,
        outfitId: String,
        garmentRemoteIds: List<String>,
        paletteHex: List<String>
    ) {
        val userDoc = db.collection("users").document(uid())
        val outfitRef = userDoc.collection("outfits").document(outfitId)
        val dayRef = userDoc.collection("weekPlan").document(date)

        db.batch()
            .set(
                outfitRef,
                mapOf(
                    "garmentIds" to garmentRemoteIds,
                    "paletteHex" to paletteHex,
                    "occasion" to occasion,
                    "createdAt" to System.currentTimeMillis()
                )
            )
            .set(dayRef, mapOf("date" to date, "occasion" to occasion, "outfitId" to outfitId))
            .commit()
    }

    /** Trae los días de [firstDate] a [lastDate] y los outfits que usan. */
    suspend fun fetchWeek(firstDate: String, lastDate: String): RemoteWeek = withTimeout(TIMEOUT_MS) {
        val userDoc = db.collection("users").document(uid())

        val days = userDoc.collection("weekPlan")
            .whereGreaterThanOrEqualTo("date", firstDate)
            .whereLessThanOrEqualTo("date", lastDate)
            .get().await().documents
            .map { d ->
                RemoteDay(
                    date = d.getString("date") ?: d.id,
                    occasion = d.getString("occasion"),
                    outfitId = d.getString("outfitId")
                )
            }

        val outfitIds = days.mapNotNull { it.outfitId }.distinct()
        val outfits: Map<String, RemoteOutfit> =
            if (outfitIds.isEmpty()) emptyMap()
            else userDoc.collection("outfits")
                .whereIn(FieldPath.documentId(), outfitIds)
                .get().await().documents
                .associate { d ->
                    d.id to RemoteOutfit(
                        id = d.id,
                        garmentRemoteIds = (d.get("garmentIds") as? List<*>)?.map { it.toString() }.orEmpty(),
                        paletteHex = (d.get("paletteHex") as? List<*>)?.map { it.toString() }.orEmpty(),
                        occasion = d.getString("occasion")
                    )
                }

        RemoteWeek(days, outfits)
    }

    private companion object {
        const val TIMEOUT_MS = 8_000L
    }
}