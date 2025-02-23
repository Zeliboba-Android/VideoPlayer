package com.example.videoplayer.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.videoplayer.data.api.NetworkModule
import com.example.videoplayer.data.db.AppDatabase
import com.example.videoplayer.data.repository.VideoRepository
import com.example.videoplayer.databinding.FragmentVideoListBinding
import com.example.videoplayer.domain.model.Resource
import com.example.videoplayer.presentation.adapter.VideoAdapter
import com.example.videoplayer.presentation.viewModel.VideoListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class VideoListFragment : Fragment() {
    private var _binding: FragmentVideoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: VideoListViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = NetworkModule.apiService
        val db = AppDatabase.getInstance(requireContext())
        val repository = VideoRepository(apiService, db.videoDao(), requireContext())

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return VideoListViewModel(repository) as T
            }
        })[VideoListViewModel::class.java]

        setupRecyclerView()
        setupSwipeRefresh()
        viewModel.loadTravelVideos()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        val adapter = VideoAdapter { video ->
            val intent = Intent(requireContext(), VideoPlayerActivity::class.java).apply {
                putExtra("VIDEO_URL", video.videoUrl)
            }
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadTravelVideos()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.videoState.collect { state ->
                when (state) {
                    is Resource.Loading -> showLoading()
                    is Resource.Success -> {
                        hideLoading()
                        (binding.recyclerView.adapter as VideoAdapter).submitList(state.data)
                    }
                    is Resource.Error -> {
                        hideLoading()
                        showError(state.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}