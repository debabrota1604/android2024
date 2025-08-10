package com.example.mealplanner.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mealplanner.recommendation.RecommendationEngine
import com.example.mealplanner.ui.viewmodels.MainViewModel
import com.example.mealplanner.ui.screens.SuggestionsScreen

@Composable
fun SuggestionsScreen(viewModel: MainViewModel) {
    val recs = viewModel.recommendations.collectAsState().value

    LazyColumn(modifier = Modifier.padding(12.dp)) {
        items(recs) { rec ->
            SuggestionRow(rec, onAccept = { viewModel.acceptMeal(rec.recipe) })
        }
    }
}

@Composable
fun SuggestionRow(rec: RecommendationEngine.Recommendation, onAccept: () -> Unit) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row {
                Text(text = rec.recipe.name, modifier = Modifier.weight(1f))
                Text(text = "${String.format("%.1f", rec.score)} pts")
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = rec.reasons.joinToString(" • ").takeIf { it.isNotBlank() } ?: "Good match")
            Spacer(modifier = Modifier.padding(6.dp))
            Row {
                Button(onClick = onAccept) { Text("Prepare") }
            }
        }
    }
}