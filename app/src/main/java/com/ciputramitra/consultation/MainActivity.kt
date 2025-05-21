package com.ciputramitra.consultation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ciputramitra.consultation.ui.auth.AuthViewModel
import com.ciputramitra.navgraph.NavGraph
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
	private val splashViewModel by viewModels<SplashViewModel>()
	private val authViewModel : AuthViewModel by viewModel()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		installSplashScreen().apply {
			setKeepOnScreenCondition {
				!splashViewModel.isReady.value
			}
		}
		
		setContent {
			requestNotificationPermission()
			NavGraph(
				authViewModel = authViewModel
			)
		}
	}
	
	private fun requestNotificationPermission() {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			val hasPermission = ContextCompat.checkSelfPermission(
				this,
				Manifest.permission.POST_NOTIFICATIONS
			) == PackageManager.PERMISSION_GRANTED
			
			if(!hasPermission) {
				ActivityCompat.requestPermissions(
					this,
					arrayOf(Manifest.permission.POST_NOTIFICATIONS),
					0
				)
			}
		}
	}
}

