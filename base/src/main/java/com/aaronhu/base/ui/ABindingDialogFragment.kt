package com.aaronhu.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.aaronhu.base.ext.inflateBindingWithReflect

/**
 * huyu create
 * 2025/3/17 16:44
 */
abstract class ABindingDialogFragment<VB : ViewBinding> : DialogFragment() {

    private lateinit var binding: VB

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateBindingWithReflect(inflater)
        return binding.root
    }
}