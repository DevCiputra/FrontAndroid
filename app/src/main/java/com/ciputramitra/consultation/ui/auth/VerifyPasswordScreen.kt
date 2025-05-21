package com.ciputramitra.consultation.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.rounded.ArrowCircleLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ciputramitra.component.FormTextField
import com.ciputramitra.component.LoadingLottieAnimation
import com.ciputramitra.consultation.R
import com.ciputramitra.consultation.ui.theme.greenColor
import com.ciputramitra.consultation.ui.theme.poppinsBold
import com.ciputramitra.consultation.ui.theme.poppinsMedium
import com.ciputramitra.consultation.ui.theme.whiteCustom
import com.ciputramitra.domain.state.StateManagement
import com.ciputramitra.domain.state.ValidationEmailVerify
import com.ciputramitra.navgraph.VerifyOtpArgs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyPasswordScreen(
	authViewModel : AuthViewModel ,
	navController : NavController
) {
	val validationEmailVerify: ValidationEmailVerify = viewModel()
	val verifyEmailState by authViewModel.authState.collectAsStateWithLifecycle()
	val context = LocalContext.current
	
	
	
	
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(color = Color.White),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(32.dp)
	) {
		stickyHeader {
			TopAppBar(
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = greenColor,
					navigationIconContentColor = whiteCustom
				),
				title = {
					Text(
						text = "Verifikasi email anda",
						fontFamily = poppinsMedium,
						fontWeight = FontWeight.Medium,
						color = whiteCustom
					)
				},
				navigationIcon = {
					IconButton(
						onClick = {
							navController.navigateUp()
						}
					) {
						Icon(
							imageVector = Icons.Rounded.ArrowCircleLeft,
							contentDescription = "Arrow Back"
						)
					}
				}
			)
		}
		item {
			AsyncImage(
				model = ImageRequest.Builder(context = LocalContext.current)
					.data(R.drawable.otp)
					.error(R.drawable.logo)
					.build(),
				contentDescription = null,
				modifier = Modifier
					.size(70.dp)
					.clip(shape = RoundedCornerShape(8.dp)),
				contentScale = ContentScale.Crop
			)
			
			Text(
				modifier = Modifier.padding(top = 7.dp),
				text = "VERIFIKASI EMAIL",
				fontFamily = poppinsBold,
				fontSize = 30.sp,
				fontWeight = FontWeight.Bold,
				color = Color.Black,
				textAlign = TextAlign.Center
			)
			
			Text(
				modifier = Modifier.padding(horizontal = 16.dp),
				text = "masukan email yang terdaftar\nuntuk mengirim kode otp ini",
				fontFamily = poppinsMedium,
				color = Color.Gray,
				fontSize = 13.sp,
				textAlign = TextAlign.Center
			)
			
			FormTextField(
				value = validationEmailVerify.email.value,
				onValueChange = {
					validationEmailVerify.email = validationEmailVerify.email.copy(value = it)
				},
				label = "Email",
				placeholder = {
					Text(
						text = "Masuk email yang terdaftar",
						fontFamily = poppinsMedium,
						fontSize = 11.sp,
						color = Color.Gray
					)
				},
				error = validationEmailVerify.email.showError,
				leadingIcon = Icons.Default.MarkEmailUnread,
				keyboardType = KeyboardType.Email,
				singleLine = true,
				suffix = {
					Text(
						text = ".com",
						fontSize = 12.sp,
						color = Color.LightGray
					)
				}
			)
			
			Button(
				onClick = {
					if (validationEmailVerify.validateForm()) {
						authViewModel.verifyEmail(
							email = validationEmailVerify.email.value
						)
					}
					
					
				},
				modifier = Modifier
					.fillMaxWidth()
					.height(56.dp)
					.padding(horizontal = 16.dp , vertical = 8.dp)
					.shadow(elevation = 4.dp , shape = RoundedCornerShape(28.dp)),
				colors = ButtonDefaults.buttonColors(
					containerColor = greenColor, // Tu color verde definido
					contentColor = Color.White
				),
				shape = RoundedCornerShape(28.dp)
			) {
				Row  (
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.Center
				) {
					Icon(
						imageVector = Icons.Default.VerifiedUser,
						contentDescription = null,
						modifier = Modifier.size(20.dp)
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text(
						text = "Kirim OTP Sekarang",
						fontFamily = poppinsMedium,
						fontSize = 16.sp,
						fontWeight = FontWeight.SemiBold
					)
				}
			}
		}
		
		item {
			when(val state = verifyEmailState) {
				is StateManagement.Loading -> LoadingLottieAnimation()
				is StateManagement.Error -> Toast.makeText(context , state.message , Toast.LENGTH_SHORT).show()
				is StateManagement.VerifyEmailSuccess -> {
					Toast.makeText(
						context ,
						"Kode OTP telah dikirim ke email Anda",
						Toast.LENGTH_SHORT
					).show()
					
					navController.navigate(
						route = VerifyOtpArgs(
							email = validationEmailVerify.email.value
						)
					)
				}
				else -> authViewModel.clearAuthState()
			}
		}
	}
	
	
	
}