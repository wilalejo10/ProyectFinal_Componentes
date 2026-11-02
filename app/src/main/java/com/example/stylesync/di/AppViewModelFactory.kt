package com.example.stylesync.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stylesync.data.local.SessionManager
import com.example.stylesync.data.local.dao.GarmentDao
import com.example.stylesync.ui.auth.AuthViewModel
import com.example.stylesync.ui.planday.PlanDayViewModel
import com.example.stylesync.ui.settings.SettingsViewModel
import com.example.stylesync.ui.theme.closet.ClosetViewModel
import com.example.stylesync.ui.theme.detail.GarmentDetailViewModel
import com.example.stylesync.ui.theme.form.AddGarmentViewModel

/** Una sola fábrica para todos los ViewModels que necesitan dao o sesión. */
class AppViewModelFactory(
    private val dao: GarmentDao,
    private val session: SessionManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val vm: ViewModel = when {
            modelClass.isAssignableFrom(ClosetViewModel::class.java) -> ClosetViewModel(dao)
            modelClass.isAssignableFrom(GarmentDetailViewModel::class.java) -> GarmentDetailViewModel(dao)
            modelClass.isAssignableFrom(AddGarmentViewModel::class.java) -> AddGarmentViewModel(dao)
            modelClass.isAssignableFrom(PlanDayViewModel::class.java) -> PlanDayViewModel(dao)
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(session)
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(session)
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
        return vm as T
    }
}