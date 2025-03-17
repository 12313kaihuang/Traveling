package com.aaronhu.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.aaronhu.base.ext.inflateBindingWithReflect

/**
 * huyu create
 * 2025/3/17 11:12
 */
abstract class ABindingFragment<VB : ViewBinding> : Fragment() {

    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflater.inflateBindingWithReflect()
        return binding.root
    }
}