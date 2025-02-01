package com.example.app2_birthdayclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app2_birthdayclone.ui.theme.App2_BirthdayCloneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App2_BirthdayCloneTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingImage(
                        textMessage = "Android",
                        textSender = "Deb",
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun GreetingText(msg: String, sender: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally // Key change 3 (also apply here)
    ) {  // Remove verticalArrangement from here. It's handled in the parent Box.
        Text(
            text = "Hello $msg!",
            textAlign = TextAlign.Center,
            lineHeight = 50.sp,
            fontSize = 30.sp
        )
        Text(
            text="from $sender...",
            color = Color.Gray,
            modifier = Modifier
                .padding(10.dp), // Removed .align(Alignment.End)
            fontSize = 15.sp
        )
    }
}

@Composable
fun GreetingImage(textMessage: String, textSender: String, modifier: Modifier = Modifier){
    val image = painterResource(R.drawable.androidparty)
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) { // Key change 1
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize() // Key change 2: Make image fill the Box
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Key change 3
            verticalArrangement = Arrangement.Center // Key change 4: Center vertically
        ) {
            GreetingText(
                msg = textMessage,
                sender = textSender,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GreetingImage(textMessage = "PreviewString", textSender="Debabrota")
}