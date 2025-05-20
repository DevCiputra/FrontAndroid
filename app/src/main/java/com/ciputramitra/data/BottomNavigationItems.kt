package com.ciputramitra.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItems(
	val title: String,
	val selectedIcon: ImageVector,
	val unSelectedIcon: ImageVector,
	
	val selectedIconColor: Color,
	val unSelectedIconColor: Color,
	
	val selectedColor: Color,
	val unSelectedColor: Color,
	val badgeCount: Int? = null,
	val routeScreen: String
)