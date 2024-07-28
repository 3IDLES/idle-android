package com.idle.post.code

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.idle.binding.base.BaseBindingFragment
import com.idle.postcode.databinding.FragmentPostCodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostCodeFragment : BaseBindingFragment<FragmentPostCodeBinding, PostCodeViewModel>(
    FragmentPostCodeBinding::inflate
) {
    override val fragmentViewModel: PostCodeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = fragmentViewModel
    }
}