@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.kajadesitionmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kajadesitionmaker.ui.theme.KajaDesitionMakerTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KajaDesitionMakerTheme {
                DecisionMakerScreen()
            }
        }
    }
}

@Composable
fun DecisionMakerScreen() {
    var inputFields by remember { mutableStateOf(listOf("")) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var result by remember { mutableStateOf<String?>(null) }
    var warning by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Kaja Decision Maker") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(inputFields) { index, text ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = text,
                            onValueChange = { newValue ->
                                inputFields = inputFields.toMutableList().also { it[index] = newValue }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (selectedIndex == index) Color.Yellow else Color.Transparent
                                )
                        )
                        Spacer(Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                inputFields = inputFields.toMutableList().also { it.removeAt(index) }
                                if (selectedIndex == index) selectedIndex = null
                            },
                            enabled = inputFields.size > 1
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove")
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row {
                Button(
                    onClick = {
                        inputFields = inputFields + ""
                    }
                ) {
                    Text("Add Field")
                }
                Spacer(Modifier.width(16.dp))
                Button(
                    onClick = {
                        val nonEmpty = inputFields.mapIndexed { i, v -> i to v.trim() }
                            .filter { it.second.isNotEmpty() }
                        if (nonEmpty.isEmpty()) {
                            warning = true
                            result = null
                            selectedIndex = null
                        } else {
                            warning = false
                            val (idx, value) = nonEmpty[Random.nextInt(nonEmpty.size)]
                            selectedIndex = idx
                            result = value
                        }
                    }
                ) {
                    Text("Pick Random!")
                }
            }
            Spacer(Modifier.height(16.dp))
            if (warning) {
                Text("Please enter at least one option!", color = Color.Red)
            }
            result?.let {
                Text("Selected: $it", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }

    // Reset highlight after a short delay
    LaunchedEffect(selectedIndex) {
        if (selectedIndex != null) {
            delay(1200)
            selectedIndex = null
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DecisionMakerScreenPreview() {
    KajaDesitionMakerTheme {
        DecisionMakerScreen()
    }
}