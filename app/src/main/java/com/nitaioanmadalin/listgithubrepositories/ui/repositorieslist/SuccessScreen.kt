package com.nitaioanmadalin.listgithubrepositories.ui.repositorieslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nitaioanmadalin.listgithubrepositories.domain.model.GithubItem
import com.nitaioanmadalin.listgithubrepositories.ui.theme.ListGithubRepositoriesTheme

@Composable
fun SuccessScreen(data: List<GithubItem>) {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(data.size) { index ->
            val item = data[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Name: ${item.name}", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "Description: ${item.description}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SuccessScreenPreview() {
    ListGithubRepositoriesTheme {
        val items = mutableListOf<GithubItem>()
        for(index in 1..10) {
            items.add(
                GithubItem(
                    id = index.toLong(),
                    name = "Name $index",
                    description = "Description $index"
                )
            )
        }
        SuccessScreen(data = items)
    }
}
