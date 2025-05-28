package com.ciputramitra.consultation.ui.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowCircleRight
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ciputramitra.component.EmptyStateView
import com.ciputramitra.consultation.BuildConfig
import com.ciputramitra.consultation.R
import com.ciputramitra.consultation.ui.theme.black
import com.ciputramitra.consultation.ui.theme.greenColor
import com.ciputramitra.consultation.ui.theme.poppinsBold
import com.ciputramitra.consultation.ui.theme.poppinsLight
import com.ciputramitra.consultation.ui.theme.poppinsMedium
import com.ciputramitra.consultation.ui.theme.smoothColor
import com.ciputramitra.consultation.ui.theme.whiteCustom
import com.ciputramitra.response.doctor.DoctorItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorAllScreen(
	polyclinicID : Int ,
	doctorViewModel : DoctorViewModel ,
	navController : NavHostController ,
	namePolyclinic : String
) {
	
	val context = LocalContext.current
	val doctor = doctorViewModel.doctors.collectAsLazyPagingItems()
	var searchPicture by rememberSaveable { mutableStateOf(value = "") }
	
	val sheetState = rememberModalBottomSheetState()
	var isOpenReviews by rememberSaveable { mutableStateOf(value = false) }
	
	LaunchedEffect(key1 = Unit) {
		doctorViewModel.fetchDoctor(
			name = "",
			doctorPolyclinicID = polyclinicID,
			isPublished = 1,
			schedule = ""
		)
	}
	
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(color = Color.White),
		horizontalAlignment = Alignment.Start,
		verticalArrangement = Arrangement.spacedBy(14.dp)
	) {
		stickyHeader {
			Surface(
				modifier = Modifier.fillMaxWidth(),
				color = Color.White,
				// Tambahkan elevasi untuk membuat bayangan
				shadowElevation = 4.dp
			) {
				Column {
					TopAppBar(
						colors = TopAppBarDefaults.topAppBarColors(
							containerColor = greenColor
						),
						title = {
							Text(
								text = "Kumpulan Dokter",
								fontFamily = poppinsBold,
								fontWeight = FontWeight.Medium,
								fontSize = 17.sp,
								color = whiteCustom,
								maxLines = 1,
								overflow = TextOverflow.Ellipsis
							)
						},
						navigationIcon = {
							IconButton(
								onClick = {
									doctorViewModel.resetFilter()
									navController.navigateUp()
								},
								
								) {
								Icon(
									modifier = Modifier.padding(start = 10.dp, end = 10.dp),
									imageVector = Icons.Default.ArrowCircleLeft,
									contentDescription = null,
									tint = whiteCustom
								)
							}
							
						}
					)
					
					HorizontalDivider(
						thickness = 1.dp,
						color = whiteCustom
					)
					
					Row(
						modifier = Modifier
							.fillMaxSize()
							.background(color = Color.White),
						horizontalArrangement = Arrangement.SpaceBetween,
						verticalAlignment = Alignment.CenterVertically
					) {
						Text(
							modifier = Modifier.padding(start = 16.dp),
							text = "Menampilkan ${doctor.itemSnapshotList.items.size} Dokter\ndari ${namePolyclinic.lowercase()}",
							fontFamily = poppinsLight,
							fontWeight = FontWeight.Medium,
							fontSize = 15.sp,
							maxLines = 2,
							overflow = TextOverflow.Ellipsis,
							color = black
						)
						
						TextButton(
							onClick = { isOpenReviews = true },
							modifier = Modifier.padding(5.dp),
							colors = ButtonColors(
								containerColor = greenColor,
								contentColor = whiteCustom,
								disabledContentColor = greenColor,
								disabledContainerColor = whiteCustom
							)
						) {
							Row(
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.spacedBy(4.dp)
							) {
								Text(
									text = "Filter"
								)
								Icon(
									imageVector = Icons.Default.ArrowDropDown, // Atau ikon lain sesuai kebutuhan
									contentDescription = "Filter Icon"
								)
							}
						}
					}
					
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.background(Color.White)
							.padding(bottom = 8.dp)
					) {
						SearchUserNameDoctor(
							userNameDoctor = searchPicture,
							onUserNameDoctorChange = { searchPicture = it },
							onSearchClicked = {
								doctorViewModel.fetchDoctor(
									name = searchPicture,
									doctorPolyclinicID = polyclinicID,
									isPublished = 1,
									schedule =  ""
								)
							}
						)
					}
					
				}
			}
			
			
		}
		
		if (doctor.itemCount == 0 && doctor.loadState.refresh !is LoadState.Loading)
			item {
				EmptyStateView(
					message = "Belum ada dokter yang tersedia. Silakan periksa kembali nanti"
				)
			}
		else
			items(
				count = doctor.itemCount,
				key = doctor.itemKey { it.id }
			) {
				val doctors = doctor[it]
				doctors?.let { doctorsItems ->
					DoctorAllItem(
						doctorsItems = doctorsItems,
						navController = navController
					)
				}
				
				HorizontalDivider(
					modifier = Modifier.padding(top = 10.dp),
					thickness = 2.dp,
					color = whiteCustom
				)
			}
	}
	
	if (isOpenReviews) {
		ModalBottomSheet(
			sheetState = sheetState,
			onDismissRequest = {
				isOpenReviews = false
			}
		) {
			BottomSheetFilter(
				doctorViewModel = doctorViewModel,
				polyclinicID = polyclinicID,
				onReset = {
					isOpenReviews = false
				}
			)
		}
	}
}

@Composable
fun DoctorAllItem(
	doctorsItems : DoctorItems ,
	navController : NavController
) {
	Row(
		modifier = Modifier
			.clickable {}
			.fillMaxWidth()
			.padding(start = 12.dp, end = 16.dp),
		horizontalArrangement = Arrangement.spacedBy(13.dp)
	) {
		
		Box(
			modifier = Modifier.size(70.dp)
		) {
			
			AsyncImage(
				model = ImageRequest.Builder(context = LocalContext.current)
					.data("${BuildConfig.BASE_URL}${doctorsItems.photos.joinToString { it.name }}")
					.error(R.drawable.logo)
					.build(),
				contentDescription = null,
				modifier = Modifier
					.size(70.dp)
					.clip(shape = RoundedCornerShape(8.dp)),
				contentScale = ContentScale.Crop
			)
		}
		
		Column(
			modifier = Modifier
				.fillMaxWidth(),
			horizontalAlignment = Alignment.Start,
			verticalArrangement = Arrangement.spacedBy(2.dp)
		) {
			Text(
				text = doctorsItems.name ,
				fontFamily = poppinsMedium,
				fontSize = 15.sp,
				fontWeight = FontWeight.Medium,
				color = black,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
			
			Text(
				text = doctorsItems.specializationName,
				fontFamily = poppinsLight,
				fontSize = 14.sp,
				fontWeight = FontWeight.Medium,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
			
			Text(
				text = doctorsItems.polyclinic.name ,
				fontFamily = poppinsMedium,
				fontSize = 14.sp,
				fontWeight = FontWeight.Medium,
				color = black,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
			
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(7.dp)
				) {
					Box(
						modifier = Modifier
							.size(12.dp)
							.background(
								color = if(doctorsItems.isPublished == 1) greenColor else Color.Gray,
								shape = CircleShape
							)
							.border(
								width = 1.5.dp,
								color = Color.White,
								shape = CircleShape
							)
							// Posisikan di pojok kanan bawah
							.align(Alignment.CenterVertically)
							// Offset sedikit agar tidak terlalu di ujung
							.offset(x = (-4).dp, y = (-4).dp)
					)
					
					Text(
						text = if (doctorsItems.isPublished != 1) "offline" else "online",
						fontFamily = poppinsLight,
						fontWeight = FontWeight.Medium,
						color = if (doctorsItems.isPublished != 1) Color.LightGray else greenColor
					)
				}
				
				Icon(
					imageVector = Icons.Rounded.ArrowCircleRight,
					contentDescription = null,
					tint = if(doctorsItems.isPublished == 1) greenColor else Color.Gray,
				)
			}
			
		}
	}
}

@Composable
fun SearchUserNameDoctor(
	userNameDoctor: String,
	onUserNameDoctorChange: (String) -> Unit,
	onSearchClicked: () -> Unit
) {
	TextField(
		modifier = Modifier
			.fillMaxWidth()
			.padding(12.dp)
			.border(
				width = 2.dp,
				color = Color.LightGray,
				shape = RoundedCornerShape(12.dp)
			),
		value = userNameDoctor,
		onValueChange = onUserNameDoctorChange,
		placeholder = {
			Text(
				text = "silahkan cari nama dokter",
			)
		},
		textStyle = TextStyle(
			fontFamily = poppinsMedium,
			fontSize = 14.sp,
			color = black
		) ,
		shape = RoundedCornerShape(8.dp) ,
		colors = TextFieldDefaults.colors(
			focusedContainerColor = Color.Transparent,
			unfocusedContainerColor = Color.Transparent,
			focusedIndicatorColor = Color.Transparent,
			unfocusedIndicatorColor = Color.Transparent
		),
		leadingIcon = {
			Icon(
				imageVector = Icons.Default.Search,
				contentDescription = null,
				tint = smoothColor
			)
		},
		singleLine = true,
		keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search) ,
		keyboardActions = KeyboardActions(onSearch = { onSearchClicked()})
	)
}

@Composable
fun BottomSheetFilter(
	doctorViewModel : DoctorViewModel ,
	onReset : () -> Unit ,
	polyclinicID : Int ,
	
	) {
	
	// Observe selectedDay dari ViewModel
	val queryParams by doctorViewModel.queryParams.collectAsStateWithLifecycle()
	val selectedDay = queryParams.schedule
	
	Row(
		modifier = Modifier
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			modifier = Modifier
				.padding(start = 14.dp , bottom = 4.dp),
			text = "Filter Hari Konsultasi",
			fontFamily = poppinsMedium,
			fontSize = 14.sp,
			fontWeight = FontWeight.Medium,
		)
		
		Text(
			modifier = Modifier
				.padding(end = 18.dp , bottom = 4.dp)
				.clickable{
					doctorViewModel.fetchDoctor(
						name = "",
						doctorPolyclinicID = polyclinicID,
						isPublished = 1,
						schedule = ""
					)
					onReset()
				},
			text = "Reset",
			fontFamily = poppinsMedium,
			fontSize = 14.sp,
			fontWeight = FontWeight.Medium,
			color = greenColor
		)
	}
	
	HorizontalDivider(
		thickness = 1.dp,
		color = Color.LightGray
	)
	
	FilterToday(
		selectedDay = selectedDay,
		onTodayClicked = { selectedToday ->
			doctorViewModel.fetchDoctor(
				name = "",
				doctorPolyclinicID = polyclinicID,
				isPublished = 1,
				schedule = selectedToday
			)
		}
	)
	
}

@Composable
fun FilterToday(
	onTodayClicked: (String) -> Unit,
	selectedDay: String?
) {
	
	FlowRow(
		modifier = Modifier.fillMaxWidth()
			.padding(12.dp),
		maxItemsInEachRow = 8,
		horizontalArrangement = Arrangement.spacedBy(6.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		
		val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
		
		days.forEach { day ->
			FilterText(
				text = day,
				isSelected = selectedDay == day,
				onClick = {
					onTodayClicked(day)
				}
			)
		}
		
	}
	
}

@Composable
fun FilterText(
	text : String ,
	isSelected : Boolean ,
	onClick : () -> Unit
) {
	Text(
		modifier = Modifier
			.background(
				color = if (isSelected) greenColor else Color.LightGray ,
				shape = RoundedCornerShape(6.dp)
			)
			.clickable { onClick() }
			.padding(10.dp) ,
		text = text ,
		fontSize = 10.sp ,
		fontFamily = poppinsMedium ,
		fontWeight = FontWeight.Bold ,
		color = Color.White
	)
}
