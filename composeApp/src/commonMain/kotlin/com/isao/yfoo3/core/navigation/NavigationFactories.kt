package com.isao.yfoo3.core.navigation

import org.koin.core.annotation.Single

@Single
class NavigationFactories(val list: List<NavigationFactory>)