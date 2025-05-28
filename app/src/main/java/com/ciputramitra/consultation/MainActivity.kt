package com.ciputramitra.consultation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ciputramitra.consultation.ui.auth.AuthViewModel
import com.ciputramitra.consultation.ui.doctor.DoctorViewModel
import com.ciputramitra.consultation.ui.polyclinic.PolyclinicViewModel
import com.ciputramitra.navgraph.NavGraph
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
	private val splashViewModel by viewModels<SplashViewModel>()
	private val authViewModel : AuthViewModel by viewModel()
	private val polyclinicViewModel: PolyclinicViewModel by viewModel()
	private val doctorViewModel: DoctorViewModel by viewModel()
//	private val promptManager by lazy {
//		BiometricPromptManager(this)
//	}
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		installSplashScreen().apply {
			setKeepOnScreenCondition {
				!splashViewModel.isReady.value
			}
		}
		
		setContent {
			NavGraph(
				authViewModel =  authViewModel,
				polyclinicViewModel = polyclinicViewModel,
				doctorViewModel = doctorViewModel
			)
		}
	}
}




