package com.example.stylesync.data.repository

import com.example.stylesync.data.PlanStore
import com.example.stylesync.data.model.Occasion
import com.example.stylesync.data.model.Outfit
import com.example.stylesync.data.remote.PlanRemoteDataSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object PlanRepository {

    private val remote by lazy { PlanRemoteDataSource() }

    private var garments: GarmentRepository? = null

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun attach(repository: GarmentRepository) {
        garments = repository
    }

    fun persist(date: String, outfit: Outfit) {
        scope.launch {
            try {
                remote.saveDay(
                    date = date,
                    occasion = outfit.occasion?.name,
                    outfitId = outfit.id,
                    garmentRemoteIds = outfit.garments.map { it.remoteId },
                    paletteHex = outfit.paletteHex
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
            }
        }
    }

    suspend fun loadWeek(): Boolean {
        val garmentRepository = garments ?: return false
        val week = PlanStore.week.value
        if (week.isEmpty()) return false

        val remoteWeek = try {
            remote.fetchWeek(week.first().date, week.last().date)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return false
        }

        val byRemoteId = garmentRepository.getAll().associateBy { it.remoteId }

        val loaded = week.map { day ->
            val remoteDay = remoteWeek.days.firstOrNull { it.date == day.date } ?: return@map day
            val remoteOutfit = remoteDay.outfitId?.let { remoteWeek.outfits[it] }

            val outfitGarments = remoteOutfit?.garmentRemoteIds.orEmpty().mapNotNull { byRemoteId[it] }
            val outfit = if (remoteOutfit != null && outfitGarments.isNotEmpty()) {
                Outfit(
                    id = remoteOutfit.id,
                    garments = outfitGarments,
                    paletteHex = remoteOutfit.paletteHex,
                    occasion = parseOccasion(remoteOutfit.occasion)
                )
            } else null

            day.copy(occasion = parseOccasion(remoteDay.occasion), outfit = outfit)
        }

        PlanStore.replaceWeek(loaded)
        return true
    }

    private fun parseOccasion(name: String?): Occasion? =
        Occasion.entries.firstOrNull { it.name == name }
}