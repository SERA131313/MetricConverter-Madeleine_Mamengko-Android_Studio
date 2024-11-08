package com.example.metricconverter

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import android.os.Bundle

fun convertMetric(metricType: String, fromUnit: String, toUnit: String, value: Double): String {
    return when (metricType) {
        "Panjang" -> {
            when {
                fromUnit == "Kilometer" && toUnit == "Meter" -> "${value * 1000} Meter"
                fromUnit == "Meter" && toUnit == "Kilometer" -> "${value / 1000} Kilometer"
                fromUnit == "Meter" && toUnit == "Centimeter" -> "${value * 100} Centimeter"
                fromUnit == "Centimeter" && toUnit == "Meter" -> "${value / 100} Meter"
                fromUnit == "Kilometer" && toUnit == "Centimeter" -> "${value * 100000} Centimeter"
                fromUnit == "Centimeter" && toUnit == "Kilometer" -> "${value / 100000} Kilometer"
                else -> "Satuan tidak dikenal untuk Panjang"
            }
        }
        "Berat" -> {
            when {
                fromUnit == "Kilogram" && toUnit == "Gram" -> "${value * 1000} Gram"
                fromUnit == "Gram" && toUnit == "Kilogram" -> "${value / 1000} Kilogram"
                else -> "Satuan tidak dikenal untuk Berat"
            }
        }
        "Suhu" -> {
            when {
                fromUnit == "Celsius" && toUnit == "Fahrenheit" -> "${(value * 9/5) + 32} °F"
                fromUnit == "Fahrenheit" && toUnit == "Celsius" -> "${(value - 32) * 5/9} °C"
                else -> "Satuan tidak dikenal untuk Suhu"
            }
        }
        else -> "Jenis metrik tidak dikenal"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MetricConverterApp()
        }
    }
}

@Composable
fun MetricConverterApp() {
    var selectedMetric by remember { mutableStateOf("Pilih Metrik") }
    var fromUnit by remember { mutableStateOf("--Pilih Satuan") }
    var toUnit by remember { mutableStateOf("--Pilih Satuan") }
    var inputValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    var expandedMetric by remember { mutableStateOf(false) }
    var expandedFromUnit by remember { mutableStateOf(false) }
    var expandedToUnit by remember { mutableStateOf(false) }

    val units = when (selectedMetric) {
        "Panjang" -> listOf("Kilometer", "Meter", "Centimeter")
        "Berat" -> listOf("Kilogram", "Gram")
        "Suhu" -> listOf("Celsius", "Fahrenheit")
        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Metric Converter", style = MaterialTheme.typography.headlineMedium)
        Text(text = "By: Madeleine Aiken Sharapova Mamengko", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Pilih Metrik")
        Box {
            Button(onClick = { expandedMetric = !expandedMetric }) {
                Text(selectedMetric)
            }
            DropdownMenu(
                expanded = expandedMetric,
                onDismissRequest = { expandedMetric = false }
            ) {
                listOf("Panjang", "Berat", "Suhu").forEach { metric ->
                    DropdownMenuItem(
                        text = { Text(metric) },
                        onClick = {
                            selectedMetric = metric
                            fromUnit = "--Pilih Satuan"
                            toUnit = "--Pilih Satuan"
                            expandedMetric = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Dari:")
        Box {
            Button(
                onClick = { if (units.isNotEmpty()) expandedFromUnit = !expandedFromUnit },
                enabled = units.isNotEmpty()
            ) {
                Text(fromUnit)
            }
            DropdownMenu(
                expanded = expandedFromUnit,
                onDismissRequest = { expandedFromUnit = false }
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = {
                            fromUnit = unit
                            expandedFromUnit = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Ke:")
        Box {
            Button(
                onClick = { if (units.isNotEmpty()) expandedToUnit = !expandedToUnit },
                enabled = units.isNotEmpty()
            ) {
                Text(toUnit)
            }
            DropdownMenu(
                expanded = expandedToUnit,
                onDismissRequest = { expandedToUnit = false }
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = {
                            toUnit = unit
                            expandedToUnit = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = inputValue,
            onValueChange = {
                inputValue = it

                val input = inputValue.toDoubleOrNull()
                result = if (input != null) {
                    convertMetric(selectedMetric, fromUnit, toUnit, input)
                } else {
                    "Nilai input tidak valid. Masukkan angka saja."
                }
            },
            label = { Text("Masukkan nilai") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = result, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MetricConverterApp()
}
