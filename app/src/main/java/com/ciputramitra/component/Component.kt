package com.ciputramitra.component

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Medication
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ciputramitra.consultation.R
import com.ciputramitra.consultation.ui.theme.greenColor
import com.ciputramitra.consultation.ui.theme.textColor
import com.ciputramitra.data.BottomNavigationItems
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun FormTextField(
	value : String,
	onValueChange: (String) -> Unit,
	label : String,
	error : Boolean,
	leadingIcon: ImageVector,
	keyboardType: KeyboardType = KeyboardType.Text,
	isPasswordField : Boolean = false,
	isVisible: Boolean = false,
	onVisibilityChange: ((Boolean) -> Unit)? = null,
	singleLine: Boolean = false,
	prefix: @Composable (() -> Unit) ? = null,
	suffix: @Composable (() -> Unit) ? = null,
	placeholder: @Composable (() -> Unit)? = null,
	enable: Boolean = true
) {
	OutlinedTextField(
		value = value,
		onValueChange = onValueChange,
		label = {
			Text(
				text = label
			)
		},
		leadingIcon =  {
			Icon(
				imageVector = leadingIcon,
				contentDescription = null,
				modifier = Modifier.size(18.dp),
				tint = greenColor
			)
		},
		trailingIcon = {
			if (isPasswordField)
				IconButton(
					onClick = {
						onVisibilityChange?.invoke(!isVisible)
					}
				) {
					Icon(
						imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
						contentDescription = null
					)
				}
		},
		visualTransformation = if (isPasswordField && !isVisible) PasswordVisualTransformation() else VisualTransformation.None,
		keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
		isError = error,
		modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
		shape = RoundedCornerShape(18.dp),
		singleLine = singleLine,
		prefix = prefix,
		suffix = suffix,
		placeholder = placeholder,
		enabled = enable
	)
	if (error) {
		Text(
			modifier = Modifier
				.padding(horizontal = 20.dp),
			text = "Invalid $label",
			color = MaterialTheme.colorScheme.error,
			fontSize = 12.sp
		)
	}
}

@Composable
fun FormCheckBox(
	checked : Boolean,
	onCheckedChange : (Boolean) -> Unit,
	error: Boolean,
	label: String
) {
	val context = LocalContext.current
	Row(
		modifier = Modifier
			.padding(horizontal = 14.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Checkbox(
			checked = checked,
			onCheckedChange = onCheckedChange,
			colors = CheckboxDefaults.colors(
				uncheckedColor = Color.Gray
			)
		)
		Text(
			text = label
		)
	}
	if (error) {
		Toast.makeText(context, "Menyetujui semua persyaratan", Toast.LENGTH_SHORT).show()
	}
}

@Composable
fun bottomNavigation(): List<BottomNavigationItems> {
	return listOf(
		BottomNavigationItems(
			title = "Beranda",
			selectedIcon = Icons.Rounded.Home,
			unSelectedIcon = Icons.Outlined.Home,
			selectedIconColor = greenColor,
			unSelectedIconColor = Color.Gray,
			selectedColor = Color.Black,
			unSelectedColor = Color.LightGray,
			badgeCount = 0,
			routeScreen = "/Home"
		),
		BottomNavigationItems(
			title = "Artikel",
			selectedIcon = Icons.Rounded.Newspaper,
			unSelectedIcon = Icons.Outlined.Newspaper,
			selectedIconColor = greenColor,
			unSelectedIconColor = Color.Gray,
			selectedColor = Color.Black,
			unSelectedColor = Color.LightGray,
			badgeCount = 0,
			routeScreen = "/Info"
		),
		BottomNavigationItems(
			title = "Riwayat",
			selectedIcon = Icons.Rounded.Medication,
			unSelectedIcon = Icons.Outlined.Medication,
			selectedIconColor = greenColor,
			unSelectedIconColor = Color.Gray,
			selectedColor = Color.Black,
			unSelectedColor = Color.LightGray,
			badgeCount = 0,
			routeScreen = "/Payment"
		),
		BottomNavigationItems(
			title = "Profil",
			selectedIcon = Icons.Rounded.AccountCircle,
			unSelectedIcon = Icons.Outlined.AccountCircle,
			selectedIconColor = greenColor,
			unSelectedIconColor = Color.Gray,
			selectedColor = Color.Black,
			unSelectedColor = Color.LightGray,
			badgeCount = 0,
			routeScreen = "/profile"
		)
	)
}

@Composable
fun LoadingLottieAnimation(
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.fillMaxWidth(),
		contentAlignment = Alignment.Center
	) {
		val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading))
		val progress by animateLottieCompositionAsState(
			composition = composition,
			iterations = LottieConstants.IterateForever
		)
		LottieAnimation(
			composition = composition,
			progress = { progress },
			modifier = modifier.size(200.dp)
		)
	}
}

@Composable
fun InformationBusy(
	message: String,
	onRetryClick: () -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(24.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(12.dp)
	) {
		Text(
			text = message
		)
		Button(
			onClick = onRetryClick
		) {
			Text(
				text = "Coba lagi setelah 30 detik"
			)
		}
	}
}

@Composable
fun PageIndicator(
	pageCount: Int,
	currentPage: Int,
	modifier: Modifier
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		repeat(pageCount) {
			val size = animateDpAsState(
				targetValue =
					if (it == currentPage)
						12.dp else 10.dp, label = ""
			)
			Box(
				modifier = modifier
					.padding(2.dp)
					.size(size.value)
					.clip(CircleShape)
					.background(
						if (it == currentPage) greenColor else textColor
					)
			)
		}
	}
}

@Composable
fun RatingBarSmall(
	rating: Float,
	modifier: Modifier,
	starCount: Int = 5,
	starSize: Dp = 16.dp,
	spaceBetween: Dp = 2.dp
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(spaceBetween),
		verticalAlignment = Alignment.CenterVertically
	) {
		for (i in 1 .. starCount) {
			val icon =
				when {
					i <= rating -> Icons.Default.Star
					1 <= rating + 0.5 -> Icons.Outlined.StarRate
					else -> Icons.Default.StarOutline
				}
			Icon(
				modifier = Modifier.size(starSize),
				imageVector = icon,
				contentDescription = null,
				tint = colorResource(id = R.color.yellow)
			)
		}
	}
}

@Composable
fun formatDate(dateString: String): String {
	try {
		// Parse the original date format
		val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("id", "ID"))
		val date = originalFormat.parse(dateString)
		
		// Format to the desired output
		val targetFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
		return date?.let { targetFormat.format(it) } ?: dateString
	} catch (e: Exception) {
		return dateString
	}
}

@Composable
fun GenderRadioGroupStyled(
	selectedGender: String,
	onGenderSelected: (String) -> Unit
) {
	val genderOptions = listOf("Pria", "Perempuan")
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 8.dp)
	) {
		Text(
			text = "Pilih Jenis Kelamin",
			style = MaterialTheme.typography.titleMedium,
			modifier = Modifier.padding(bottom = 12.dp)
		)
		
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 16.dp),
			horizontalArrangement = Arrangement.spacedBy(12.dp)
		) {
			genderOptions.forEach { gender ->
				val isSelected = gender == selectedGender
				val backgroundColor = if (isSelected)
					MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
				else
					MaterialTheme.colorScheme.surface
				
				Box(
					modifier = Modifier
						.weight(1f)
						.clip(RoundedCornerShape(8.dp))
						.background(backgroundColor)
						.border(
							width = 1.dp,
							color = if (isSelected) MaterialTheme.colorScheme.primary
							else MaterialTheme.colorScheme.outline,
							shape = RoundedCornerShape(8.dp)
						)
						.clickable { onGenderSelected(gender) }
						.padding(vertical = 12.dp),
					contentAlignment = Alignment.Center
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.Center
					) {
						RadioButton(
							selected = isSelected,
							onClick = null, // Click dihandle oleh parent Box
							colors = RadioButtonDefaults.colors(
								selectedColor = MaterialTheme.colorScheme.primary
							)
						)
						Spacer(modifier = Modifier.width(8.dp))
						Text(
							text = gender,
							style = MaterialTheme.typography.bodyMedium,
							color = if (isSelected) MaterialTheme.colorScheme.primary
							else MaterialTheme.colorScheme.onSurface
						)
					}
				}
			}
		}
	}
}