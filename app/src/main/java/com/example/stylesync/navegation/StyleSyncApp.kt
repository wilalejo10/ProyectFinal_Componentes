package com.example.stylesync.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stylesync.R
import com.example.stylesync.data.PlanStore
import com.example.stylesync.data.local.AppDatabase
import com.example.stylesync.data.local.SessionManager
import com.example.stylesync.di.AppViewModelFactory
import com.example.stylesync.ui.auth.AuthViewModel
import com.example.stylesync.ui.auth.ForgotPasswordScreen
import com.example.stylesync.ui.auth.LoginScreen
import com.example.stylesync.ui.auth.RegisterScreen
import com.example.stylesync.ui.camera.CameraScreen
import com.example.stylesync.ui.components.AppTopBar
import com.example.stylesync.ui.components.BottomItem
import com.example.stylesync.ui.components.BottomNavBar
import com.example.stylesync.ui.home.HomeScreen
import com.example.stylesync.ui.home.HomeViewModel
import com.example.stylesync.ui.outfitresult.OutfitResultScreen
import com.example.stylesync.ui.outfitresult.OutfitResultViewModel
import com.example.stylesync.ui.planday.PlanDayScreen
import com.example.stylesync.ui.planday.PlanDayViewModel
import com.example.stylesync.ui.settings.SettingsScreen
import com.example.stylesync.ui.settings.SettingsViewModel
import com.example.stylesync.ui.splash.SplashScreen
import com.example.stylesync.ui.theme.closet.ClosetScreen
import com.example.stylesync.ui.theme.closet.ClosetViewModel
import com.example.stylesync.ui.theme.detail.GarmentDetailScreen
import com.example.stylesync.ui.theme.detail.GarmentDetailViewModel
import com.example.stylesync.ui.theme.form.AddEditGarmentScreen
import com.example.stylesync.ui.theme.form.AddGarmentViewModel
import com.example.stylesync.ui.theme.reports.ReportsScreen
import com.example.stylesync.ui.weekplan.WeekPlanScreen
import com.example.stylesync.ui.weekplan.WeekPlanViewModel

/** Raíz de la app: barra inferior + NavGraph completo (auth y principal). */
@Composable
fun StyleSyncApp() {
    val context = LocalContext.current.applicationContext
    val session = remember { SessionManager(context) }
    val factory = remember {
        AppViewModelFactory(AppDatabase.getDatabase(context).garmentDao(), session)
    }
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val tabs = remember {
        listOf(
            BottomItem(Routes.HOME, R.string.nav_home, Icons.Filled.Home),
            BottomItem(Routes.CLOSET, R.string.nav_closet, Icons.Filled.Checkroom),
            BottomItem(Routes.WEEK_PLAN, R.string.nav_plan, Icons.Filled.CalendarMonth),
            BottomItem(Routes.REPORTS, R.string.nav_reports, Icons.Filled.Description),
            BottomItem(Routes.SETTINGS, R.string.nav_settings, Icons.Filled.Settings)
        )
    }

    Scaffold(
        bottomBar = {
            if (tabs.any { it.route == currentRoute }) {
                BottomNavBar(tabs, currentRoute) { route -> navController.goToTab(route) }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.padding(innerPadding)
        ) {
            // ---------- AUTH ----------
            composable(Routes.SPLASH) {
                SplashScreen(onFinished = {
                    val destination = if (session.isLoggedIn) Routes.HOME else Routes.LOGIN
                    navController.navigate(destination) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                })
            }
            composable(Routes.LOGIN) {
                val vm: AuthViewModel = viewModel(factory = factory)
                LoginScreen(
                    viewModel = vm,
                    onLoggedIn = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onRegister = { navController.navigate(Routes.REGISTER) },
                    onForgot = { navController.navigate(Routes.FORGOT) }
                )
            }
            composable(Routes.REGISTER) {
                val vm: AuthViewModel = viewModel(factory = factory)
                RegisterScreen(
                    viewModel = vm,
                    onRegistered = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.FORGOT) {
                val vm: AuthViewModel = viewModel(factory = factory)
                ForgotPasswordScreen(viewModel = vm, onBack = { navController.popBackStack() })
            }

            // ---------- PESTAÑAS ----------
            composable(Routes.HOME) {
                val vm: HomeViewModel = viewModel()
                val state by vm.uiState.collectAsStateWithLifecycle()
                TitledScreen(stringResource(R.string.nav_home)) { m ->
                    HomeScreen(
                        uiState = state,
                        onPlanWeekClick = { navController.goToTab(Routes.WEEK_PLAN) },
                        modifier = m
                    )
                }
            }
            composable(Routes.CLOSET) {
                val vm: ClosetViewModel = viewModel(factory = factory)
                TitledScreen(
                    title = stringResource(R.string.nav_closet),
                    fab = {
                        FloatingActionButton(onClick = { navController.navigate(Routes.form()) }) {
                            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.closet_add))
                        }
                    }
                ) { m ->
                    Box(m) {
                        ClosetScreen(
                            viewModel = vm,
                            onNavigateToDetail = { navController.navigate(Routes.detail(it)) }
                        )
                    }
                }
            }
            composable(Routes.WEEK_PLAN) {
                val vm: WeekPlanViewModel = viewModel()
                val state by vm.uiState.collectAsStateWithLifecycle()
                TitledScreen(stringResource(R.string.nav_plan)) { m ->
                    WeekPlanScreen(
                        uiState = state,
                        onDayClick = vm::onDaySelected,
                        onPlanDayClick = { day ->
                            PlanStore.selectedDay = day
                            navController.navigate(Routes.PLAN_DAY)
                        },
                        modifier = m
                    )
                }
            }
            composable(Routes.REPORTS) {
                TitledScreen(stringResource(R.string.nav_reports)) { m ->
                    Box(m) { ReportsScreen() }
                }
            }
            composable(Routes.SETTINGS) {
                val vm: SettingsViewModel = viewModel(factory = factory)
                TitledScreen(stringResource(R.string.settings_title)) { m ->
                    SettingsScreen(
                        viewModel = vm,
                        onLogout = {
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        },
                        modifier = m
                    )
                }
            }

            // ---------- PRENDAS ----------
            composable(
                route = Routes.DETAIL,
                arguments = listOf(navArgument(Routes.ARG_GARMENT_ID) { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt(Routes.ARG_GARMENT_ID) ?: -1
                val vm: GarmentDetailViewModel = viewModel(factory = factory)
                TitledScreen(
                    title = stringResource(R.string.garment_detail_title),
                    onBack = { navController.popBackStack() }
                ) { m ->
                    Box(m) {
                        GarmentDetailScreen(
                            viewModel = vm,
                            garmentId = id,
                            onNavigateUp = { navController.popBackStack() },
                            onNavigateToEdit = { navController.navigate(Routes.form(it)) }
                        )
                    }
                }
            }
            composable(
                route = Routes.FORM,
                arguments = listOf(navArgument(Routes.ARG_GARMENT_ID) { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt(Routes.ARG_GARMENT_ID) ?: -1
                val vm: AddGarmentViewModel = viewModel(factory = factory)
                LaunchedEffect(id) { vm.load(id) }

                // La cámara deja aquí la foto tomada.
                val photo by entry.savedStateHandle
                    .getStateFlow<String?>(Routes.KEY_PHOTO, null)
                    .collectAsStateWithLifecycle()
                LaunchedEffect(photo) {
                    photo?.let {
                        vm.imageUri = it
                        entry.savedStateHandle[Routes.KEY_PHOTO] = null
                    }
                }

                TitledScreen(
                    title = stringResource(
                        if (id > 0) R.string.garment_form_title_edit else R.string.garment_form_title_new
                    ),
                    onBack = { navController.popBackStack() }
                ) { m ->
                    AddEditGarmentScreen(
                        viewModel = vm,
                        onNavigateBack = {
                            // Al editar se vuelve al closet para que el detalle no quede con datos viejos.
                            if (id > 0) navController.popBackStack(Routes.CLOSET, false)
                            else navController.popBackStack()
                        },
                        onOpenCamera = { navController.navigate(Routes.CAMERA) },
                        modifier = m
                    )
                }
            }
            composable(Routes.CAMERA) {
                CameraScreen(
                    onPhoto = { uri ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle?.set(Routes.KEY_PHOTO, uri)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // ---------- OUTFIT ----------
            composable(Routes.PLAN_DAY) {
                val vm: PlanDayViewModel = viewModel(factory = factory)
                val state by vm.uiState.collectAsStateWithLifecycle()
                TitledScreen(
                    title = stringResource(R.string.plan_day_title),
                    onBack = { navController.popBackStack() }
                ) { m ->
                    PlanDayScreen(
                        uiState = state,
                        onOccasionSelected = vm::onOccasionSelected,
                        onGarmentToggled = vm::onGarmentToggled,
                        onGenerateClick = {
                            vm.onGenerateClick()?.let { outfit ->
                                PlanStore.save(outfit)
                                navController.navigate(Routes.RESULT)
                            }
                        },
                        modifier = m
                    )
                }
            }
            composable(Routes.RESULT) {
                val vm: OutfitResultViewModel = viewModel()
                val state by vm.uiState.collectAsStateWithLifecycle()
                TitledScreen(
                    title = stringResource(R.string.result_title),
                    onBack = { navController.popBackStack() }
                ) { m ->
                    OutfitResultScreen(
                        uiState = state,
                        onPlanAnotherClick = { navController.popBackStack(Routes.WEEK_PLAN, false) },
                        modifier = m
                    )
                }
            }
        }
    }
}

/** Cambia de pestaña conservando el estado de cada una. */
private fun NavHostController.goToTab(route: String) {
    navigate(route) {
        popUpTo(Routes.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** Marco común: barra superior + botón flotante opcional. */
@Composable
private fun TitledScreen(
    title: String,
    onBack: (() -> Unit)? = null,
    fab: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = { AppTopBar(title = title, onBack = onBack) },
        floatingActionButton = fab
    ) { padding ->
        content(Modifier.padding(padding))
    }
}