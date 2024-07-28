package com.idle.post.code

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.idle.postcode.databinding.FragmentPostCodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostCodeFragment : DialogFragment() {
    private val fragmentViewModel: PostCodeViewModel by viewModels()

    private var _binding: FragmentPostCodeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostCodeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = fragmentViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}