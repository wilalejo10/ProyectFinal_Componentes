package com.example.stylesync.ui.theme.form

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.ui.components.AppTextField
import com.example.stylesync.ui.components.GarmentImage
import com.example.stylesync.ui.components.ImagePickerSheet
import com.example.stylesync.ui.components.PrimaryButton
import com.example.stylesync.ui.theme.components.CategoryDropdown
import com.example.stylesync.util.PhotoStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val colorPalette = listOf(
    "#000000", "#FFFFFF", "#808080", "#C0392B", "#2F4F7F",
    "#2E8B57", "#F1C40F", "#D2B48C", "#8E44AD"
)

@Composable
fun AddEditGarmentScreen(
    viewModel: AddGarmentViewModel,
    onNavigateBack: () -> Unit,
    onOpenCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val path = withContext(Dispatchers.IO) { PhotoStorage.copyFromGallery(context, uri) }
                if (path != null) viewModel.imageUri = path
            }
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GarmentImage(
            imageUri = viewModel.imageUri,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { showSheet = true }
        )
        OutlinedButton(onClick = { showSheet = true }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Filled.PhotoCamera, contentDescription = null)
            Text(
                text = stringResource(
                    if (viewModel.imageUri.isBlank()) R.string.garment_add_photo
                    else R.string.garment_change_photo
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        AppTextField(
            value = viewModel.name,
            onValueChange = { viewModel.name = it; viewModel.errorRes = null },
            label = stringResource(R.string.garment_name),
            errorText = viewModel.errorRes?.let { stringResource(it) }
        )

        CategoryDropdown(
            selectedCategory = viewModel.category,
            onCategorySelected = { viewModel.category = it }
        )

        Text(stringResource(R.string.garment_color), style = MaterialTheme.typography.titleSmall)
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            colorPalette.forEach { hex ->
                val selected = hex.equals(viewModel.colorHex, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor(hex)))
                        .border(
                            width = if (selected) 3.dp else 1.dp,
                            color = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                        .clickable { viewModel.colorHex = hex }
                )
            }
        }

        PrimaryButton(
            text = stringResource(R.string.garment_save),
            onClick = { viewModel.saveGarment(onSuccess = onNavigateBack) }
        )
    }

    if (showSheet) {
        ImagePickerSheet(
            onTakePhoto = { showSheet = false; onOpenCamera() },
            onPickFromGallery = {
                showSheet = false
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onDismiss = { showSheet = false }
        )
    }
}