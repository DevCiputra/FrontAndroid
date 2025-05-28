package com.ciputramitra.navgraph

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ciputramitra.component.bottomNavigation
import com.ciputramitra.consultation.ui.article.ArticlesScreen
import com.ciputramitra.consultation.ui.auth.AuthViewModel
import com.ciputramitra.consultation.ui.auth.LoginScreen
import com.ciputramitra.consultation.ui.auth.RegisterScreen
import com.ciputramitra.consultation.ui.auth.RequestOtpScreen
import com.ciputramitra.consultation.ui.auth.ResetPasswordScreen
import com.ciputramitra.consultation.ui.auth.VerifyPasswordScreen
import com.ciputramitra.consultation.ui.doctor.DoctorAllScreen
import com.ciputramitra.consultation.ui.doctor.DoctorViewModel
import com.ciputramitra.consultation.ui.history.HistoryScreen
import com.ciputramitra.consultation.ui.home.HomeScreen
import com.ciputramitra.consultation.ui.polyclinic.PolyclinicScreen
import com.ciputramitra.consultation.ui.polyclinic.PolyclinicViewModel
import com.ciputramitra.consultation.ui.profile.ProfileScreen
import com.ciputramitra.consultation.ui.theme.poppinsMedium
import com.ciputramitra.consultation.ui.theme.whiteCustom

@Composable
fun NavGraph(
	authViewModel : AuthViewModel ,
	polyclinicViewModel : PolyclinicViewModel ,
	doctorViewModel : DoctorViewModel ,
) {
	val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle(initialValue = false)
	val token by authViewModel.token.collectAsStateWithLifecycle()
	val fetchUser by authViewModel.fetchUser.collectAsStateWithLifecycle()
	
	val navController = rememberNavController()
	var selectedItemIndex by rememberSaveable { mutableIntStateOf(value = 0) }
	val context = LocalContext.current
	
	NavHost(
		navController = navController,
		startDestination = if (isLoggedIn && token != null) Home else Login
	) {
		composable<Login> {
			LoginScreen(
				onLoginSuccess = {
					if (token != null)
						navController.navigate(route = Home) {
							popUpTo(route = Login) { inclusive = true }
						}
				},
				navController = navController,
				authViewModel = authViewModel
			)
		}
		
		composable<Register> {
			RegisterScreen(
				onRegisterSuccess = {
					navController.navigate(route = Login) {
						popUpTo(route = Login) { inclusive = true }
					}
					
				},
				navController = navController,
				authViewModel = authViewModel
			)
		}
		
		composable<Home> {
			BackHandler(
				enabled = true
			) {
				if (selectedItemIndex == 0)
					(context as Activity).finish()
				else selectedItemIndex = 0
			}
			
			Scaffold(
				bottomBar = {
					NavigationBar(
						modifier = Modifier.shadow(elevation = 20.dp),
						containerColor = Color.White
					) {
						bottomNavigation().forEachIndexed { index, items ->
							NavigationBarItem(
								selected = selectedItemIndex == index,
								onClick = { selectedItemIndex = index },
								label = {
									Text(
										text = items.title,
										color = if (index == selectedItemIndex)
											items.selectedColor
										else items.unSelectedColor,
										fontFamily = poppinsMedium,
										fontSize = 12.sp,
										maxLines = 1,
										overflow = TextOverflow.Ellipsis
									)
								},
								icon = {
									Icon(
										imageVector = if (index == selectedItemIndex)
											items.selectedIcon
										else items.unSelectedIcon,
										tint = if (index == selectedItemIndex)
											items.selectedIconColor else items.unSelectedIconColor,
										contentDescription = items.title,
										modifier = Modifier.size(22.dp)
									)
								},
								colors = NavigationBarItemDefaults.colors(
									indicatorColor = whiteCustom
								)
							)
						}
					}
				}
			) { paddingValues ->
				Box(
					modifier = Modifier
						.background(color = Color.White)
						.padding(paddingValues)
				) {
					when (selectedItemIndex) {
						0 -> HomeScreen(
							navController = navController,
							fetchUser = fetchUser
						)
						
						1 -> ArticlesScreen(
							navController = navController,
						)
						
						2 -> HistoryScreen(
							navController = navController,
						)
						
						3 -> ProfileScreen(
							navController = navController
						)
					}
				}
			}
		}
		
		composable<VerifyEmailRoute> {
			BackHandler {
				navController.navigateUp()
			}
			VerifyPasswordScreen(
				authViewModel = authViewModel,
				navController = navController,
			)
		}
		
		composable<VerifyOtpArgs> {
			BackHandler {
				navController.navigateUp()
				authViewModel.clearAuthState()
			}
			
			val args = it.toRoute<VerifyOtpArgs>()
			RequestOtpScreen(
				email = args.email,
				authViewModel = authViewModel,
				navController = navController
			)
		}
		
		composable<ResetPasswordArgs> {
			BackHandler {
				navController.navigateUp()
				authViewModel.clearAuthState()
			}
			
			val args = it.toRoute<ResetPasswordArgs>()
			ResetPasswordScreen(
				onResetSuccess = {
					navController.navigate(
						route = Login
					)
				},
				userID = args.userID,
				authViewModel = authViewModel,
				navController = navController
			)
		}
		
		
		composable<Polyclinic> {
			BackHandler {
				navController.navigateUp()
			}
			
			PolyclinicScreen(
				navController = navController,
				polyclinicViewModel = polyclinicViewModel
			)
		}
		
		composable<DoctorAllArgs> {
			BackHandler {
				navController.navigateUp()
			}
			
			val args = it.toRoute<DoctorAllArgs>()
			DoctorAllScreen(
				polyclinicID = args.polyclinicID,
				namePolyclinic = args.namePolyclinic,
				doctorViewModel = doctorViewModel,
				navController = navController
			)
		}
		
		
	}
}