package com.example.stylesync.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stylesync.data.local.SessionManager
import com.example.stylesync.data.local.dao.GarmentDao
import com.example.stylesync.data.repository.GarmentRepository
import com.example.stylesync.ui.auth.AuthViewModel
import com.example.stylesync.ui.planday.PlanDayViewModel
import com.example.stylesync.ui.reports.ReportsViewModel
import com.example.stylesync.ui.settings.SettingsViewModel
import com.example.stylesync.ui.theme.closet.ClosetViewModel
import com.example.stylesync.ui.theme.detail.GarmentDetailViewModel
import com.example.stylesync.ui.theme.form.AddGarmentViewModel

/** Una sola fabrica para todos los ViewModels que necesitan dao, repositorio o sesion. */
class AppViewModelFactory(
    private val dao: GarmentDao,
    private val session: SessionManager,
    private val appContext: Context,
    private val repository: GarmentRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val vm: ViewModel = when {
            modelClass.isAssignableFrom(ClosetViewModel::class.java) -> ClosetViewModel(repository)
            modelClass.isAssignableFrom(GarmentDetailViewModel::class.java) -> GarmentDetailViewModel(repository)
            modelClass.isAssignableFrom(AddGarmentViewModel::class.java) -> AddGarmentViewModel(repository)
            modelClass.isAssignableFrom(PlanDayViewModel::class.java) -> PlanDayViewModel(repository)
            modelClass.isAssignableFrom(ReportsViewModel::class.java) -> ReportsViewModel(appContext, repository)
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(session)
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(session)
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
        return vm as T
    }
}
