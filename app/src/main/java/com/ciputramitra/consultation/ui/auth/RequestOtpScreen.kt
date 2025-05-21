package com.ciputramitra.consultation.ui.auth

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ciputramitra.component.LoadingLottieAnimation
import com.ciputramitra.consultation.R
import com.ciputramitra.consultation.ui.theme.greenColor
import com.ciputramitra.consultation.ui.theme.poppinsBold
import com.ciputramitra.consultation.ui.theme.poppinsMedium
import com.ciputramitra.domain.state.StateManagement
import com.ciputramitra.navgraph.ResetPasswordArgs
import kotlinx.coroutines.delay

@SuppressLint("DefaultLocale")
@Composable
fun RequestOtpScreen(
	authViewModel : AuthViewModel ,
	email : String ,
	navController : NavController
) {
	
	val verifyOtpState by authViewModel.authState.collectAsStateWithLifecycle()
	val otpValues = remember { List(6) { mutableStateOf("") } }
	val focusRequesters = remember { List(6) { FocusRequester() } }
	val remainingTime = remember { mutableIntStateOf(180) }
	val formattedTime = remember(remainingTime.intValue) {
		val minutes = remainingTime.intValue / 60
		val seconds = remainingTime.intValue % 60
		String.format("%02d:%02d", minutes, seconds)
	}
	
	val isCodeComplete = remember {
		derivedStateOf {
			otpValues.all { it.value.isNotEmpty() }
		}
	}
	
	val context = LocalContext.current
	
	LaunchedEffect(key1 = Unit) {
		while (remainingTime.intValue > 0) {
			delay(1000)
			remainingTime.intValue--
		}
		navController.navigateUp()
	}
	
	val getCompleteOtp = {
		otpValues.joinToString("") { it.value }
	}
	
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(color = Color.White)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			
			Row (
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 16.dp),
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
			
			Spacer(modifier = Modifier.height(16.dp))
			
			AsyncImage(
				model = ImageRequest.Builder(context = LocalContext.current)
					.data(R.drawable.logo)
					.error(R.drawable.logo)
					.build(),
				contentDescription = null,
				modifier = Modifier
					.size(100.dp),
				contentScale = ContentScale.Crop
			)
			
			Spacer(modifier = Modifier.height(24.dp))
			
			
			Text(
				text = "Verifikasi Kode",
				fontFamily = poppinsBold,
				fontSize = 24.sp,
				fontWeight = FontWeight.SemiBold,
				color = Color.Black
			)
			
			Spacer(modifier = Modifier.height(8.dp))
			
			Text(
				text = "Masukkan kode 6 digit yang telah dikirim ke",
				fontFamily = poppinsMedium,
				fontSize = 14.sp,
				color = Color.Gray,
				textAlign = TextAlign.Center
			)
			
			Text(
				modifier = Modifier
					.padding(top = 5.dp),
				text = email,
				fontFamily = poppinsMedium,
				fontSize = 14.sp,
				color = Color.Black,
				textAlign = TextAlign.Center
			)
			
			Spacer(modifier = Modifier.height(40.dp))
			
			Row (
				modifier = Modifier
					.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				otpValues.forEachIndexed { index, otpValue ->
					OtpDigitField(
						value = otpValue.value,
						onValueChange = { newValue ->
							if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
								otpValue.value = newValue
								
								
								if (newValue.isNotEmpty() && index < 5) {
									focusRequesters[index + 1].requestFocus()
								}
							}
						},
						modifier = Modifier
							.weight(1f)
							.padding(horizontal = 4.dp)
							.focusRequester(focusRequesters[index]),
						isFocused = index == otpValues.indexOfFirst { it.value.isEmpty() } ||
								(index == 5 && otpValues[4].value.isNotEmpty()),
						onKeyEvent = { keyEvent ->
							
							if (keyEvent.key == Key.Backspace && otpValue.value.isEmpty() && index > 0) {
								focusRequesters[index - 1].requestFocus()
								otpValues[index - 1].value = ""
								true
							} else {
								false
							}
						},
						isLastField = index == 5
					)
				}
			}
			
			Spacer(modifier = Modifier.height(32.dp))
			
			
			Text(
				text = if (remainingTime.intValue > 0)
					"Kode akan kedaluwarsa dalam"
				else "Kode telah kedaluwarsa",
				fontFamily = poppinsMedium,
				fontSize = 14.sp,
				color = if (remainingTime.intValue > 0) Color.Gray else Color.Red
			)
			
			Text(
				text = formattedTime,
				fontFamily = poppinsMedium,
				fontSize = 18.sp,
				color = if (remainingTime.intValue > 0) greenColor else Color.Red,
				fontWeight = FontWeight.SemiBold
			)
			
			Spacer(modifier = Modifier.height(32.dp))
			
			Button(
				onClick = {
					val completeOtp = getCompleteOtp()
					authViewModel.verifyOtp(
						email = email,
						otp = completeOtp
					)
				},
				modifier = Modifier
					.fillMaxWidth()
					.height(56.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = if (isCodeComplete.value) greenColor else Color.Gray.copy(alpha = 0.5f),
					contentColor = Color.White
				),
				shape = RoundedCornerShape(28.dp) ,
				enabled = isCodeComplete.value && remainingTime.intValue > 0
			) {
				Text(
					text = "Verifikasi",
					fontFamily = poppinsMedium,
					fontSize = 18.sp
				)
			}
		}
		
		Box(
			modifier = Modifier.
			fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			when(val state = verifyOtpState) {
				is StateManagement.Loading  -> LoadingLottieAnimation()
				is StateManagement.Error -> Toast.makeText( context, state.message , Toast.LENGTH_SHORT).show()
				is StateManagement.VerifyOtpSuccess ->  {
					Toast.makeText( context, "Silahkan melanjutkan Reset Password" , Toast.LENGTH_SHORT)
						.show()
					navController.navigate(
						route = ResetPasswordArgs(
							userID = state.otpRequestResponse.id
						)
					)
				}
				else -> authViewModel.clearAuthState()
			}
		}
	}
	
	
}

@Composable
fun OtpDigitField(
	value: String ,
	onValueChange: (String) -> Unit ,
	modifier: Modifier = Modifier ,
	isFocused: Boolean = false ,
	onKeyEvent: (KeyEvent) -> Boolean = { false } ,
	isLastField: Boolean = false
) {
	val focusManager = LocalFocusManager.current
	val focusRequester = remember { FocusRequester() }
	val keyboardController = LocalSoftwareKeyboardController.current
	
	BasicTextField(
		value = value,
		onValueChange = { newValue ->
			if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
				onValueChange(newValue)
				
				// Jika ini adalah field terakhir dan nilai baru dimasukkan, sembunyikan keyboard
				if (isLastField && newValue.isNotEmpty()) {
					keyboardController?.hide()
					// Hapus fokus dari semua field
					focusManager.clearFocus()
				}
			}
		},
		modifier = modifier
			.focusRequester(focusRequester)
			.onKeyEvent { onKeyEvent(it) },
		textStyle = TextStyle(
			fontSize = 24.sp,
			fontFamily = poppinsMedium,
			fontWeight = FontWeight.SemiBold,
			color = Color.Black,
			textAlign = TextAlign.Center
		) ,
		keyboardOptions = KeyboardOptions(
			keyboardType = KeyboardType.Number,
			imeAction = if (isLastField) ImeAction.Done else ImeAction.Next
		) ,
		keyboardActions = KeyboardActions(
			onNext = {
				focusManager.moveFocus(FocusDirection.Next)
			},
			onDone = {
				keyboardController?.hide()
				focusManager.clearFocus()
			}
		) ,
		singleLine = true,
		cursorBrush = SolidColor(greenColor) ,
		decorationBox = { innerTextField ->
			Box(
				modifier = Modifier
					.size(width = 50.dp , height = 60.dp)
					.background(
						color = if (value.isNotEmpty()) Color(0xFFF0F8FF) else Color(0xFFF9F9F9) ,
						shape = RoundedCornerShape(8.dp)
					)
					.border(
						width = if (value.isNotEmpty() || isFocused) 2.dp else 1.dp ,
						color = if (value.isNotEmpty() || isFocused) greenColor else Color(
							0xFFE2E8F0
						) ,
						shape = RoundedCornerShape(8.dp)
					),
				contentAlignment = Alignment.Center
			) {
				innerTextField()
				
				// Mostrar cursor personalizado cuando está vacío y tiene foco
				if (value.isEmpty() && isFocused) {
					HorizontalDivider(
						color = greenColor,
						modifier = Modifier
							.height(24.dp)
							.width(2.dp)
							.align(Alignment.Center)
					)
				}
			}
		}
	)
	
	// Solicitar foco si es el primer campo vacío
	LaunchedEffect(isFocused) {
		if (isFocused) {
			focusRequester.requestFocus()
		}
	}
}