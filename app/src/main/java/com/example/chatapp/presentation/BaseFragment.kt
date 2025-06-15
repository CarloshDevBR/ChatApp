package com.example.chatapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    protected abstract fun inflate(): VB

    protected open fun setupViews() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflate()
        _binding = view
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    protected fun navigate(navDirections: NavDirections) {
        findNavController().navigate(navDirections)
    }

    protected fun popBackStack() = findNavController().popBackStack()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}