package com.example.stylesync.data.repository

import androidx.core.net.toUri
import com.example.stylesync.data.local.dao.GarmentDao
import com.example.stylesync.data.local.entity.GarmentEntity
import com.example.stylesync.data.remote.GarmentRemoteDataSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class GarmentRepository(
    private val dao: GarmentDao,
    private val remote: GarmentRemoteDataSource
) {
    // scope propio: la sincronizacion sigue aunque se cierre la pantalla
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val syncMutex = Mutex()

    val garments: Flow<List<GarmentEntity>> = dao.getAllGarments()
    fun garmentsByCategory(category: String) = dao.getGarmentsByCategory(category)
    suspend fun getById(id: Int) = dao.getGarmentById(id)
    suspend fun getAll() = dao.getAllOnce()

    /** Guarda primero en Room y despues intenta subir a Firebase. */
    suspend fun save(garment: GarmentEntity) {
        dao.insertGarment(
            garment.copy(updatedAt = System.currentTimeMillis(), pendingSync = true, deleted = false)
        )
        requestSync()
    }

    /** Borrado logico: se oculta ya, y se borra en Firestore al sincronizar. */
    suspend fun delete(garment: GarmentEntity) {
        dao.updateGarment(
            garment.copy(deleted = true, pendingSync = true, updatedAt = System.currentTimeMillis())
        )
        requestSync()
    }

    fun requestSync() {
        scope.launch { sync() }
    }

    /** Sube lo pendiente y baja lo nuevo. Devuelve true si todo salio bien. */
    suspend fun sync(): Boolean = syncMutex.withLock {
        val pushed = pushPending()
        val pulled = pull()
        pushed && pulled
    }

    private suspend fun pushPending(): Boolean {
        var allOk = true
        for (g in dao.getPending()) {
            if (!pushOne(g)) allOk = false
        }
        return allOk
    }

    private suspend fun pushOne(g: GarmentEntity): Boolean {
        if (g.deleted) {
            attempt { remote.delete(g.remoteId) } ?: return false
            dao.hardDelete(g.id)
            return true
        }
        var item = g
        var imageOk = true
        if (g.imageUri.isNotBlank() && g.imageUrl.isBlank()) {
            val url = attempt { remote.uploadImage(g.remoteId, g.imageUri.toUri()) }
            if (url != null) item = g.copy(imageUrl = url) else imageOk = false
        }
        attempt { remote.upsert(item) } ?: return false
        // si la foto no subio (p. ej. Storage no disponible) la prenda igual se
        // sincroniza y queda pendiente solo la foto
        dao.updateGarment(item.copy(pendingSync = !imageOk))
        return imageOk
    }

    private suspend fun pull(): Boolean {
        val remoteList = attempt { remote.fetchAll() } ?: return false
        val remoteIds = remoteList.map { it.remoteId }.toSet()
        for (r in remoteList) {
            val local = dao.getByRemoteId(r.remoteId)
            when {
                local == null -> dao.insertGarment(
                    GarmentEntity(
                        name = r.name, category = r.category, colorHex = r.colorHex,
                        imageUri = "", remoteId = r.remoteId, imageUrl = r.imageUrl,
                        updatedAt = r.updatedAt, pendingSync = false
                    )
                )
                !local.pendingSync && r.updatedAt > local.updatedAt -> dao.updateGarment(
                    local.copy(
                        name = r.name, category = r.category, colorHex = r.colorHex,
                        imageUrl = r.imageUrl, updatedAt = r.updatedAt
                    )
                )
            }
        }
        // prendas ya sincronizadas que ya no estan en el servidor: se borraron en otro dispositivo
        dao.getSynced().filter { it.remoteId !in remoteIds }.forEach { dao.hardDelete(it.id) }
        return true
    }

    /** Ejecuta una llamada de red; devuelve null si falla o se agota el tiempo. */
    private suspend fun <T> attempt(block: suspend () -> T): T? =
        try {
            block()
        } catch (e: TimeoutCancellationException) {
            null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
}