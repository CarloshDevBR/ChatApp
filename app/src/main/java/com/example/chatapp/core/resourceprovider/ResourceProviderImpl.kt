package com.example.chatapp.core.resourceprovider

import android.content.Context

class ResourceProviderImpl(
    private val context: Context
) : ResourceProvider {
    override fun getString(resId: Int) = context.getString(resId)

    override fun getString(resId: Int, vararg formatArgs: Any): String =
        context.getString(resId, *formatArgs)
}