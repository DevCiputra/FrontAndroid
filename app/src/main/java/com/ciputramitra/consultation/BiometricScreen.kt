package com.ciputramitra.consultation

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ciputramitra.BiometricPromptManager
import com.ciputramitra.consultation.ui.theme.black
import com.ciputramitra.consultation.ui.theme.greenColor
import com.ciputramitra.consultation.ui.theme.poppinsBold
import com.ciputramitra.consultation.ui.theme.poppinsMedium
import com.ciputramitra.consultation.ui.theme.whiteCustom

@Composable
fun BiometricScreen(promptManager : BiometricPromptManager , onAuthSuccess : () -> Unit) {
	
	val context = LocalContext.current
	val biometricResult by promptManager.promptResult.collectAsStateWithLifecycle(initialValue = null)
	val enrollLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.StartActivityForResult(),
		onResult = {
		
		}
	)
	LaunchedEffect(biometricResult) {
		if (biometricResult is BiometricPromptManager.BiometricResult.AuthenticationNotSet) {
			if (Build.VERSION.SDK_INT >= 30) {
				val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
					putExtra(
						Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
						BiometricManager.Authenticators.BIOMETRIC_STRONG or DEVICE_CREDENTIAL
					)
				}
				enrollLauncher.launch(enrollIntent)
			}
		}
	}
	
	Column(
		modifier = Modifier
			.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		
		AsyncImage(
			model = ImageRequest.Builder(context = LocalContext.current)
				.data(R.drawable.face)
				.error(R.drawable.logo)
				.build(),
			contentDescription = null,
			modifier = Modifier
				.size(100.dp),
			contentScale = ContentScale.Crop
		)
		
		Text(
			modifier = Modifier.padding(10.dp),
			text = "Autentikasi Biometrik",
			fontFamily = poppinsBold,
			fontSize = 15.sp,
			fontWeight = FontWeight.Medium,
			textAlign = TextAlign.Center,
			color = black
		)
		
		Text(
			modifier = Modifier.padding(bottom = 12.dp),
			text = "Gunakan sidik jari atau wajah\nAnda untuk masuk ke aplikasi",
			fontFamily = poppinsMedium,
			fontSize = 12.sp,
			fontWeight = FontWeight.Medium,
			textAlign = TextAlign.Center,
			color = Color.Gray
		)
		
		Button(
			colors = ButtonDefaults.buttonColors(
				containerColor = greenColor,
				contentColor = whiteCustom
			),
			onClick = {
				promptManager.showBiometricPrompt(
					title = "Confirmation Indentitas",
					description = "Gunakan sidik jari atau wajah\nAnda untuk masuk ke aplikasi"
				)
			}
		) {
			Text(
				text = "Authentication",
				fontFamily = poppinsBold,
				fontSize = 14.sp,
				fontWeight = FontWeight.SemiBold
			)
		}
		
		biometricResult?.let { result ->
			when(result) {
				is BiometricPromptManager.BiometricResult.AuthenticationError -> {
					Toast.makeText( context, result.error , Toast.LENGTH_SHORT).show()
				}
				is BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
					Toast.makeText( context, "Authentication Failed" , Toast.LENGTH_SHORT).show()
				}
				is BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
					Toast.makeText( context, "Authentication Not set" , Toast.LENGTH_SHORT).show()
				}
				is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
					Toast.makeText( context, "Authentication Successfully" , Toast.LENGTH_SHORT).show()
					onAuthSuccess()
				}
				is BiometricPromptManager.BiometricResult.FeatureUnavailable -> {
					Toast.makeText( context, "Authentication Failure" , Toast.LENGTH_SHORT).show()
				}
				is BiometricPromptManager.BiometricResult.HardwareUnavailable -> {
					Toast.makeText( context, "Authentication Hardware" , Toast.LENGTH_SHORT).show()
				}
			}
			
		}
	}
}