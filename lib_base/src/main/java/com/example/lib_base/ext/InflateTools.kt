package com.example.lib_base.ext

import java.lang.reflect.ParameterizedType

/**
 *@author ZhiQiang Tu
 *@time 2021/10/25  21:58
 *@signature 我们不明前路，却已在路上
 */
fun inflateGenericSuperclass(clz: Class<*>, index: Int): Class<*> {
    val type = clz.genericSuperclass as ParameterizedType
    return type.actualTypeArguments[index] as Class<*>
}