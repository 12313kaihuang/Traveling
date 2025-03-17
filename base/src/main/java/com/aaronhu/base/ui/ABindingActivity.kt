package com.aaronhu.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.aaronhu.base.ext.inflateBindingWithReflect

/**
 * huyu create
 * 2025/3/17 10:36
 */
abstract class ABindingActivity<VB : ViewBinding> : AppCompatActivity() {
    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutInflater.from(this).inflateBindingWithReflect()
    }

}