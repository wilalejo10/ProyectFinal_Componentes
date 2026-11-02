package com.example.stylesync.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.stylesync.R
import com.example.stylesync.ui.components.AppTopBar
import com.example.stylesync.ui.components.CameraPreview
import com.example.stylesync.ui.components.ErrorMessage
import com.example.stylesync.ui.components.PermissionRationale
import com.example.stylesync.util.PhotoStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Pantalla 9: CameraX + galería. Devuelve la ruta de la foto con onPhoto(uriString). */
@Composable
fun CameraScreen(onPhoto: (String) -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var capturing by remember { mutableStateOf(false) }
    var errorRes by remember { mutableStateOf<Int?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val path = withContext(Dispatchers.IO) { PhotoStorage.copyFromGallery(context, uri) }
                if (path != null) onPhoto(path) else errorRes = R.string.generic_error
            }
        }
    }

    Scaffold(topBar = { AppTopBar(stringResource(R.string.camera_title), onBack) }) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (!hasPermission) {
                PermissionRationale(
                    title = stringResource(R.string.permission_camera_title),
                    message = stringResource(R.string.permission_camera_message),
                    onGrant = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    onOpenSettings = {
                        context.startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                        )
                    }
                )
            } else {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onReady = { imageCapture = it },
                    onError = { errorRes = R.string.camera_error }
                )
                errorRes?.let {
                    ErrorMessage(stringResource(it), Modifier.align(Alignment.TopCenter))
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(onClick = {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }) { Text(stringResource(R.string.camera_gallery)) }

                    Button(
                        enabled = imageCapture != null && !capturing,
                        onClick = {
                            val capture = imageCapture ?: return@Button
                            val file = PhotoStorage.newPhotoFile(context)
                            capturing = true
                            capture.takePicture(
                                ImageCapture.OutputFileOptions.Builder(file).build(),
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                        capturing = false
                                        onPhoto(PhotoStorage.toUriString(file))
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        capturing = false
                                        errorRes = R.string.camera_error
                                    }
                                }
                            )
                        }
                    ) { Text(stringResource(R.string.camera_take_photo)) }
                }
            }
        }
    }
}