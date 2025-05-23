package com.ciputramitra.consultation.ui.polyclinic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ciputramitra.component.InformationBusy
import com.ciputramitra.component.LoadingLottieAnimation
import com.ciputramitra.consultation.R
import com.ciputramitra.consultation.ui.theme.greenColor
import com.ciputramitra.consultation.ui.theme.poppinsBold
import com.ciputramitra.consultation.ui.theme.poppinsLight
import com.ciputramitra.consultation.ui.theme.poppinsMedium
import com.ciputramitra.consultation.ui.theme.whiteCustom
import com.ciputramitra.navgraph.DoctorAllArgs
import com.ciputramitra.response.polyclinic.Data


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolyclinicScreen(
	navController : NavHostController,
	polyclinicViewModel : PolyclinicViewModel ,
) {
	val fetchPolyclinic = polyclinicViewModel.polyclinic.collectAsLazyPagingItems()
	
	LaunchedEffect(key1 = Unit) {
		polyclinicViewModel.fetchPolyclinic()
	}
	
	
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(color = Color.White),
		verticalArrangement = Arrangement.spacedBy(5.dp),
		horizontalAlignment = Alignment.Start
	) {
		stickyHeader {
			TopAppBar(
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = greenColor
				),
				title = {
					Text(
						text = "Kategori Poliklinik",
						fontFamily = poppinsBold,
						fontWeight = FontWeight.Medium,
						color = whiteCustom,
						fontSize = 17.sp
					)
				},
				navigationIcon = {
					IconButton(
						onClick = {
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
		}
		
		item {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(end = 14.dp, start = 14.dp, top = 8.dp, bottom = 8.dp),
				verticalArrangement = Arrangement.spacedBy(4.dp)
			) {
				Text(
					text = "Poliklinik",
					fontFamily = poppinsMedium,
					fontWeight = FontWeight.Medium,
					fontSize = 17.sp,
					letterSpacing = TextUnit.Unspecified
				)
				
				Text(
					fontFamily = poppinsLight,
					fontWeight = FontWeight.Normal,
					fontSize = 13.sp,
					text = "Konsultasi dengan Dokter Spesialis"
				)
			}
		}
		
		item {
			PolyclinicItem(
				categoryPolyclinic = fetchPolyclinic,
				navController = navController
			)
		}
		
		fetchPolyclinic.apply {
			when {
				loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> item {
					LoadingLottieAnimation()
				}
				
				loadState.refresh is LoadState.Error || loadState.append is LoadState.Error -> item {
					InformationBusy (
						message = "Mohon maaf server sedang sibuk" ,
						onRetryClick = { retry() }
					)
				}
			}
		}
		
	}
	
}

@Composable
fun PolyclinicItem(categoryPolyclinic : LazyPagingItems<Data> , navController : NavHostController) {
	val context = LocalContext.current
	LazyVerticalGrid(
		columns = GridCells.Fixed(2),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = Modifier
			.fillMaxWidth()
			.padding(start = 12.dp, end = 12.dp, bottom = 16.dp)
			.heightIn(min = 300.dp, max = 800.dp)
	) {
		
		items(
			count = categoryPolyclinic.itemCount,
			key = categoryPolyclinic.itemKey { it.id}
		) {
			val categoryPolyclinics = categoryPolyclinic[it]
			categoryPolyclinics?.let { categoriesPolyclinicItems ->
				Row (
					modifier = Modifier
						.fillMaxWidth()
						.padding(12.dp)
						.clickable {
							navController.navigate(
								route = DoctorAllArgs(
									polyclinicID = categoryPolyclinics.id
								)
							)
						},
					horizontalArrangement = Arrangement.spacedBy(4.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					AsyncImage(
						model = ImageRequest.Builder(context = context)
							.data(categoriesPolyclinicItems.icon)
							.error(R.drawable.logo)
							.build(),
						contentDescription = null,
						modifier = Modifier
							.size(width = 30.dp, height = 30.dp)
							.clip(shape = RoundedCornerShape(16.dp)),
						contentScale = ContentScale.Crop
					)
					
					Text(
						modifier = Modifier.padding(start = 5.dp),
						textAlign = TextAlign.Start,
						text = categoriesPolyclinicItems.name,
						fontFamily = poppinsLight,
						fontWeight = FontWeight.Normal,
						fontSize = 13.sp,
						maxLines = 3,
						overflow = TextOverflow.Ellipsis
					)
				}
			}
			
		}
	}
}


