package com.example.app2_birthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app2_birthday.ui.theme.App2_birthdayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App2_birthdayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GreetingText(message = "Happy Birthday Deb!", from = "From Sampa...", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}


@Composable
    fun GreetingText(message: String, from: String, modifier: Modifier = Modifier) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            text = message,
            fontSize = 30.sp,
            lineHeight = 116.sp,
            color = Color.Blue,
            textAlign = TextAlign.Center
        )
        Text(
            text = from,
            fontSize = 16.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BirthdayPreview() {
    App2_birthdayTheme {
        GreetingText(message = "Happy Birthday Deb!", from ="From Sampa")
    }
}