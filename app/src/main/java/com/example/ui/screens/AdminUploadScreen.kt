package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUploadScreen(viewModel: MainViewModel, navController: NavController) {
    val isAdmin by viewModel.isAdmin.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()
    
    if (!isAdmin) {
        navController.popBackStack()
        return
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf("") }
    var creator by remember { mutableStateOf("AI CARTOONS Studio") }
    var category by remember { mutableStateOf("Cartoon") }
    var tags by remember { mutableStateOf("") }
    var isFeatured by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Secure Admin Upload") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Publish to Online Database", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Video Title") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            OutlinedTextField(
                value = videoUrl,
                onValueChange = { videoUrl = it },
                label = { Text("Video Stream URL") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = creator,
                onValueChange = { creator = it },
                label = { Text("Creator / Channel Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
                label = { Text("Tags & Keywords (comma separated)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isFeatured, onCheckedChange = { isFeatured = it })
                Text("Mark as Featured Video (Hero Banner)")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.uploadNewVideo(
                            title = title,
                            description = description,
                            videoUrl = videoUrl,
                            creator = creator,
                            tags = "$category, $tags",
                            isFeatured = isFeatured
                        ) {
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !isUploading && title.isNotBlank()
            ) {
                if (isUploading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Upload, contentDescription = "Publish", modifier = Modifier.padding(end = 8.dp))
                    Text("Publish to App", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
