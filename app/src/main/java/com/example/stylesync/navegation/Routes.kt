package com.example.stylesync.navigation

object Routes {
    // Autenticación
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT = "forgot"

    // Pestañas principales (barra inferior)
    const val HOME = "home"
    const val CLOSET = "closet"
    const val WEEK_PLAN = "week_plan"
    const val REPORTS = "reports"
    const val SETTINGS = "settings"

    // Pantallas secundarias
    const val CAMERA = "camera"
    const val PLAN_DAY = "plan_day"
    const val RESULT = "result"

    const val ARG_GARMENT_ID = "garmentId"
    const val DETAIL = "detail/{$ARG_GARMENT_ID}"
    const val FORM = "form/{$ARG_GARMENT_ID}"

    fun detail(id: Int) = "detail/$id"

    /** id = -1 -> prenda nueva; id > 0 -> editar. */
    fun form(id: Int = -1) = "form/$id"

    /** Clave con la que la cámara devuelve la foto a la pantalla anterior. */
    const val KEY_PHOTO = "photo_uri"
}
