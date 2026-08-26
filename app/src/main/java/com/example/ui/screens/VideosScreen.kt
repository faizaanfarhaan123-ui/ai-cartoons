package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.R
import com.example.data.VideoPost
import com.example.navigation.Screen
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideosScreen(viewModel: MainViewModel, navController: NavController) {
    val videos by viewModel.videoPosts.collectAsState()
    val featuredVideo = videos.firstOrNull()
    
    val categories = listOf("All", "Funny", "Adventure", "Kids", "AI Stories", "Animation")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Search cartoons...") },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(end = 16.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        readOnly = true,
                        enabled = false
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier.clickable { navController.navigate(Screen.Search.route) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Categories
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(categories.size) { index ->
                        FilterChip(
                            selected = index == 0,
                            onClick = { /*TODO*/ },
                            label = { Text(categories[index]) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.Black
                            )
                        )
                    }
                }
            }
            
            // Video Player Area
            if (featuredVideo != null) {
                item {
                    Column(modifier = Modifier.fillMaxWidth().clickable { navController.navigate(Screen.VideoPlayer.createRoute(featuredVideo.id)) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f/9f)
                                .background(Color.Black)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_cartoon_thumb_1787672653515),
                                contentDescription = "Playing Video",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f))
                            )
                            // Player Controls Overlay
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(Icons.Filled.Pause, contentDescription = "Pause", tint = Color.White)
                                }
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(Icons.Filled.VolumeUp, contentDescription = "Volume", tint = Color.White)
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Slider(
                                    value = 0.4f,
                                    onValueChange = {},
                                    modifier = Modifier
                                        .weight(3f)
                                        .padding(horizontal = 8.dp),
                                    colors = SliderDefaults.colors(
                                        thumbColor = MaterialTheme.colorScheme.primary,
                                        activeTrackColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text("04:20 / ${featuredVideo.duration}", color = Color.White, style = MaterialTheme.typography.labelSmall)
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(Icons.Filled.Fullscreen, contentDescription = "Fullscreen", tint = Color.White)
                                }
                            }
                        }
                        
                        // Video Info
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(featuredVideo.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${featuredVideo.views} views • Uploaded recently", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("AI", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(featuredVideo.creator, fontWeight = FontWeight.Bold)
                                }
                                Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.Black)) {
                                    Text("SUBSCRIBE")
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                InteractionButton(icon = Icons.Filled.ThumbUp, text = "Like")
                                InteractionButton(icon = Icons.Filled.ChatBubble, text = "Comment")
                                InteractionButton(icon = Icons.Filled.Share, text = "Share")
                            }
                        }
                    }
                }
            }
            
            // Related Videos
            item {
                Text(
                    "More Videos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp)
                )
            }
            
            items(videos.size) { index ->
                if (featuredVideo == null || videos[videos.size - 1 - index].id != featuredVideo.id) {
                    RelatedVideoCard(videos[videos.size - 1 - index], navController)
                }
            }
            
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}


@Composable
fun InteractionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun RelatedVideoCard(video: VideoPost, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(Screen.VideoPlayer.createRoute(video.id)) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(140.dp)
                .aspectRatio(16f/9f)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_cartoon_thumb_1787672653515),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.BottomEnd)
                    .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text("10:00", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(video.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            Spacer(modifier = Modifier.height(4.dp))
            Text(video.creator, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
            Text("${video.views} views", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
        }
    }
}

