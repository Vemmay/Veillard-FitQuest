package com.example.fitquest.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.fitquest.LeaderboardViewModel
import com.example.fitquest.component.TopUserScreen
import com.example.fitquest.data.database.User

@Composable
fun LeaderboardScreen(viewModel: LeaderboardViewModel) {
    viewModel.loadTopUsers()
    val topUsers by viewModel.topUsers.collectAsState()
    Log.d("LeaderboardScreen", "Top Users: $topUsers")

    if (topUsers.isEmpty()) {
        // Show loading indicator or placeholder
        Text("Loading...")
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopUserScreen(topUsers)
            Spacer(modifier = Modifier.height(40.dp))
            LazyColumn (Modifier.fillMaxWidth().padding(top = 32.dp)){
                items(topUsers.size) { index ->
                    val user = topUsers[index]
                    UserSession(user, index)
                }
            }
        }
    }
}

@Composable
fun UserSession(user: User, rank: Int){
    Box (modifier = Modifier
        .padding(16.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.secondaryContainer)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row (verticalAlignment = Alignment.CenterVertically) {
                val profileColor = if (rank < 3) { Color.Yellow } else { MaterialTheme.colorScheme.primary }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://media.istockphoto.com/id/1386479313/photo/happy-millennial-afro-american-business-woman-posing-isolated-on-white.jpg?s=612x612&w=0&k=20&c=8ssXDNTp1XAPan8Bg6mJRwG7EXHshFO5o0v9SIj96nY=")
                        .crossfade(true).build(),
                    contentDescription = "Circular Image with Border",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(2.dp, profileColor, CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(user.name)
            }
            Text("Score: ${user.score}")
        }
    }
}