package com.aaronhu.base.ext

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.aaronhu.base.utils.GsonUtils
import java.lang.reflect.ParameterizedType

/**
 * huyu create
 * 2025/3/17 11:11
 */

fun <T> T.toJson(): String = GsonUtils.toJson(this)

@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> Any.inflateBindingWithReflect(inflater: LayoutInflater): VB {
    var genericSuperClass = javaClass.genericSuperclass // 获取当前类的泛型父类
    var supperClass = javaClass.superclass
    while (supperClass != null) {
        //一级一级往上找
        if (genericSuperClass is ParameterizedType) {
            genericSuperClass.actualTypeArguments.filter {
                //过滤viewBinding子类
                it is Class<*> && ViewBinding::class.java.isAssignableFrom(it)
            }.forEach { bindingClass ->
                try {
                    return (bindingClass as Class<VB>).getMethod(
                        "inflate",
                        LayoutInflater::class.java
                    ).invoke(null, inflater) as VB
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        genericSuperClass = supperClass.genericSuperclass
        supperClass = supperClass.superclass
    }
    throw IllegalArgumentException("must have a generic type parameter.")
}