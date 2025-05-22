package com.ciputramitra.consultation.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ciputramitra.component.FormTextField
import com.ciputramitra.component.LoadingLottieAnimation
import com.ciputramitra.consultation.R
import com.ciputramitra.consultation.ui.theme.black
import com.ciputramitra.consultation.ui.theme.greenColor
import com.ciputramitra.consultation.ui.theme.poppinsBold
import com.ciputramitra.consultation.ui.theme.poppinsLight
import com.ciputramitra.consultation.ui.theme.whiteCustom
import com.ciputramitra.domain.state.StateManagement
import com.ciputramitra.domain.state.ValidationPassword
import com.ciputramitra.navgraph.Login

@Composable
fun ResetPasswordScreen(
	authViewModel : AuthViewModel ,
	navController : NavHostController ,
	userID : Int ,
	onResetSuccess : () -> Unit
) {
	
	val validationPassword: ValidationPassword = viewModel()
	val passwordState by authViewModel.authState.collectAsStateWithLifecycle()
	val context = LocalContext.current
	
	// State untuk mengecek apakah password dan konfirmasi password cocok
	val passwordsMatch = validationPassword.password.value == validationPassword.passwordConfirmation.value
	
	
	
	Box(
		modifier = Modifier
			.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		
		Row (
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 16.dp)
				.align(Alignment.TopStart),
			horizontalArrangement = Arrangement.Start
		) {
			IconButton(
				onClick = {
					navController.navigateUp()
					authViewModel.clearAuthState()
				},
				modifier = Modifier
					.size(48.dp)
					.background(
						color = Color(0xFFF9F9F9) ,
						shape = CircleShape
					)
			) {
				Icon(
					imageVector = Icons.Default.ArrowCircleLeft,
					contentDescription = "Volver",
					tint = Color.Black
				)
			}
		}
		
		Column(
			modifier = Modifier
				.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(7.dp)
		) {
			
			AsyncImage(
				model = ImageRequest.Builder(context = LocalContext.current)
					.data(R.drawable.reset)
					.error(R.drawable.logo)
					.build(),
				contentDescription = null,
				modifier = Modifier
					.size(80.dp),
				contentScale = ContentScale.Crop
			)
			
			Text(
				text = "Reset password",
				fontFamily = poppinsBold,
				fontSize = 16.sp,
				fontWeight = FontWeight.SemiBold,
				color = black
			)
			
			Text(
				modifier = Modifier.padding(horizontal = 14.dp),
				text = "Pastikan Anda memilih kata sandi\nyang aman dan mudah diingat",
				fontFamily = poppinsLight,
				fontSize = 12.sp,
				color = Color.Gray,
				textAlign = TextAlign.Center
			)
			
			FormTextField(
				value = validationPassword.password.value,
				onValueChange = {
					validationPassword.password = validationPassword.password.copy(
						value = it
					)
				},
				label = "Password baru",
				placeholder = {
					Text(
						text = "Minimal 6 karakter"
					)
				},
				error = validationPassword.password.showError,
				leadingIcon = Icons.Default.Fingerprint,
				keyboardType = KeyboardType.Password,
				isPasswordField = true,
				isVisible = validationPassword.password.isVisible,
				onVisibilityChange = {
					validationPassword.password = validationPassword.password.copy(isVisible = it)
				},
				singleLine = true
			)
			
			FormTextField(
				value = validationPassword.passwordConfirmation.value,
				onValueChange = {
					validationPassword.passwordConfirmation = validationPassword.passwordConfirmation.copy(
						value = it
					)
				},
				label = "Password Confirmation",
				placeholder = {
					Text(
						text = "Password Confirmation anda"
					)
				},
				error = validationPassword.passwordConfirmation.showError ||
						(validationPassword.passwordConfirmation.value.isNotEmpty() && !passwordsMatch),
				leadingIcon = Icons.Default.Fingerprint,
				keyboardType = KeyboardType.Password,
				isPasswordField = true,
				isVisible = validationPassword.passwordConfirmation.isVisible,
				onVisibilityChange = {
					validationPassword.passwordConfirmation = validationPassword.passwordConfirmation.copy(isVisible = it)
				},
				singleLine = true
			)
			
			
			if (validationPassword.passwordConfirmation.value.isNotEmpty() && !passwordsMatch) {
				Text(
					text = "Password konfirmasi tidak cocok dengan password baru",
					color = Color.Red,
					fontSize = 12.sp,
					fontFamily = poppinsLight,
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 16.dp)
				)
			}
			
			Button(
				onClick = {
					if (validationPassword.validateForm() && passwordsMatch) {
						authViewModel.resetPassword(
							id = userID,
							password = validationPassword.password.value
						)
						navController.navigate(
							route = Login
						)
					} else if (!passwordsMatch) {
						// Tampilkan toast jika password tidak cocok
						Toast.makeText(
							context,
							"Password konfirmasi harus sama dengan password baru",
							Toast.LENGTH_SHORT
						).show()
					}
				},
				colors = ButtonDefaults.buttonColors(
					contentColor = whiteCustom,
					containerColor = greenColor
				)
			) {
				Text(
					text = "Reset password sekarang"
				)
			}
		}
		
		when(val state = passwordState) {
			is StateManagement.Loading -> LoadingLottieAnimation()
			is StateManagement.Error -> Toast.makeText(context , state.message , Toast.LENGTH_SHORT).show()
			is StateManagement.ResetPasswordSuccess ->  {
				Toast.makeText(context , state.message , Toast.LENGTH_SHORT).show()
			}
			else -> authViewModel.clearAuthState()
		}
	}

}