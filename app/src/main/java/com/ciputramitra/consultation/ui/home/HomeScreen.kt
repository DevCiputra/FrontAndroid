package com.ciputramitra.consultation.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.ciputramitra.response.auth.User
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
	navController : NavHostController ,
	fetchUser : User?
) {
	
	val context = LocalContext.current
	
	// Launcher untuk request permission - Android akan menampilkan dialog bawaan
	val notificationPermissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission()
	) { isGranted ->
		if (isGranted)
			Toast.makeText(context , "Notification active" , Toast.LENGTH_SHORT).show()
		else Toast.makeText(context , "Notification denied" , Toast.LENGTH_SHORT).show()
	}
	
	// Request permission saat HomeScreen pertama kali dimuat
	LaunchedEffect(Unit) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			val hasPermission = ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.POST_NOTIFICATIONS
			) == PackageManager.PERMISSION_GRANTED
			
			if (!hasPermission) {
				// Delay sebentar agar user tidak langsung diminta permission
				delay(1000)
				notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
			}
		}
	}
}