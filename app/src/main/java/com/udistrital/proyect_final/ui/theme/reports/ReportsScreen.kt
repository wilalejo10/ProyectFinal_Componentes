package com.udistrital.proyect_final.ui.theme.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReportsScreen()
{
    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Center)
    {
        Text("Reportes (Datos Entrega 1)")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth())
        {
            Text("Descargar PDF Semanal")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("Descargar CSV Inventario")
        }
    }
}