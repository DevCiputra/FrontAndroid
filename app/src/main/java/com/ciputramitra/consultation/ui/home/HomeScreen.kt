package com.ciputramitra.consultation.ui.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ciputramitra.component.PageIndicator
import com.ciputramitra.consultation.R
import com.ciputramitra.consultation.ui.theme.black
import com.ciputramitra.consultation.ui.theme.greenColor
import com.ciputramitra.consultation.ui.theme.poppinsLight
import com.ciputramitra.consultation.ui.theme.poppinsMedium
import com.ciputramitra.consultation.ui.theme.whiteCustom
import com.ciputramitra.data.CategoryItems
import com.ciputramitra.navgraph.Polyclinic
import com.ciputramitra.response.auth.User
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
	navController : NavHostController ,
	fetchUser : User?
) {
	
	val pagerState = rememberPagerState(
		pageCount = {
			3
		}
	)
	
	
	LaunchedEffect(key1 = Unit) {
		while (true) {
			delay(3000)
			val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
			pagerState.animateScrollToPage(
				nextPage, pageOffsetFraction = 0f
			)
		}
	}
	
	val image = remember {
		mutableStateListOf(
			"https://firebasestorage.googleapis.com/v0/b/kakaarab-4bfcf.appspot.com/o/1banner.jpg?alt=media&token=5e027330-2747-4166-99ca-4652bf398573",
			"https://firebasestorage.googleapis.com/v0/b/kakaarab-4bfcf.appspot.com/o/banner2.jpg?alt=media&token=5a351413-7a1b-4cbd-9f53-4ec864e77979",
			"https://firebasestorage.googleapis.com/v0/b/kakaarab-4bfcf.appspot.com/o/banner3.png?alt=media&token=ceba3cbf-1bbf-4e17-87b0-e86e1d13bb32"
		)
	}
	
	val listCategory = listOf(
		CategoryItems(0,"Konsultasi\nOnline", R.drawable.consultation_online),
		CategoryItems(1,"Medical\ncheck up", R.drawable.medical_check_up),
		CategoryItems(2,"Home\nservice", R.drawable.homeservice),
		CategoryItems(3,"Daftar\npoliklinik", R.drawable.polyclinic)
		
	)
	
	val context = LocalContext.current
	
	// State untuk System Alert Window Permission
	var hasSystemAlertPermission by remember {
		mutableStateOf(
			Settings.canDrawOverlays(context)
		)
	}
	
	// Launcher untuk System Alert Window Permission
	val systemAlertPermissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.StartActivityForResult()
	) { _ ->
		// Check apakah permission sudah diberikan setelah kembali dari Settings
		hasSystemAlertPermission = Settings.canDrawOverlays(context)
		if (hasSystemAlertPermission) {
			Toast.makeText(context, "System Alert Window permission granted", Toast.LENGTH_SHORT).show()
		} else {
			Toast.makeText(context, "System Alert Window permission denied", Toast.LENGTH_SHORT).show()
		}
	}
	
	// Notification Permission State (untuk Android 13+)
	val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
		rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
	} else null
	
	// Dialog state untuk explain permission
	var showSystemAlertDialog by remember { mutableStateOf(false) }
	
	
	rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission()
	) { isGranted ->
		if (isGranted)
			Toast.makeText(context , "Notification active" , Toast.LENGTH_SHORT).show()
		else Toast.makeText(context , "Notification denied" , Toast.LENGTH_SHORT).show()
	}
	
	// Request permissions saat HomeScreen pertama kali dimuat
	LaunchedEffect(Unit) {
		delay(1000) // Delay sebentar agar user experience lebih baik
		
		// Check dan request System Alert Window permission
		if (!Settings.canDrawOverlays(context)) {
			showSystemAlertDialog = true
		}
		
		// Request notification permission untuk Android 13+
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			notificationPermissionState?.let { state ->
				if (!state.status.isGranted && !state.status.shouldShowRationale) {
					state.launchPermissionRequest()
				}
			}
		}
	}
	
	
	// Handle notification permission result
	notificationPermissionState?.let { state ->
		when (state.status) {
			is PermissionStatus.Granted -> {
				// Permission granted, bisa lanjut
			}
			is PermissionStatus.Denied -> {
				if (state.status.shouldShowRationale) {
					// Show rationale dialog
					LaunchedEffect(Unit) {
						delay(500)
						state.launchPermissionRequest()
					}
				}
			}
		}
	}
	
	// Dialog untuk explain System Alert Window permission
	if (showSystemAlertDialog) {
		AlertDialog(
			onDismissRequest = { showSystemAlertDialog = false },
			title = { Text("Permission Required") },
			text = {
				Text("We need your consent for the System Alert Window permission in order to use the offline call function properly")
			},
			confirmButton = {
				TextButton (
					onClick = {
						showSystemAlertDialog = false
						// Launch Settings untuk System Alert Window permission
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
							val intent = Intent(
								Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
								Uri.parse("package:${context.packageName}")
							)
							systemAlertPermissionLauncher.launch(intent)
						}
					}
				) {
					Text("Allow")
				}
			},
			dismissButton = {
				TextButton(
					onClick = { showSystemAlertDialog = false }
				) {
					Text("Deny")
				}
			}
		)
	}
	
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(color = Color.White),
		verticalArrangement = Arrangement.spacedBy(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		stickyHeader {
			Row(
				modifier = Modifier
					.fillMaxSize()
					.background(color = greenColor)
					.padding(10.dp),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = "CIPUTRA MITRA HOSPITAL\nBANJARMASIN",
					fontFamily = poppinsMedium,
					fontWeight = FontWeight.SemiBold,
					fontSize = 12.sp,
					color = whiteCustom
				)
				
				Box(
					modifier = Modifier
						.size(30.dp)
						.background(whiteCustom, CircleShape)
						.clip(CircleShape),
					contentAlignment = Alignment.Center
				) {
					Text(
						text = fetchUser?.username?.first().toString(),
						fontSize = 16.sp,
						fontWeight = FontWeight.Medium,
						color = black
					)
				}
			}
			
			
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.background(color = Color.White)
					.padding(start = 14.dp, end = 14.dp, top = 18.dp, bottom = 8.dp)
			) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(end = 16.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(16.dp)
				) {
					Card(
						modifier = Modifier
							.fillMaxWidth()
							.weight(1f)
							.clickable {
//                            onClick()
							},
						shape = RoundedCornerShape(14.dp),
						colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
						border = BorderStroke(width = 1.dp, color = Color.LightGray)
					) {
						Row(
							modifier = Modifier
								.padding(10.dp),
							horizontalArrangement = Arrangement.spacedBy(8.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							Icon(
								imageVector = Icons.Rounded.Search,
								contentDescription = null,
								tint = greenColor
							)
							Text(
								text = "cari dokter anda...",
								fontFamily = poppinsMedium,
								fontSize = 13.sp,
								color = Color.Gray
							)
						}
					}
					
					
				}
			}
			
			HorizontalDivider(
				thickness = 2.dp,
				color = whiteCustom
			)
		}
		
		item {
			HorizontalPager(
				state = pagerState,
				pageSize = PageSize.Fill,
				contentPadding = PaddingValues(horizontal = 16.dp) ,
				pageSpacing = 5.dp
			) { index ->
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.height(150.dp)
				) {
					AsyncImage(
						model = ImageRequest.Builder(context = context)
							.data(image[index])
							.build(),
						contentDescription = null,
						modifier = Modifier
							.clip(shape = RoundedCornerShape(16.dp))
							.fillMaxWidth()
							.height(150.dp),
						contentScale = ContentScale.Crop
					)
				}
			}
			
			PageIndicator(
				pageCount = image.size,
				currentPage = pagerState.currentPage,
				modifier = Modifier
					.padding(top = 6.dp)
					.background(color = Color.White)
			)
		}
		
		
		item {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Column(
					verticalArrangement = Arrangement.spacedBy(3.dp),
					horizontalAlignment = Alignment.Start
				) {
					Text(
						text = "Layanan unggulan",
						fontFamily = poppinsMedium,
						fontWeight = FontWeight.Medium,
						fontSize = 19.sp,
						color = Color.Black
					)
					
					Text(
						text = "Pilih layanan sesuai kebutuhan kesehatan Anda",
						fontFamily = poppinsLight,
						fontWeight = FontWeight.Medium,
						fontSize = 12.sp,
						color = Color.Black
					)
				}
			}
		}
		
		
		item {
			CategoryScreen(
				dataCategory = listCategory,
				navController = navController
			)
			
			HorizontalDivider(
				modifier = Modifier.padding(top = 6.dp),
				color = whiteCustom,
				thickness = 1.dp
			)
		}
		
	}
	
}

@Composable
fun CategoryScreen(
	dataCategory : List<CategoryItems> ,
	navController : NavController
) {
	val context = LocalContext.current
	LazyVerticalGrid(
		modifier = Modifier
			.fillMaxWidth()
			.height(90.dp)
			.background(color = Color.White)
			.padding(end = 6.dp, start = 6.dp, top = 4.dp),
		columns = GridCells.Fixed(4),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(dataCategory) { itemCategory ->
			Column(
				modifier = Modifier
					.size(80.dp),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				// Menggunakan BadgedBox untuk menambahkan badge
				BadgedBox(
					badge = {
						Badge(
							containerColor = greenColor,
							contentColor = Color.White,
							modifier = Modifier.offset(x = 3.dp, y = 3.dp)
						) {
							Text(
								text = "new",
								fontSize = 6.sp,
								fontWeight = FontWeight.Bold,
								modifier = Modifier.padding(horizontal = 2.dp)
							)
						}
					}
				) {
					
					// AsyncImage yang akan mendapat badge
					AsyncImage(
						modifier = Modifier
							.size(40.dp)
							.clip(shape = RoundedCornerShape(12.dp))
							.clickable {
								when (itemCategory.categoryID) {
									0 -> {
										navController.navigate(
											route = Polyclinic
										)
									}
								}
							},
						contentDescription = null,
						contentScale = ContentScale.Crop,
						model = ImageRequest.Builder(context = context)
							.data(itemCategory.imageRes)
							.error(R.drawable.logo)
							.build()
					)
				}
				
				Text(
					text = itemCategory.nameCategory,
					fontFamily = poppinsMedium,
					fontSize = 10.sp,
					maxLines = 2,
					textAlign = TextAlign.Center,
					overflow = TextOverflow.Ellipsis
				)
			}
		}
	}
}


