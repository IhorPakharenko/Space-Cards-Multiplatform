package com.isao.yfoo3.core.navigation

import androidx.navigation.NavGraphBuilder

interface NavigationFactory {
    fun create(builder: NavGraphBuilder)
}
