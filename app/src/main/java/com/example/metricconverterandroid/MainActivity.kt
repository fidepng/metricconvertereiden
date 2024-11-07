package com.example.metricconverterandroid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.metricconverterandroid.ui.theme.MetricConverterAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MetricConverterAndroidTheme {
                MetricConverterApp()
            }
        }
    }
}

@Composable
fun MetricConverterApp() {
    var selectedMetric by remember { mutableStateOf("Panjang") }
    var fromUnit by remember { mutableStateOf("Meter") }
    var toUnit by remember { mutableStateOf("Centimeter") }
    var inputValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val units = when (selectedMetric) {
        "Panjang" -> listOf("Meter", "Centimeter", "Kilometer", "Millimeter")
        else -> listOf()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Metric Converter", style = MaterialTheme.typography.headlineLarge)
        Text("By: [Nama Anda]", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Pilih Metrik")
        DropdownMenu(selectedMetric, listOf("Panjang")) { selectedMetric = it }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Dari:")
        DropdownMenu(fromUnit, units) { fromUnit = it }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Ke:")
        DropdownMenu(toUnit, units) { toUnit = it }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text("Nilai untuk Konversi") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val input = inputValue.toDoubleOrNull()
            if (input != null) {
                result = performConversion(input, fromUnit, toUnit)
            } else {
                result = "Input tidak valid"
            }
        }) {
            Text("Konversi")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Hasil: $result", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun DropdownMenu(selectedItem: String, items: List<String>, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedItem)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun performConversion(value: Double, fromUnit: String, toUnit: String): String {
    val conversionFactors = mapOf(
        "Meter to Centimeter" to 100.0,
        "Centimeter to Meter" to 0.01,
        "Kilometer to Meter" to 1000.0,
        "Meter to Kilometer" to 0.001,
        "Millimeter to Meter" to 0.001,
        "Meter to Millimeter" to 1000.0
    )
    val conversionKey = "$fromUnit to $toUnit"
    val factor = conversionFactors[conversionKey]
    return if (factor != null) {
        (value * factor).toString()
    } else {
        "Konversi tidak tersedia"
    }
}
