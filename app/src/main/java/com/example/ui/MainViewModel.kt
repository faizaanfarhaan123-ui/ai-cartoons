package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.OnlineRepository
import com.example.data.AuthRepository
import com.example.data.ChannelLink
import com.example.data.VideoPost
import com.example.data.FeedbackMsg
import com.example.data.SalesLink
import com.google.firebase.auth.FirebaseUser
import com.example.util.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = OnlineRepository()
    val authRepository = AuthRepository(application)
    private val networkMonitor = NetworkMonitor(application)
    
    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    
    val channelLinks: StateFlow<List<ChannelLink>> = repository.channelLinks.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val videoPosts: StateFlow<List<VideoPost>> = repository.videoPosts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val feedbackMsgs: StateFlow<List<FeedbackMsg>> = repository.feedbackMsgs.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val salesLinks: StateFlow<List<SalesLink>> = repository.salesLinks.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentUser = MutableStateFlow<FirebaseUser?>(authRepository.getCurrentUser())
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    private val _searchResults = MutableStateFlow<List<VideoPost>>(emptyList())
    val searchResults: StateFlow<List<VideoPost>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    fun setAdmin(isAdmin: Boolean) {
        _isAdmin.value = isAdmin
    }
    
    fun refreshUser() {
        _currentUser.value = authRepository.getCurrentUser()
    }

    suspend fun updateProfileName(name: String) {
        authRepository.updateDisplayName(name)
        refreshUser()
    }

    fun signOut() {
        authRepository.signOut()
        refreshUser()
        setAdmin(false)
    }

    fun performSearch(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            _isSearching.value = true
            _searchResults.value = repository.searchVideos(query)
            _isSearching.value = false
        }
    }

    fun uploadNewVideo(title: String, description: String, videoUrl: String, creator: String, tags: String, isFeatured: Boolean, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isUploading.value = true
            repository.uploadVideo(
                VideoPost(
                    title = title,
                    description = description,
                    videoUrl = videoUrl,
                    thumbnailUrl = "placeholder",
                    creator = creator,
                    tags = tags,
                    isFeatured = isFeatured,
                    views = 0
                )
            )
            _isUploading.value = false
            onComplete()
        }
    }
}
