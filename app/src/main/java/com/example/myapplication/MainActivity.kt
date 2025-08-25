package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scheduler = AndroidAlarmScheduler(this)
        var alarmItem: AlarmItem? = null

        setContent {
            MyApplicationTheme {

                var origin by remember { mutableStateOf("") }
                var destination by remember { mutableStateOf("") }
                var idealArrivalTime by remember { mutableStateOf("") } // Unix timestamp as string
                var mode by remember { mutableStateOf("") }
                var result by remember { mutableStateOf("") }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    OutlinedTextField(
                        value = origin,
                        onValueChange = { origin = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Origin (location)") }
                    )

                    OutlinedTextField(
                        value = destination,
                        onValueChange = { destination = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Destination (location)") }
                    )

                    OutlinedTextField(
                        value = idealArrivalTime,
                        onValueChange = { idealArrivalTime = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ideal Arrival Time (Unix timestamp)") }
                    )

                    OutlinedTextField(
                        value = mode,
                        onValueChange = { mode = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Mode (driving, walking, transit)") }
                    )

                    Button(
                        onClick = {
                            result = "Fetching directions..."
                            GoogleDirections.fetchDirections(
                                this@MainActivity,
                                mode,
                                idealArrivalTime,
                                origin.replace(" ", "+"),
                                destination.replace(" ", "+"),
                                object : DirectionsCallback {
                                    override fun onSuccess(durationSeconds: Int) {
                                        try {
                                            val arrivalEpoch = idealArrivalTime.toLong()
                                            val alarmEpoch = arrivalEpoch - durationSeconds
                                            val alarmDateTime = Instant.ofEpochSecond(alarmEpoch)
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDateTime()

                                            alarmItem = AlarmItem(
                                                3092,
                                                alarmDateTime,
                                                "Leave now"
                                            )
                                            alarmItem.let { scheduler.schedule(it) }

                                            result = "Alarm scheduled for $alarmDateTime"
                                        } catch (e: Exception) {
                                            Log.e("MainActivity", "Error scheduling alarm", e)
                                            result = "Failed to schedule alarm"
                                        }
                                    }

                                    override fun onFailure(e: Exception) {
                                        Log.e("MainActivity", "Failed to fetch directions", e)
                                        result = "Failed to fetch directions"
                                    }
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Submit")
                    }

                    if (result.isNotEmpty()) {
                        Text("Status: $result")
                    }
                }
            }
        }
    }
}
