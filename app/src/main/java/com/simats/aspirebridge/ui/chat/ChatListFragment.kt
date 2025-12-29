package com.simats.aspirebridge.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.aspirebridge.databinding.FragmentChatListBinding
import com.simats.aspirebridge.di.DependencyContainer
import com.simats.aspirebridge.ui.ViewModelFactory

class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChatListViewModel
    private lateinit var chatRoomsAdapter: ChatRoomsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupViewModel() {
        val dependencyContainer = DependencyContainer.getInstance(requireContext())
        val factory = ViewModelFactory(dependencyContainer)
        viewModel = ViewModelProvider(this, factory)[ChatListViewModel::class.java]
    }

    private fun setupRecyclerView() {
        chatRoomsAdapter = ChatRoomsAdapter { chatRoom ->
            // Navigate to chat room
            val action = ChatListFragmentDirections.actionChatListToChatRoom(chatRoom.id)
            findNavController().navigate(action)
        }

        binding.rvChatRooms.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatRoomsAdapter
        }
    }

    private fun setupObservers() {
        viewModel.chatRooms.observe(viewLifecycleOwner) { chatRooms ->
            chatRoomsAdapter.submitList(chatRooms)
            
            // Show/hide empty state
            if (chatRooms.isEmpty()) {
                binding.rvChatRooms.visibility = View.GONE
                binding.tvEmptyState.visibility = View.VISIBLE
            } else {
                binding.rvChatRooms.visibility = View.VISIBLE
                binding.tvEmptyState.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshChatRooms()
        }

        binding.fabNewChat.setOnClickListener {
            // TODO: Navigate to new chat/contact selection
            Toast.makeText(requireContext(), "New chat feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}